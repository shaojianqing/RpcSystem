package org.smile.service.sequence.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.smile.service.domain.sequence.Sequence;
import org.smile.service.domain.sequence.SequenceEnum;
import org.smile.service.exception.BizException;
import org.smile.service.sequence.ISequenceService;
import org.smile.service.sequence.dao.ISequenceDao;

public class SequenceServiceImpl implements ISequenceService {
	
	private ISequenceDao sequenceDao;

	public String createBizSequence(SequenceEnum sequenceEnum) {
		if (sequenceEnum!=null) {
			Sequence sequence = sequenceDao.getSequenceByName(sequenceEnum.getName());
			Long sequenceValue = 1l;
			if (sequence!=null) {
				sequenceValue = sequence.getValue() + 1;
				sequenceDao.updateSequenceValue(sequence.getName(), sequenceValue);
			} else {
				Timestamp timestamp = new Timestamp(new Date().getTime());
				sequence = new Sequence();
				sequence.setMinValue(1l);
				sequence.setName(sequenceEnum.getName());
				sequence.setValue(sequenceValue);
				sequence.setMaxValue(99999999999l);
				sequence.setCreateTime(timestamp);
				sequence.setOperateTime(timestamp);
				
				sequenceDao.saveSequence(sequence);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String timePrefix = formatter.format(new Date());
			
			String sequenceString = String.format("%s0000%08d", timePrefix, sequenceValue);
			return sequenceString;
		} else {
			throw new BizException("SequenceEnum can not be null!");
		}
	}

	public ISequenceDao getSequenceDao() {
		return sequenceDao;
	}

	public void setSequenceDao(ISequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}
}
