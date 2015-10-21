package com.wyk.esaydb.session;

import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wyk.esaydb.pool.DataSourceFactory;

/**
 * 持有连接池，构造session工场
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月20日
 * @version 1.0
 */
public class SqlSessionFactoryBuilder {

	private static SqlSessionFacotry sqlSessionFactory = null;
	
	public synchronized static SqlSessionFacotry builder(Properties p)throws Exception{
		if(sqlSessionFactory == null){
			ComboPooledDataSource pool = DataSourceFactory.createDataSource(p);
			sqlSessionFactory = new SqlSessionFacotry(pool);
		}
		return sqlSessionFactory;
	}
}
