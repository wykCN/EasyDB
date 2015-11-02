package com.wyk.esaydb.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.wyk.esaydb.annotation.Column;
import com.wyk.esaydb.annotation.Table;
import com.wyk.esaydb.annotation.Transient;
import com.wyk.esaydb.exception.UniqueNotFoundException;
import com.wyk.esaydb.interfaces.IEntity;

/**
 * 
 * ** to builder sql by this class
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月14日
 * @version 1.0
 */
public class SqlBuilder {
	/**
	 * 单表的插入语句构造
	 * 
	 * @param entity
	 * @return
	 */
	public static SqlHolder buildInsert(IEntity entity) {
		//parse element
		SqlHolder holder = new SqlHolder();
		Field[] fields = entity.getClass().getDeclaredFields();

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		for (Field f : fields) {
			if(isTransient(f)){
				continue;
			}
			if(!insertable(f)){
				continue ;
			}
			holder.addParam(convert(getValue(entity,f)));
			columns.append(getColumnName(f)).append(",");
			values.append("?").append(",");
		}
		deleteLastComma(columns);
		deleteLastComma(values);
		
		//builder sql
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(getTableName(entity.getClass())).append("(");
		sql.append(columns).append(")");
		sql.append(" VALUES(").append(values).append(")");
		holder.setSql(sql.toString());
		return holder;
		
	}
	/**
	 * 单表更新,唯一键
	 * @param entity
	 * @param conditions
	 * @return
	 * @throws UniqueNotFoundException 
	 */
	public static SqlHolder buildUpdate(IEntity entity) throws UniqueNotFoundException{
		//parse element
		SqlHolder holder = new SqlHolder();
		Field[] declaredFields = entity.getClass().getDeclaredFields();

		StringBuilder columns = new StringBuilder();
		for (Field field : declaredFields) {
			if(isTransient(field)){
				continue;
			}
			if(!updatable(field)){
				continue;
			}
			holder.addParam(convert(getValue(entity,field)));
			columns.append(getColumnName(field)).append("=?,");
		}
		deleteLastComma(columns);
		//build sql
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(getTableName(entity.getClass())).append(" SET ");
		sql.append(columns.toString());
		holder.setSql(sql.toString());
		return _buildWhereUniqueSqlHolder(entity,holder);
	}
	
	/**
	 * 单表更新,自定义条件
	 * @param entity
	 * @param conditions
	 * @return
	 */
	public static SqlHolder buildUpdate(IEntity entity,Map<String,Object> conditions){
		//parse element
		SqlHolder holder = new SqlHolder();
		Field[] declaredFields = entity.getClass().getDeclaredFields();

		StringBuilder columns = new StringBuilder();
		for (Field field : declaredFields) {
			if(isTransient(field)){
				continue;
			}
			if(!updatable(field)){
				continue;
			}
			holder.addParam(convert(getValue(entity,field)));
			columns.append(getColumnName(field)).append("=?,");
		}
		deleteLastComma(columns);
		//build sql
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(getTableName(entity.getClass())).append(" SET ");
		sql.append(columns.toString());
		holder.setSql(sql.toString());
		return _buildWhereSqlHolder(holder, conditions);
	}
	
	/**
	 * 根据唯一键删除
	 * @param entity
	 * @param conditions
	 * @return
	 * @throws UniqueNotFoundException 
	 */
	public static SqlHolder buildDelete(IEntity entity) throws UniqueNotFoundException {
		SqlHolder holder = new SqlHolder();
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(getTableName(entity.getClass()));
		
		holder.setSql(sql.toString());
		return _buildWhereUniqueSqlHolder(entity,holder);
	}
	
	/**
	 * 自定义条件删除
	 * @param entity
	 * @param conditions
	 * @return
	 */
	public static SqlHolder buildDelete(IEntity entity,Map<String,Object> conditions) {
		SqlHolder holder = new SqlHolder();
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(getTableName(entity.getClass()));
		
		holder.setSql(sql.toString());
		
		return _buildWhereSqlHolder(holder,conditions);
	}
	
	/**
	 * 根据唯一键查询
	 * @param entity
	 * @param conditions
	 * @return
	 * @throws UniqueNotFoundException 
	 */
	public static SqlHolder buildFind(IEntity entity) throws UniqueNotFoundException{
		SqlHolder holder = new SqlHolder();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ").append(getTableName(entity.getClass()));
		
		holder.setSql(sql.toString());
		
		return _buildWhereUniqueSqlHolder(entity,holder);
	}
	
	/**
	 * 条件查询
	 * @param entity
	 * @param conditions
	 * @return
	 */
	public static SqlHolder buildFind(Class<? extends IEntity> clazz,Map<String,Object> conditions){
		SqlHolder holder = new SqlHolder();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ").append(getTableName(clazz));
		
		holder.setSql(sql.toString());
		
		return _buildWhereSqlHolder(holder,conditions);
	}
	
	/**
	 * 模糊条件查询
	 * @param entity
	 * @param conditions
	 * @return
	 */
	public static SqlHolder buildLikeFind(Class<? extends IEntity> clazz,Map<String,Object> conditions){
		SqlHolder holder = new SqlHolder();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ").append(getTableName(clazz));
		
		holder.setSql(sql.toString());
		
		return _buildWhereLikeSqlHolder(holder,conditions);
	}
	
