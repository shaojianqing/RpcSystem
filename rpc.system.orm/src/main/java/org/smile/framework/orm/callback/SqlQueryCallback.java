package org.smile.framework.orm.callback;

import java.sql.ResultSet;

public interface SqlQueryCallback {
	
	public Object onExecuteResult(ResultSet resultSet) throws Exception;
}
