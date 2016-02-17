package com.epsm.gwtHibernteHello.client;

import com.epsm.gwtHibernteHello.client.page.Login;
import com.epsm.gwtHibernteHello.client.service.LoginService;
import com.epsm.gwtHibernteHello.client.service.LoginServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class GwtHello implements EntryPoint {
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	private String sessionId;
	
	@Override
	public void onModuleLoad() {
		getSessionIdFromCookies();
		
		if(sessionId == null && isSeesionStillNotExpiredOnServer()){
			displayGreetUserPage();
		}else {
			displayLoginPage();
		}
	}
	
	private void getSessionIdFromCookies(){
		sessionId = Cookies.getCookie("sessionId");
	}

	private boolean isSeesionStillNotExpiredOnServer(){
		return false;
	}
	
	private void displayGreetUserPage(){
		
	}
	
	private void displayLoginPage(){
		RootLayoutPanel.get().add(new Login());
	}
}
