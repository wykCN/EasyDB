package com.wyk.esaydb.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


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
		// extends transient
		return false;
	}
}
