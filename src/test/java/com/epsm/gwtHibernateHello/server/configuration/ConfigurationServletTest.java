package com.epsm.gwtHibernateHello.server.configuration;

import java.io.File;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epsm.gwtHibernateHello.server.repository.UserDao;
import com.epsm.hello.model.MessageFactory;

public class ConfigurationServletTest {
	private static ConfigurationServlet configuration;
	private static IDatabaseTester databaseTester;
	
	@BeforeClass
	public static void setUp() throws ClassNotFoundException{
		configuration = new ConfigurationServlet();
		databaseTester = new JdbcDatabaseTester("org.h2.Driver", "jdbc:h2:mem:test_mem");
		
		configuration.init();
	}
	
	@Test
	public void databaseKeepsNecessaryDataAfterApplicationInitialization() throws Exception{
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(
				"src/test/resources/expectedDbAfterAppInit.xml"));
		IDataSet actualDataSet = databaseTester.getConnection().createDataSet();
		
		Assertion.assertEquals(expectedDataSet, actualDataSet);
	}
	
	@Test
	public void getUsedDaoMethodReturnsNotNullUserDao(){
		UserDao userDao = ConfigurationServlet.getUsedDao();
		
		Assert.assertNotNull(userDao);
	}
	
	@Test
	public void getMesageFactoryMethodReturnsNotNullMessageFactory(){
		MessageFactory messageFactory = ConfigurationServlet.getMesageFactory();
		
		Assert.assertNotNull(messageFactory);
	}
}
