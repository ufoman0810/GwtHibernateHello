package com.epsm.gwtHibernateHello.client.presenter;

import java.util.logging.Logger;

import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.PageView;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PagePresenter {
	private LoginServiceAsync loginService;
	private PageView view;
	private String sessionId;
	private UserDTO user;
	private boolean serverNotAvaible;
	private static Logger logger = Logger.getLogger("PagePresenter");
	
	public PagePresenter(LoginServiceAsync loginService, PageView view){

		
		
		this.loginService = loginService;
		this.view = view;
	}

	public void acceptLoginAndPassword(String login, String password){
		
	}
	
	public void executeLogout(){
		
	}
	
	public void showPage(){
		displayPage();
		
		if(isSessionIdExistInCookie()){
			checkWithServerIsSessionIdStillLegal();
			
			if(serverNotAvaible){
				displayLoginFilling();
				displayServerNotAvaibleMessageOnLoginFilling();
				logger.warning("Error: server not avaible.");
			}else if(isSessionActive()){
				displayGreetUserFilling();
				logger.fine("Displayed: greet user filling.");
			}else{
				displayLoginFilling();
				logger.fine("Displayed:login filling.");
			}
		}else{
			displayLoginFilling();
			return;
		}
	}
	
	private void displayPage(){
		view.displayPage();
	}
	
	private boolean isSessionIdExistInCookie(){
		sessionId = Cookies.getCookie(Constants.COOKIE_SESSION_ID);
		logger.fine("Invoked: getSessionIdFromCookies() method, sessionId = '" + sessionId + "'.");
		
		return sessionId != null;
	}
	
	private void checkWithServerIsSessionIdStillLegal(){		
		loginService.isSessionIdStillLegal(sessionId, new AsyncCallback<UserDTO>() {
			
			@Override
			public void onSuccess(UserDTO result) {
				user = result;
				serverNotAvaible = false;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				serverNotAvaible = true;
			}
		});
	}
	
	private void displayLoginFilling(){
		eraseLoginFillingState();
		makeViewDisplayLoginFilling();
	}
	
	private void eraseLoginFillingState(){
		view.eraseLoginAndPassword();
		view.eraseLoginFillingErrorsFields();
	}
	
	private void makeViewDisplayLoginFilling(){
		view.displayLoginFilling();
	}
	
	private void displayServerNotAvaibleMessageOnLoginFilling(){
		view.displayLoginError(Constants.SERVER_NOT_AVAIBLE);
	}
	
	private boolean isSessionActive(){
		return user.isLoggedIn();
	}
	
	private void displayGreetUserFilling(){
		eraseGreetingFillingState();
		makeViewDisplayGreetingFilling();
	}
	
	private void eraseGreetingFillingState(){
		view.eraseGreetingFillingErrorField();
	}
	
	private void makeViewDisplayGreetingFilling(){
		String greeting = createGreeting();
		view.displayGreetingFilling(greeting);
	}
	
	private String createGreeting(){
		return user.getUserGreeting() + ", " + user.getUserName() + ".";
	}

		
	
	
	
}
