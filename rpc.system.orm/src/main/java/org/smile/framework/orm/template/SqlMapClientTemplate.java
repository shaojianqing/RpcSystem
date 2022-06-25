package org.smile.framework.orm.template;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.smile.framework.core.util.ClassUtil;
import org.smile.framework.core.util.LogUtil;
import org.smile.framework.orm.callback.SqlQueryCallback;
import org.smile.framework.orm.datamap.PropertyMap;
import org.smile.framework.orm.datamap.ResultDataMap;
import org.smile.framework.orm.exception.SqlTemplateException;
import org.smile.framework.orm.parser.SqlMapConfigParser;
import org.smile.framework.orm.statement.SqlStatement;
import org.smile.framework.orm.util.TypeUtil;

@SuppressWarnings("rawtypes")
public class SqlMapClientTemplate {
	
	private SqlMapConfigParser parser = new SqlMapConfigParser();
	
	private DataSource dataSource;
	
	private Map<String, SqlStatement> statementMap = new HashMap<String, SqlStatement>();
	
	private List<String> mapperList;
	
	public void initTemplate() throws Exception {
		
		LogUtil.i("start to initialize SqlMapClientTemplate!");
		
		if (mapperList!=null && mapperList.size()>0) {			
			for (String mapperConfigPath:mapperList) {
				InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(mapperConfigPath);
				SAXReader xmlReader = new SAXReader();
		        
		        Document document = xmlReader.read(inputStream);
		        Element root = document.getRootElement();
		        
		        parser.buildSqlMapConfig(statementMap, root, dataSource);     
			}
		} else {
			throw new SqlTemplateException("Sql Mapping File List Can not be Empty!");
		}
	}
	
	public List queryForList(String statementName) throws Exception {
		return queryForList(statementName, null);
	}
	
	public List queryForList(String statementName, Object parameter) throws Exception {
		if (statementMap.containsKey(statementName)) {
			final SqlStatement sqlStatement = statementMap.get(statementName);
			List list = (List)sqlStatement.executeQuerySql(parameter, new SqlQueryCallback() {

				@SuppressWarnings("unchecked")
				public Object onExecuteResult(ResultSet resultSet) throws Exception {
					ResultDataMap resultDataMap = sqlStatement.getResultDataMap();
					String className = resultDataMap.getClassName();
					List resultList = new ArrayList();
					
					if (StringUtils.isNotBlank(className)) {
						Class<?> clazz = Class.forName(className);
						Map<String, Method> methodMap = ClassUtil.prepareMethodMap(clazz);
						List<PropertyMap> propertyMapList = resultDataMap.getPropertyList();
						while(resultSet.next()) {
							Object data = clazz.newInstance();
							for (PropertyMap propertyMap:propertyMapList) {
								prepareDataValue(resultSet, data, methodMap, propertyMap);
							}							
							resultList.add(data);
						}
						return resultList;
					} else {
						throw new SqlTemplateException("Can not find Bean Class Name Definition!");
					}	
				}
			});
			return list;
		} else {
			String message = String.format("%s statement does not exist!", statementName);
			throw new SqlTemplateException(message);
		}
	}
	
	public Object queryForObject(String statementName) throws Exception {		
		return queryForObject(statementName, null);
	}
	
	public Object queryForObject(String statementName, Object parameter) throws Exception {
		if (statementMap.containsKey(statementName)) {
			final SqlStatement sqlStatement = statementMap.get(statementName);
			
			Object data = sqlStatement.executeQuerySql(parameter, new SqlQueryCallback() {

				public Object onExecuteResult(ResultSet resultSet) throws Exception {
					ResultDataMap resultDataMap = sqlStatement.getResultDataMap();
					String className = resultDataMap.getClassName();
					
					if (StringUtils.isNotBlank(className)) {
						Class<?> clazz = Class.forName(className);
						Object data = clazz.newInstance();
						Map<String, Method> methodMap = ClassUtil.prepareMethodMap(clazz);
						List<PropertyMap> propertyMapList = resultDataMap.getPropertyList();
						if (resultSet.next()) {
							for (PropertyMap propertyMap:propertyMapList) {
								prepareDataValue(resultSet, data, methodMap, propertyMap);
							}
							return data;
						}
					} else {
						throw new SqlTemplateException("Can not find Bean Class Name Definition!");
					}
					
					return null;
				}
			});
			
			return data;
		} else {
			String message = String.format("%s statement does not exist!", statementName);
			throw new SqlTemplateException(message);
		}
	}
	
	public int countForObject(String statementName, Object parameter) {
		
		return 0;
	}
	
	public int execute(String statementName, Object parameter) throws Exception {
		if (statementMap.containsKey(statementName)) {
			SqlStatement sqlStatement = statementMap.get(statementName);
			return sqlStatement.executeUpdateSql(parameter);
		} else {
			String message = String.format("%s statement does not exist!", statementName);
			throw new SqlTemplateException(message);
		}
	}

	private void prepareDataValue(ResultSet resultSet, Object data, Map<String, Method> methodMap,
			PropertyMap propertyMap) throws SQLException, IllegalAccessException, InvocationTargetException {
		if (TypeUtil.TYPE_STRING.equals(propertyMap.getJavaType())) {
			String value = resultSet.getString(propertyMap.getColumnName());
			setDataValue(value, data, methodMap, propertyMap);
		} else if (TypeUtil.TYPE_DATE.equals(propertyMap.getJavaType())) {
			Date value = resultSet.getDate(propertyMap.getColumnName());
			setDataValue(value, data, methodMap, propertyMap);
		} else if (TypeUtil.TYPE_DOUBLE.equals(propertyMap.getJavaType())) {
			Double value = resultSet.getDouble(propertyMap.getColumnName());
			setDataValue(value, data, methodMap, propertyMap);
		} else if (TypeUtil.TYPE_SHORT.equals(propertyMap.getJavaType())) {
			Short value = resultSet.getShort(propertyMap.getColumnName());
			setDataValue(value, data, methodMap, propertyMap);
		} else if (TypeUtil.TYPE_FLOAT.equals(propertyMap.getJavaType())) {
			Float value = resultSet.getFloat(propertyMap.getColumnName());
			setDataValue(value, data, methodMap, propertyMap);
		} else if (TypeUtil.TYPE_INTEGER.equals(propertyMap.getJavaType())) {
			Integer value = resultSet.getInt(propertyMap.getColumnName());
			setDataValue(value, data, methodMap, propertyMap);
		} else if (TypeUtil.TYPE_LONG.equals(propertyMap.getJavaType())) {
			Long value = resultSet.getLong(propertyMap.getColumnName());
			setDataValue(value, data, methodMap, propertyMap);
		} else if (TypeUtil.TYPE_TIMESTAMP.equals(propertyMap.getJavaType())) {
			Timestamp value = resultSet.getTimestamp(propertyMap.getColumnName());
			setDataValue(value, data, methodMap, propertyMap);
		}
	}
	
	private void setDataValue(Object value, Object data, Map<String, Method> methodMap, PropertyMap propertyMap)
			throws SQLException, IllegalAccessException, InvocationTargetException {
		String writerMethodName = ClassUtil.getWriteMethodName(propertyMap.getPropertyName());
		if (methodMap.containsKey(writerMethodName)) {
			Method method = methodMap.get(writerMethodName);
			method.invoke(data, value);
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<String> getMapperList() {
		return mapperList;
	}

	public void setMapperList(List<String> mapperList) {
		this.mapperList = mapperList;
	}
}
