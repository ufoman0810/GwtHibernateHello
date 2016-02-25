package com.epsm.gwtHibernateHello.server.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

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
	private UserDao dao = new UserDaoImpl();
	private final String EXISTS_LOGIN_IN_TEST_DB = "nk";
	private final String NOT_EXISTS_LOGIN_IN_TEST_DB = "notExistsLogin";
	private final String EXISTS_LOGIN_IN_SCRIPT_FILE = "john";
	private final String TEST_DB_PATH = "src/test/resources/testDb.xml";
	private final String CONFIGURATION_SQL_SCRIPT_PATH = "configuration/script.sql";
	
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

	@Test
	public void testExecuteNativeSQLMethodExecutes() throws IOException{
		String script = prepareScript();
		
		dao.executeNativeSQL(script);
		
		User user = dao.findUserByLogin(EXISTS_LOGIN_IN_SCRIPT_FILE);
		Assert.assertEquals(EXISTS_LOGIN_IN_SCRIPT_FILE, user.getLogin());
	}
	
	private String prepareScript() throws IOException{
		byte[] encoded = Files.readAllBytes(Paths.get(CONFIGURATION_SQL_SCRIPT_PATH));
		
		return new String(encoded, Charset.forName("UTF-8"));
	}
}
