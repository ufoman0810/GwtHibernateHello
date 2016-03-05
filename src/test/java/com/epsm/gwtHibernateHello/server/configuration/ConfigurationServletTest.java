package com.epsm.gwtHibernateHello.server.configuration;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NativeSqlExecuter.class)
public class ConfigurationServletTest {
	private ConfigurationServlet configuration;
	private NativeSqlExecuter executer;
	
	@Before
	public void setUp(){
		PowerMockito.mockStatic(NativeSqlExecuter.class);
		executer = mock(NativeSqlExecuter.class);
		configuration = new ConfigurationServlet();
		when(NativeSqlExecuter.getInstance()).thenReturn(executer);
	}
	
	@Test
	public void initMethodExecutesSqlScrip(){
		configuration.init();
		
		verify(executer).executeNativeSQL(anyString());
	}
}
