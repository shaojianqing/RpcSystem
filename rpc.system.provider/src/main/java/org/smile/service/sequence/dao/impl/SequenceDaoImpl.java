package org.smile.service.sequence.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.smile.framework.orm.template.SqlMapClientTemplate;
import org.smile.service.domain.sequence.Sequence;
import org.smile.service.sequence.dao.ISequenceDao;

public class SequenceDaoImpl implements ISequenceDao {
	
	private SqlMapClientTemplate sqlMapClientTemplate;

	public Sequence getSequenceByName(String name) {
		try {
			return (Sequence)sqlMapClientTemplate.queryForObject("org.smile.service.domain.sequence.getSequenceByName", name);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Sequence saveSequence(Sequence sequence) {
		try {
			int result = sqlMapClientTemplate.execute("org.smile.service.domain.sequence.saveSequence", sequence);
			if (result>0) {
				return sequence;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Long updateSequenceValue(String name, Long value) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("name", name);
			param.put("value", value);
			
			int result = sqlMapClientTemplate.execute("org.smile.service.domain.sequence.updateSequenceValue", param);
			if (result>0) {
				return value;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1l;
	}

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
}
