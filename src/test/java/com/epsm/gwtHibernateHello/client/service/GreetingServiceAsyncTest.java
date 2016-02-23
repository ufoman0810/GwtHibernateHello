package com.epsm.gwtHibernateHello.client.service;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class GreetingServiceAsyncTest extends GWTTestCase{
	private GreetingServiceAsync greetingService;
	private final int RPC_TIMEOUT = 10_000;
	private final Date TIMESOURCE = new Date();
	private final String TOKEN = "someToken";
	
	@Override
	public String getModuleName() {
		return "com.epsm.gwtHibernateHello.GwtHibernateHello";
	}

	public void testGetMessageMethod(){
		createAndConfigGreetingService();
		executeGetMessageForTime();
	}
	
	private void createAndConfigGreetingService(){
		greetingService = GWT.create(GreetingService.class);
		ServiceDefTarget target = (ServiceDefTarget) greetingService;
	    target.setServiceEntryPoint(GWT.getModuleBaseURL() + "GwtHibernateHello/greeting");
	    delayTestFinish(RPC_TIMEOUT);
	}
	
	private void executeGetMessageForTime(){
		greetingService.getGreeting(TIMESOURCE, TOKEN, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				assertNull(result);
				
				finishTest();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fail("Request failure: " + caught.getMessage());
			}
		});
	}
}
