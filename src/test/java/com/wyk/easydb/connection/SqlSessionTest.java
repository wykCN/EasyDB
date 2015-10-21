package com.wyk.easydb.connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.wyk.esaydb.session.SqlSession;
import com.wyk.esaydb.session.SqlSessionFacotry;
import com.wyk.esaydb.session.SqlSessionFactoryBuilder;

public class SqlSessionTest {

	@Test
	public void testGetSqlSession(){
		Properties p = new Properties();
		p.setProperty("jdbcUrl", "jdbc:mysql://localhost:3306/test");
		p.setProperty("driverClass", "com.mysql.jdbc.Driver");
		p.setProperty("user", "root");
		p.setProperty("password", "root");
		
		try {
			SqlSessionFacotry sqlSessionFactory = SqlSessionFactoryBuilder.builder(p);
			@SuppressWarnings("rawtypes")
			SqlSession openSession = sqlSessionFactory.openSession(Role.class);
			Map<String,Object> conditions = new HashMap<String,Object>();
			conditions.put("id", "01");
			@SuppressWarnings({ "unchecked"})
			List<Role> find = openSession.find(conditions);
			System.out.println(find.size());
			Role role = find.get(0);
			System.out.println(role.getRolename());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