	private static SqlHolder _buildWhereUniqueSqlHolder(IEntity entity,SqlHolder holder) throws UniqueNotFoundException{
		boolean flag = false;
		StringBuilder sql = new StringBuilder();
		sql.append(holder.getSql());
		sql.append(" WHERE ");
		
		Field[] declaredFields = entity.getClass().getDeclaredFields();
		
		for (Field field : declaredFields) {
			if(!isUnique(field)){
				continue;
			}
			sql.append(getColumnName(field)).append("=? and ");
			holder.addParam(convert(getValue(entity, field)));
			flag = true;
		}
		
		if(!flag){
			throw new UniqueNotFoundException();
		}
		sql = deleteLastAND(sql);
		holder.setSql(sql.toString());
		return holder;
	}
	

	private static SqlHolder _buildWhereSqlHolder(SqlHolder holder,
			Map<String, Object> conditions) {
		StringBuilder sql = new StringBuilder();
		sql.append(holder.getSql());
		sql.append(" WHERE ");
		for(String column : conditions.keySet()){
			Object obj = conditions.get(column);
			sql.append(column.toUpperCase()+" = ? and ");
			holder.addParam(obj);
		}
		sql = deleteLastAND(sql);
		holder.setSql(sql.toString());
		return holder;
	}
	
	private static SqlHolder _buildWhereLikeSqlHolder(SqlHolder holder,
			Map<String, Object> conditions) {
		StringBuilder sql = new StringBuilder();
		sql.append(holder.getSql());
		sql.append(" WHERE ");
		for(String column : conditions.keySet()){
			Object obj = conditions.get(column);
			sql.append(column.toUpperCase()+" LIKE ? and ");
			holder.addParam("%"+obj+"%");
		}
		sql = deleteLastAND(sql);
		holder.setSql(sql.toString());
		return holder;
	}
	/**
	 * 过滤字段
	 * 
	 * @param f
	 * @return
	 */
	private static boolean isTransient(Field f) {
		int m = f.getModifiers();
		if (Modifier.isStatic(m) || Modifier.isFinal(m)) {
			return true;
		}
		Transient t = f.getAnnotation(Transient.class);
		if (t != null && t.value() == true) {
			return true;
		}
		return false;
	}
	/**
	 * 确定唯一键
	 * @param field
	 * @return
	 */
	private static boolean isUnique(Field field) {
		Column c = field.getAnnotation(Column.class);
		if(c == null){
			return false;
		}
		return c.unique();
	}
	/**
	 * 过滤更新字段
	 * @param f
	 * @return
	 */
	private static boolean updatable(Field f){
		Column c = f.getAnnotation(Column.class);
		if(c == null){
			return false;
		}
		if(c.unique()){
			return false;
		}else{
			return c.updatable();
		}
	}
	/**
	 * 过滤持久化字段
	 * @param f
	 * @return
	 */
	private static boolean insertable(Field f) {
		Column c = f.getAnnotation(Column.class);
		if(c == null){
			return false;
		}
		return c.insertable();
	}
	/**
	 * 取表名
	 * 
	 * @param tableEntity
	 * @return
	 */
	private static String getTableName(Class<?> tableEntity) {
		String entityName = tableEntity.getSimpleName();
		Table table = tableEntity.getAnnotation(Table.class);
		if (table != null && StringUtils.isNotEmpty(table.name())) {
			return table.name().toUpperCase();
		}
		return entityName;
	}

	/**
	 * 取列名
	 * 
	 * @param f
	 * @return
	 */
	private static String getColumnName(Field f) {
		String propertyName = f.getName();
		Column column = f.getAnnotation(Column.class);
		if (column != null && StringUtils.isNotEmpty(column.name())) {
			return column.name().toUpperCase();
		}
		return propertyName;
	}
	/**
	 * 对象转换
	 * @param o
	 * @return
	 */
	private static Object convert(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Date) {
            Date date = (Date) o;
            Timestamp t = new Timestamp(date.getTime());
            return t;
        }
        return o;
    }
	/**
	 * 获取对象的属性
	 * @param obj
	 * @param f
	 */
	private static Object getValue(Object obj,Field f){
		Object resObj = null;
		try{
			if(f.isAccessible()){
				resObj = f.get(obj);
			}else {
				f.setAccessible(true);
				resObj = f.get(obj);
				f.setAccessible(false);
			}
		}catch(Exception e){
			throw new RuntimeException("获取属性值失败",e);
		}
		return resObj;
	}
	
	 /**
     * 删除最后那个“,”
     * 
     * @param sb
     */
    private static void deleteLastComma(StringBuilder sb) {
        if (sb.lastIndexOf(",") == sb.length() - 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
    }
    /**
     * 删除最后 “and”
     * @param sql
     * @return
     */
    private static StringBuilder deleteLastAND(StringBuilder sql) {
    	StringBuilder trimSql = new StringBuilder(sql.toString().trim());
		if(trimSql.lastIndexOf("and") == trimSql.length() - 3){
			trimSql.delete(trimSql.lastIndexOf("and")-1, trimSql.length());
		}
		return trimSql;
	}

}
