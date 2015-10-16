package com.wyk.esaydb.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.wyk.esaydb.annotation.Column;
import com.wyk.esaydb.annotation.Table;
import com.wyk.esaydb.annotation.Transient;
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
	public SqlHolder buildInsert(IEntity entity) {
		//parse element
		SqlHolder holder = new SqlHolder();
		Field[] fields = entity.getClass().getDeclaredFields();

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		for (Field f : fields) {
			if(isTransient(f)){
				continue;
			}
			holder.addParam(convert(getValue(entity,f)));
			columns.append(getColumnName(f)).append(",");
			values.append("?").append(",");
		}
		deleteLastComma(columns);
		deleteLastComma(values);
		
		//builder sql
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO").append(getTableName(entity.getClass())).append("(");
		sql.append(columns).append("");
		sql.append(" VALUES(").append(values).append(")");
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
	 * 取表名
	 * 
	 * @param tableEntity
	 * @return
	 */
	private static String getTableName(Class<?> tableEntity) {
		String entityName = tableEntity.getSimpleName();
		Table table = tableEntity.getAnnotation(Table.class);
		if (table != null && StringUtils.isNotEmpty(table.name())) {
			return table.name();
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
			return column.name();
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

}
