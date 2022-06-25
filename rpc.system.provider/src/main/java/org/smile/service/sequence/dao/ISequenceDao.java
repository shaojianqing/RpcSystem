package org.smile.service.sequence.dao;

import org.smile.service.domain.sequence.Sequence;

public interface ISequenceDao {
	
	public Sequence getSequenceByName(String name);
	
	public Sequence saveSequence(Sequence sequence);
	
	public Long updateSequenceValue(String name, Long value); 
}
