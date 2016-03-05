package com.epsm.gwtHibernateHello.server.dao;

import java.io.FileInputStream;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.epsm.gwtHibernateHello.server.domain.User;
import com.epsm.gwtHibernateHello.server.repository.UserDao;
import com.epsm.gwtHibernateHello.server.repository.UserDaoImpl;

public class UserDaoImplTest extends DBTestCase{
	private UserDao dao = UserDaoImpl.getInstatnce();
	private final String EXISTS_LOGIN_IN_TEST_DB = "nk";
	private final String NOT_EXISTS_LOGIN_IN_TEST_DB = "notExistsLogin";
	private final String TEST_DB_PATH = "src/test/resources/testDb.xml";
	
	public UserDaoImplTest(){
        System.setProperty(PropertiesBasedJdbcDatabaseTester
        		.DBUNIT_DRIVER_CLASS, "org.h2.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester
        		.DBUNIT_CONNECTION_URL, "jdbc:h2:mem:test_mem");
    }
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream(TEST_DB_PATH));
	}
	
	@Test
	public void testFindUserByLoginMethodReturnsNullifItUserNotExists(){
		User user = dao.findUserByLogin(NOT_EXISTS_LOGIN_IN_TEST_DB);
		
		Assert.assertNull(user);
	}
	
	@Test
	public void testFindUserByLoginMethodReturnsUserIfItExists(){
		User user = dao.findUserByLogin(EXISTS_LOGIN_IN_TEST_DB);
		
		Assert.assertEquals(EXISTS_LOGIN_IN_TEST_DB, user.getLogin());
	}
}
