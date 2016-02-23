package com.epsm.gwtHibernateHello.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtHibernateHelloTest extends GWTTestCase{
	
	@Override
	public String getModuleName() {
		return "com.epsm.gwtHibernateHello.GwtHibernateHello";
	}
	
	public void testModuleCompilesWithoutExceptions(){ 
		GWT.create(GwtHibernateHello.class);
	}
}
