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

public class ConfigurationTest {
	private static IDatabaseTester databaseTester;
	
	@BeforeClass
	public static void setUp() throws ClassNotFoundException{
		databaseTester = new JdbcDatabaseTester("org.h2.Driver", "jdbc:h2:mem:test_mem");
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
		UserDao userDao = Configuration.getUsedDao();
		
		Assert.assertNotNull(userDao);
	}
	
	@Test
	public void getUsedDaoMethodReturnsTheSameInstanceEachTime(){
		UserDao userDao_1 = Configuration.getUsedDao();
		UserDao userDao_2 = Configuration.getUsedDao();
		
		Assert.assertTrue(userDao_1 == userDao_2);
	}
	
	@Test
	public void getMesageFactoryMethodReturnsNotNullMessageFactory(){
		MessageFactory messageFactory = Configuration.getMesageFactory();
		
		Assert.assertNotNull(messageFactory);
	}
	
	@Test
	public void getMesageFactoryMethodReturnsTheSameInstanceEachTime(){
		MessageFactory messageFactory_1 = Configuration.getMesageFactory();
		MessageFactory messageFactory_2 = Configuration.getMesageFactory();
		
		Assert.assertTrue(messageFactory_1 == messageFactory_2);
	}
}
