package com.epsm.gwtHibernteHello.client;

import java.util.logging.Logger;

import com.epsm.gwtHibernteHello.client.page.Page;
import com.epsm.gwtHibernteHello.client.service.LoginService;
import com.epsm.gwtHibernteHello.client.service.LoginServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class GwtHibernateHello implements EntryPoint {
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	private String sessionId;
	private Page page = new Page();
	private static Logger logger = Logger.getLogger("GwtHibernateHello");
	
	@Override
	public void onModuleLoad() {
		logger.info("Requested: GwtHibernateHello module.");
		
		if(isSessionActive()){
			displayGreetUserFilling();
			logger.fine("Displayed: greet user filling.");
		}else{
			displayLoginFilling();
			logger.fine("Displayed:login filling.");
		}
	}
	
	private boolean isSessionActive(){
		getSessionIdFromCookies();
		
		if(sessionId == null){
			return false;
		}
		if(isSessionExpiredOnServer()){
			return false;
		}
		
		return true;
	}
	
	private void getSessionIdFromCookies(){
		sessionId = Cookies.getCookie("sessionId");
		logger.fine("Invoked: getSessionIdFromCookies() method, sessionId = '" + sessionId + "'.");
	}

	private boolean isSessionExpiredOnServer(){
		boolean expired = true;
		logger.fine("Invoked: isSeesionStillNotExpiredOnServer() method, returned '" + expired + "'.");
	
		return expired;
	}
	
	private void displayGreetUserFilling(){
		displayPage();
		fillPageAsGreetUser();
	}
	
	private void displayPage(){
		RootLayoutPanel.get().add(page);
	}
	
	private void fillPageAsGreetUser(){
		page.displayGreetUserFilling();
	}
	
	private void displayLoginFilling(){
		displayPage();
		fillPageAsLogin();
	}

	private void fillPageAsLogin(){
		page.displayLoginFilling();
	}
}
