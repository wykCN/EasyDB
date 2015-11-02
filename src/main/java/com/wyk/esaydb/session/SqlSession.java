package com.wyk.esaydb.session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wyk.esaydb.exception.UniqueNotFoundException;
import com.wyk.esaydb.interfaces.IEntity;
import com.wyk.esaydb.jdbc.SqlRunner;

/**
 * 
 * 执行sql操作的会话
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月15日
 * @version 1.0
 */
public class SqlSession<T extends IEntity> {
	private Connection connection;
	private Class<T> clazz ;
	private SqlRunner<T> sqlRunner;
	/**
	 * constructor
	 * @param connection
	 * @param clazz
	 */
	public SqlSession(Connection connection,Class<T> clazz) {
		this.connection = connection;
		this.clazz = clazz;
		sqlRunner = new SqlRunner<T>(clazz);
	}
	
	public boolean save(T obj){
		boolean flag = false;
		try {
			flag = sqlRunner.executeInsert(connection, obj);
		} catch (SQLException e) {
			throw new RuntimeException("插入实体失败",e);
		}
		return flag;
	}
	public boolean delete(T obj){
		boolean flag = false;
		try {
			flag = sqlRunner.executeDelete(connection, obj);
		} catch (SQLException e) {
			throw new RuntimeException("删除实体失败",e);
		} catch (UniqueNotFoundException e) {
			throw new RuntimeException("未找到实体的唯一索引",e);
		}
		return flag;
	}
	public boolean delete(T obj,Map<String,Object> conditions){
		boolean flag = false;
		try {
			flag = sqlRunner.executeDelete(connection, obj, conditions);
		} catch (SQLException e) {
			throw new RuntimeException("删除实体失败",e);
		}
		return flag;
	}
	public boolean update(T obj){
		boolean flag = false;
		try {
			flag = sqlRunner.executeUpdate(connection, obj);
		} catch (SQLException e) {
			throw new RuntimeException("更新实体失败",e);
		} catch (UniqueNotFoundException e) {
			throw new RuntimeException("未找到实体的唯一索引",e);
		}
		return flag;
	}
	public boolean update(T obj,Map<String,Object> conditions){
		boolean flag = false;
		try {
			flag = sqlRunner.executeUpdate(connection, obj, conditions);
		} catch (SQLException e) {
			throw new RuntimeException("更新实体失败",e);
		}
		return flag;
	}
	public T find(T obj){
		List<T> list = new ArrayList<T>();
		try {
			list = sqlRunner.executeQuery(connection, obj);
		} catch (SQLException e) {
			throw new RuntimeException("更新实体失败",e);
		} catch (UniqueNotFoundException e) {
			throw new RuntimeException("未找到实体的唯一索引",e);
		}
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	public List<T> find(Map<String,Object> conditions){
		List<T> list = new ArrayList<T>();
		try {
			list = sqlRunner.executeQuery(connection, clazz, conditions);
		} catch (SQLException e) {
			throw new RuntimeException("查找实体集失败",e);
		}
		return list;
	}
	public List<T> findLike(Map<String,Object> conditions){
		List<T> list = new ArrayList<T>();
		try {
			list = sqlRunner.executeQueryLike(connection, clazz, conditions);
		} catch (SQLException e) {
			throw new RuntimeException("查找实体集失败",e);
		}
		return list;
	}
	
	public List<T> findByCustomSql(Class<T> customType,String sql,Object[] params){
		List<T> list = new ArrayList<T>();
		try {
			list = sqlRunner.executeCostomQuery(connection, customType, sql, params);
		} catch (SQLException e) {
			throw new RuntimeException("更新实体失败",e);
		}
		return list;
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
