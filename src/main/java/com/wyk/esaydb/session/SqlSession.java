package com.wyk.esaydb.session;

import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 
 * 执行sql操作的会话
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月15日
 * @version 1.0
 */
public class SqlSession {
	private DataSource dataSource;
	
	public void beginTransaction(){
		
	}
	
	
	public void commitTransaction(){
		
	}
	public void closeResource(){
		//close connectionn
	}
}
