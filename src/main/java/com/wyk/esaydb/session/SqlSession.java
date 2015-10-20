package com.wyk.esaydb.session;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * 执行sql操作的会话
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月15日
 * @version 1.0
 */
public class SqlSession {
	private Connection connection;
	
	public SqlSession(Connection connection) {
		this.connection = connection;
	}


	public void beginTransaction() throws SQLException{
		connection.setAutoCommit(false);
	}
	public void commitTransaction() throws SQLException{
		connection.commit();
	}
	public void rollback() throws SQLException{
		connection.rollback();
	}
	public void closeResource() throws SQLException{
		//close connectionn
		if(connection != null){
			if(!connection.isClosed())
				connection.close();
		}
	}
}
