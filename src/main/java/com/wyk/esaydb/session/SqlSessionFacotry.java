package com.wyk.esaydb.session;
/**
 * 
 * 工场模式创建SqlSession对象
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月18日
 * @version 1.0
 */
public class SqlSessionFacotry {

	/**
	 * 默认(自动)事务提交方式获取sqlsession
	 * @return
	 */
	public static SqlSession openSession(){
		return null;
	}
	/**
	 * 是否打开事务
	 * @param openTransiaction
	 * @return
	 */
	public static SqlSession openSession(boolean openTransiaction){
		return null;
	}
}
