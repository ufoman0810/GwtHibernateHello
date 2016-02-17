package com.epsm.gwtHibernteHello.client;

import java.util.logging.Logger;

import com.epsm.gwtHibernteHello.client.page.LoginPage;
import com.epsm.gwtHibernteHello.client.service.LoginService;
import com.epsm.gwtHibernteHello.client.service.LoginServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class GwtHello implements EntryPoint {
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	private String sessionId;
	private static Logger logger = Logger.getLogger("GwtHello");
	
	@Override
	public void onModuleLoad() {
		logger.info("Requested: GwtHello page.");
		getSessionIdFromCookies();
		
		if(sessionId == null && isSeesionStillNotExpiredOnServer()){
			displayGreetUserPage();
			logger.fine("Displayed: GreetUserPage.");
		}else {
			displayLoginPage();
			logger.fine("Displayed: LoginPage.");
		}
	}
	
	private void getSessionIdFromCookies(){
		sessionId = Cookies.getCookie("sessionId");
		logger.fine("Invoked: getSessionIdFromCookies() method, sessionId = '" + sessionId + "'.");
	}

	private boolean isSeesionStillNotExpiredOnServer(){
		boolean notExpired = false;
		logger.fine("Invoked: isSeesionStillNotExpiredOnServer() method, returned '" + notExpired + "'.");
	
		return notExpired;
	}
	
	private void displayGreetUserPage(){
		
	}
	
	private void displayLoginPage(){
		RootLayoutPanel.get().add(new LoginPage());
	}
}
