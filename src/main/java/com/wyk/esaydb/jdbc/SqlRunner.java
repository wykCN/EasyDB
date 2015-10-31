package com.wyk.esaydb.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;

import com.wyk.esaydb.interfaces.IEntity;
import com.wyk.esaydb.utils.SqlBuilder;
import com.wyk.esaydb.utils.SqlHolder;

/**
 * 
 * 执行sql查询
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月18日
 * @version 1.0
 */
public class SqlRunner<T extends IEntity> {

	private QueryRunner runner;
	private Class<T> clazz;
	private ListResultSetHandler<T> handler;

	public SqlRunner(Class<T> type) {
		super();
		this.setClazz(type);
		this.handler = new ListResultSetHandler<T>(type);
		runner = new QueryRunner();
	}

	public boolean executeInsert(Connection conn,T obj) throws SQLException{
		SqlHolder sqlHolder = SqlBuilder.buildInsert(obj);
		int rows = runner.update(conn, sqlHolder.getSql(), sqlHolder.getParams());
		return rows > 0;
	}
	public boolean executeUpdate(Connection conn,T obj,Map<String,Object> conditions) throws SQLException {
		SqlHolder sqlHolder = SqlBuilder.buildUpdate(obj, conditions);
		int rows = runner.update(conn, sqlHolder.getSql(), sqlHolder.getParams());
		return rows > 0;
	}
	public boolean executeDelete(Connection conn,T obj,Map<String,Object> conditions) throws SQLException{
		SqlHolder sqlHolder = SqlBuilder.buildDelete(obj, conditions);
		int rows = runner.update(conn, sqlHolder.getSql(), sqlHolder.getParams());
		return rows > 0;
	}
	public List<T> executeQuery(Connection conn,Class<T> clazz,Map<String,Object> conditions) throws SQLException {
		SqlHolder sqlHolder = SqlBuilder.buildFind(clazz, conditions);
		List<T> list = runner.query(conn, sqlHolder.getSql(), handler,sqlHolder.getParams());
		return list;
	}
	public List<T> executeQueryLike(Connection conn,Class<T> clazz,Map<String,Object> conditions) throws SQLException {
		SqlHolder sqlHolder = SqlBuilder.buildLikeFind(clazz, conditions);
		List<T> list = runner.query(conn, sqlHolder.getSql(), handler,sqlHolder.getParams());
		return list;
	}
	/**
	 * costomer Sql
	 * @param conn
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<T> executeCostomQuery(Connection conn,Class<T> clazz,String sql,Object[] params) throws SQLException{
		ListResultSetHandler<T> customHandler = new ListResultSetHandler<T>(clazz) ;
		List<T> list = runner.query(conn, sql,customHandler,params);
		return list;
	}
	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}
}
