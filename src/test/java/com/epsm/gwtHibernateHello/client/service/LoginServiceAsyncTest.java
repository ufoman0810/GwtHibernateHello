package com.epsm.gwtHibernateHello.client.service;

import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class LoginServiceAsyncTest extends GWTTestCase {
	private LoginServiceAsync loginService;
	private boolean firstTime = true;
	private final int RPC_TIMEOUT = 10_000; 
	private final String TOKEN = "someToken";
	private final String LOGIN = "someLogin";
	private final String PASSWORD = "somePassword";
	
	@Override
	public String getModuleName() {
		return "com.epsm.gwtHibernateHello.GwtHibernateHello";
	}

	@Override
	public void gwtSetUp(){
		if(firstTime){
			createAndConfigLoginService();
			firstTime = false;
		}
	}
	
	private void createAndConfigLoginService(){
		loginService = GWT.create(LoginService.class);
		ServiceDefTarget target = (ServiceDefTarget) loginService;
	    target.setServiceEntryPoint(GWT.getModuleBaseURL() + "GwtHibernateHello/login");
	    delayTestFinish(RPC_TIMEOUT);
	}
	
	public void testIsSessionStillLegalMethod(){
		loginService.isSessionStillLegal(TOKEN, new AsyncCallback<UserDTO>() {
			
			@Override
			public void onSuccess(UserDTO result) {
				assertNotNull(result);
				assertFalse(result.isLoggedIn());
				
				finishTest();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fail("Request failure: " + caught.getMessage());
			}
		});
	}
	
	public void testLoginServerMethod(){
		loginService.loginServer(LOGIN, PASSWORD, new AsyncCallback<UserDTO>() {
			
			@Override
			public void onSuccess(UserDTO result) {
				assertNotNull(result);
				assertFalse(result.isLoggedIn());
				
				finishTest();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fail("Request failure: " + caught.getMessage());
			}
		});
	}
	
	public void testLogoutMethod(){
		loginService.logout(TOKEN, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				finishTest();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				fail("Request failure: " + caught.getMessage());
			}
		});
	}
}
