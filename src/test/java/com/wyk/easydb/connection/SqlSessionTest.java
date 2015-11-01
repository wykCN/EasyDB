package com.wyk.easydb.connection;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.*;

import com.wyk.esaydb.session.SqlSession;
import com.wyk.esaydb.session.SqlSessionFacotry;
import com.wyk.esaydb.session.SqlSessionFactoryBuilder;

public class SqlSessionTest {

	private SqlSessionFacotry sqlSessionFactory;
	private SqlSession<Role> session;

	@Before
	public void init() {
		Properties p = new Properties();
		p.setProperty("jdbcUrl", "jdbc:mysql://localhost:3306/test");
		p.setProperty("driverClass", "com.mysql.jdbc.Driver");
		p.setProperty("user", "root");
		p.setProperty("password", "root");
		try {
			sqlSessionFactory = SqlSessionFactoryBuilder.builder(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetSqlSession() {
		try {

			session = sqlSessionFactory.openSession(Role.class);
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("id", "01");
			List<Role> find = session.find(conditions);
			System.out.println(find.size());
			Role role = find.get(0);
			System.out.println(role.getRolename());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindById() {
		session = sqlSessionFactory.openSession(Role.class);
		Role role = new Role();
		role.setId("01");
		role = session.find(role);
		System.out.println(role);
		Assert.assertNotNull(role);

		 System.out.println(role.getRolename());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testQueryLike() {
		session = sqlSessionFactory.openSession(Role.class);

		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("rolename", "i");
		List<Role> lists = session.findLike(conditions);
		Assert.assertEquals(false, lists.isEmpty());
		for (Role role : lists) {
			System.out.println(role.getRolename());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void update() {
		session = sqlSessionFactory.openSession(Role.class);
		
		Role role = new Role("01","",""); 
		role = session.find(role);
		role.setDes("update...");
		boolean updated = session.update(role);
		Assert.assertEquals(true, updated);
	}

	@After
	public void destory() {
		if (session != null) {
			try {
				session.closeResource();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			session = null;
		}
	}
}
