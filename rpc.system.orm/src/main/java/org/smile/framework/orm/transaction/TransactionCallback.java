package org.smile.framework.orm.transaction;

public interface TransactionCallback<T> {
	
	public T doTransaction();
}
