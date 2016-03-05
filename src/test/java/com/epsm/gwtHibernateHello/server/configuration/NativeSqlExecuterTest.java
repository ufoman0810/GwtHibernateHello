package com.epsm.gwtHibernateHello.server.configuration;

import java.io.File;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

public class NativeSqlExecuterTest {
	private NativeSqlExecuter executer = NativeSqlExecuter.getInstance();
	private static IDatabaseTester databaseTester;
	private final String SCRIPT
			= "INSERT INTO user (id, login, password, name) "
			+ "VALUES (1, 'someUser', 'someHash', 'someName');";
	
	@BeforeClass
	public static void setUp() throws ClassNotFoundException{
		databaseTester = new JdbcDatabaseTester("org.h2.Driver", "jdbc:h2:mem:test_mem");
	}
	
	@Test
	public void testExecuteNativeSQLMethodChangesDB() throws Exception{
		executer.executeNativeSQL(SCRIPT);
		
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(
				"src/test/resources/expectedDbAfterScriptExecution.xml"));
		IDataSet actualDataSet = databaseTester.getConnection().createDataSet();
		
		Assertion.assertEquals(expectedDataSet, actualDataSet);
	}
}
