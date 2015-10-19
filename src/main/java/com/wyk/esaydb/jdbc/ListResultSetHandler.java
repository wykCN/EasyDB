package com.wyk.esaydb.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.lang3.StringUtils;

import com.wyk.esaydb.annotation.Column;
/**
 * 
 * 自定义实现映射对象模型策略
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月18日
 * @version 1.0
 */
public class ListResultSetHandler<T> extends AbstractListHandler<T> {

	private Class<T> clazz;
	@SuppressWarnings("unchecked")
	public ListResultSetHandler(Class<T> type) {
		super();
		this.clazz = type;
	}

	@Override
	protected T handleRow(ResultSet rs) throws SQLException {
		T res = null;
		try{
			res = clazz.newInstance();
			ResultSetMetaData rmd = rs.getMetaData();
			int cols = rmd.getColumnCount();
			while(rs.next()){
				for (int i = 0; i < cols; i++) {
					String columnName = rmd.getColumnLabel(i + 1);
					Object obj = rs.getObject(columnName);
					if(obj == null){
						obj = "";
					}
					Field field = getColumnMappingField(columnName);
					if(field == null){
						continue;
					}
					field.setAccessible(true);
					field.set(res, obj);
				}
			}
		}catch(Exception e){
			throw new RuntimeException("映射对象模型失败",e);
		}
		return res;
	}

	private Field getColumnMappingField(String columnName) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if(column!=null && StringUtils.equals(columnName, column.name())){
				return field;
			}
		}
		return null;
	}

}
