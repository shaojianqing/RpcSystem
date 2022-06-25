package org.smile.framework.orm.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class TransactionManager {
	
	private DataSource dataSource;
	
	public void beginTransaction() throws SQLException {
		Connection connection = this.dataSource.getConnection();
		connection.setAutoCommit(false);
	}
	
	public void commitTransaction() throws SQLException {
		Connection connection = this.dataSource.getConnection();
		connection.commit();
	}
	
	public void rollbacTransaction() throws SQLException {
		Connection connection = this.dataSource.getConnection();
		connection.rollback();
	}
	
	public void setInitialCommit() throws SQLException {
		Connection connection = this.dataSource.getConnection();
		connection.setAutoCommit(true);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
