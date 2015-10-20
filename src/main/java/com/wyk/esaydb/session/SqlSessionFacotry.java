package com.wyk.esaydb.session;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * 工场模式创建SqlSession对象
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月18日
 * @version 1.0
 */
public class SqlSessionFacotry {

	private ComboPooledDataSource dataSourcePool;
	
	protected SqlSessionFacotry(ComboPooledDataSource dataSourcePool){
		this.dataSourcePool = dataSourcePool;
	}
	/**
	 * 默认(自动)事务提交方式获取sqlsession
	 * @return
	 */
	public SqlSession openSession(){
		SqlSession sqlSession = null;
		try {
			Connection connection = dataSourcePool.getConnection();
			sqlSession = new SqlSession(connection);
		} catch (SQLException e) {
			throw new RuntimeException("连接获取失败",e);
		}
		return sqlSession;
	}
}
