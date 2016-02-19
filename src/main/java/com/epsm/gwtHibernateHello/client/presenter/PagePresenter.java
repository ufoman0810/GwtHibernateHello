package com.epsm.gwtHibernateHello.client.presenter;

import java.util.logging.Logger;

import com.epsm.gwtHibernateHello.client.service.GreetingServiceAsync;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.PageView;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PagePresenter {
	private LoginServiceAsync loginService;
	private GreetingServiceAsync greetingService;
	private PageView view;
	private String sessionId;
	private UserDTO user;
	private boolean expired;
	private static Logger logger = Logger.getLogger("PagePresenter");
	
	public PagePresenter(LoginServiceAsync loginService, GreetingServiceAsync greetingService,
			PageView view){
		
		this.loginService = loginService;
		this.greetingService = greetingService;
		this.view = view;
	}

	public void acceptLoginAndPassword(String login, String password){
		
	}
	
	public void executeLogout(){
		
	}
	
	public void showPage(){
		displayPage();
		
		if(isSessionActive()){
			//displayGreetUserFilling();
			logger.fine("Displayed: greet user filling.");
		}else{
			displayLoginFilling();
			logger.fine("Displayed:login filling.");
		}
	}
	
	private void displayPage(){
		view.displayPage();
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
		loginService.loginFromSessionServer(new AsyncCallback<UserDTO>() {
			
			@Override
			public void onSuccess(UserDTO result) {
				if(result.isLoggedIn()){
					user = result;
					expired = false;
				}else{
					expired = true;
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	
		return true;
	}
	
	private void displayLoginFilling(){
		eraseLoginViewState();
		makeViewDisplayLoginFilling();
	}
	
	private void eraseLoginViewState(){
		view.eraseLoginAndPassword();
		view.eraseErrorsFields();
	}
	
	private void makeViewDisplayLoginFilling(){
		view.displayLoginFilling();
	}
}
