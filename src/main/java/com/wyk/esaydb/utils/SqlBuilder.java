package com.wyk.esaydb.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.StringUtils;

import com.wyk.esaydb.annotation.Column;
import com.wyk.esaydb.annotation.Table;
import com.wyk.esaydb.annotation.Transient;


/**
 * 
 * ** to builder sql by this class
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月14日
 * @version 1.0
 */
public class SqlBuilder {
//	public SQLHolder buildInsert(IEntity )
	
	/**
	 * 过滤字段
	 * @param f 
	 * @return
	 */
	private static boolean isTransient(Field f){
		int m = f.getModifiers();
		if(Modifier.isStatic(m) || Modifier.isFinal(m)){
			return true;
		}
		Transient t = f.getAnnotation(Transient.class);
		if(t != null && t.value() == true){
			return true;
		}
		return false;
	}
	
	/**
	 * 取表名
	 * @param tableEntity
	 * @return
	 */
	private static String getTableName(Class<?> tableEntity){
		String entityName = tableEntity.getSimpleName();
		Table table = tableEntity.getAnnotation(Table.class);
		if(table != null && StringUtils.isNotEmpty(table.name())){
			return table.name();
		}
		return entityName;
	}
	
	/**
	 * 取列名
	 * @param f
	 * @return
	 */
	private static String getColumnName(Field f){
		String propertyName = f.getName();
		Column column = f.getAnnotation(Column.class);
		if(column != null && StringUtils.isNotEmpty(column.name())){
			return column.name();
		}
		return propertyName;
	}
}
