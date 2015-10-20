package com.wyk.esaydb.pool;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月20日
 * @version 1.0
 */
public class DataSourceFactory implements ObjectFactory {
	// 当连接池中的连接用完时，C3P0一次性创建新连接的数目；
	public final static String PROP_ACQUIREINCREMENT = "acquireIncrement";
	// 定义在从数据库获取新连接失败后重复尝试获取的次数，默认为
	public final static String PROP_ACQUIRERETRYATTEMPTS = "acquireRetryAttempts";
	// 两次连接中间隔时间，单位毫秒，默认为1000；
	public final static String PROP_ACQUIRERETRYDELAY = "acquireRetryDelay";
	// 连接关闭时默认将所有未提交的操作回滚。默认为 false
	public final static String PROP_AUTOCOMMITONCLOSE = "autoCommitOnClose";
	// 获取连接失败将会引起所有等待获取连接的线程抛出异常。但是数据源仍有效保留，并在下次调
	// 用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试获取连接失败后该数据源将申明已断开并永久关闭。默认为
	// false；
	public final static String PROP_BREAKAFTERACQUIREFAILURE = "breakAfterAcquireFailure";
	// 当连接池用完时客户端调用getConnection()后等待获取新连接的时间，超时后将抛出SQLException，如设为0则无限期等待。单位毫秒，默认为0；
	public final static String PROP_CHECKOUTTIMEOUT = "checkoutTimeout";
	// 隔多少秒检查所有连接池中的空闲连接，默认为0表示不检查
	public final static String PROP_IDLECONNECTIONTESTPERIOD = "idleConnectionTestPeriod";
	// 初始化时创建的连接数默认为3
	public final static String PROP_INITIALPOOLSIZE = "initialPoolSize";
	// 最大空闲时间，超过空闲时间的连接将被丢弃。为0或负数则永不丢弃。默认为0
	public final static String PROP_MAXIDLETIME = "maxIdleTime";
	// 连接池中保留的最大连接数。默认为15
	public final static String PROP_MAXPOOLSIZE = "maxPoolSize";

	private static final String[] ALL_PROPERTIES = { PROP_ACQUIREINCREMENT,
			PROP_ACQUIRERETRYATTEMPTS, PROP_ACQUIRERETRYDELAY,
			PROP_AUTOCOMMITONCLOSE, PROP_BREAKAFTERACQUIREFAILURE,
			PROP_CHECKOUTTIMEOUT, PROP_CHECKOUTTIMEOUT,
			PROP_IDLECONNECTIONTESTPERIOD,
			PROP_INITIALPOOLSIZE, PROP_MAXIDLETIME, PROP_MAXPOOLSIZE };

	public Object getObjectInstance(Object obj, Name name, Context nameCtx,
			Hashtable<?, ?> environment) throws Exception {

		// We only know how to deal with <code>javax.naming.Reference</code>s
		// that specify a class name of "javax.sql.DataSource"
		if ((obj == null) || !(obj instanceof Reference)) {
			return null;
		}
		Reference ref = (Reference) obj;

		if ((!"javax.sql.DataSource".equals(ref.getClassName())) //
				&& (!"com.alibaba.druid.pool.DruidDataSource".equals(ref
						.getClassName())) //
		) {
			return null;
		}

		Properties properties = new Properties();
		for (int i = 0; i < ALL_PROPERTIES.length; i++) {
			String propertyName = ALL_PROPERTIES[i];
			RefAddr ra = ref.get(propertyName);
			if (ra != null) {
				String propertyValue = ra.getContent().toString();
				properties.setProperty(propertyName, propertyValue);
			}
		}

		return createDataSourceInternal(properties);
	}

	protected ComboPooledDataSource createDataSourceInternal(
			Properties properties) throws Exception {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		config(dataSource, properties);
		return dataSource;
	}
	@SuppressWarnings("rawtypes")
	public static ComboPooledDataSource createDataSource(Properties protperties){
		return createDataSource((Map) protperties);
	}
	@SuppressWarnings("rawtypes")
	public static ComboPooledDataSource createDataSource(Map map){
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		config(dataSource, map);
		return dataSource;
	}
	
	@SuppressWarnings("rawtypes" )
	public static void config(ComboPooledDataSource dataSource, Map properties) {
		String value = null;

		value = (String) properties.get(PROP_ACQUIREINCREMENT);
		if (value != null) {
			dataSource.setAcquireIncrement(Integer.parseInt(value));
		}

		value = (String) properties.get(PROP_ACQUIRERETRYATTEMPTS);
		if (value != null) {
			dataSource.setAcquireRetryAttempts(Integer.parseInt(value));
		}

		value = (String) properties.get(PROP_ACQUIRERETRYDELAY);
		if (value != null) {
			dataSource.setAcquireRetryDelay(Integer.parseInt(value));
		}

		value = (String) properties.get(PROP_AUTOCOMMITONCLOSE);
		if (value != null) {
			dataSource.setAutoCommitOnClose(Boolean.parseBoolean(value));
		}

		value = (String) properties.get(PROP_BREAKAFTERACQUIREFAILURE);
		if (value != null) {
			dataSource.setBreakAfterAcquireFailure(Boolean.parseBoolean(value));
		}

		value = (String) properties.get(PROP_CHECKOUTTIMEOUT);
		if (value != null) {
			dataSource.setCheckoutTimeout(Integer.parseInt(value));
		}

		value = (String) properties.get(PROP_IDLECONNECTIONTESTPERIOD);
		if (value != null) {
			dataSource.setIdleConnectionTestPeriod(Integer.parseInt(value));
		}

		value = (String) properties.get(PROP_INITIALPOOLSIZE);
		if (value != null) {
			dataSource.setInitialPoolSize(Integer.parseInt(value));
		}

		value = (String) properties.get(PROP_MAXIDLETIME);
		if (value != null) {
			dataSource.setMaxIdleTime(Integer.parseInt(value));
		}

		value = (String) properties.get(PROP_MAXPOOLSIZE);
		if (value != null) {
			dataSource.setMaxPoolSize(Integer.parseInt(value));
		}
	}

}
