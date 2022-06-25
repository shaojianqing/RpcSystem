package org.smile.service.operaterecord.dao.impl;

import org.smile.framework.orm.template.SqlMapClientTemplate;
import org.smile.service.domain.record.OperateRecord;
import org.smile.service.operaterecord.dao.IOperateRecordDao;

public class OperateRecordDaoImpl implements IOperateRecordDao {
	
	private SqlMapClientTemplate sqlMapClientTemplate;

	public OperateRecord saveOperateRecord(OperateRecord operateRecord) {
		if (operateRecord!=null) {
			try {
				int result = sqlMapClientTemplate.execute("org.smile.service.domain.record.saveOperateRecord", operateRecord);
				if (result>0) {
					return operateRecord;
				} else {
					return null;
				}
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return operateRecord;
	}

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
}
