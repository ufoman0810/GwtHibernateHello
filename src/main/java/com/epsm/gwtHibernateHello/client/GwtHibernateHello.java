package com.epsm.gwtHibernateHello.client;

import java.util.logging.Logger;

import com.epsm.gwtHibernateHello.client.service.LoginService;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.Page;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
		callRemote();
		getSessionIdFromCookies();
		
		if(sessionId == null){
			return false;
		}
		if(isSessionExpiredOnServer()){
			return false;
		}
		
		return true;
	}
	
	private void callRemote(){
		loginService.loginServer("", "", new AsyncCallback<UserDTO>() {
			
			@Override
			public void onSuccess(UserDTO result) {
				logger.severe("locale: " + result.getUserGreeting());
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				logger.severe("failed getting locale.");
				
			}
		});
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
