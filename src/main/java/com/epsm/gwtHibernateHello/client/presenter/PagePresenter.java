package com.epsm.gwtHibernateHello.client.presenter;

import java.util.Date;
import java.util.logging.Logger;

import com.epsm.gwtHibernateHello.client.service.GreetingServiceAsync;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.PageView;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PagePresenter {
	private LoginServiceAsync loginService;
	private GreetingServiceAsync greetingService;
	private PageView view;
	private String sessionId;
	private UserDTO user;
	private String greeting;
	private boolean loginServerNotAvaible;
	private boolean greetingServerNotAvaible;
	private static Logger logger = Logger.getLogger("PagePresenter");
	
	public PagePresenter(LoginServiceAsync loginService, GreetingServiceAsync greetingService,
			PageView view){
		
		if(loginService == null){
			throw new IllegalArgumentException("Constructor: loginService can't be null.");
		}else if(greetingService == null){
			throw new IllegalArgumentException("Constructor: greetingService can't be null.");
		}else if(view == null){
			throw new IllegalArgumentException("Constructor: view can't be null.");
		}
		
		this.loginService = loginService;
		this.greetingService = greetingService;
		this.view = view;
	}
	
	public void showPage(){
		displayPage();
		
		if(isSessionIdExistInCookie()){
			checkWithServerIsSessionIdStillLegal();
			
			if(loginServerNotAvaible){
				displayLoginFillingWithServerNotAvaibleMessage();
				logger.warning("Error: server not avaible.");
			}else if(isUserLoggedIn()){
				getGreetingFromServer();
				
				if(greetingServerNotAvaible){
					displayLoginFillingWithServerNotAvaibleMessage();
				}else{
					displayGreetUserFilling();
				}
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
		loginService.isSessionIdStillLegal(new AsyncCallback<UserDTO>() {
			
			@Override
			public void onSuccess(UserDTO result) {
				user = result;
				loginServerNotAvaible = false;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				loginServerNotAvaible = true;
			}
		});
	}
	
	private void displayLoginFillingWithServerNotAvaibleMessage(){
		displayLoginFilling();
		displayServerNotAvaibleMessageOnLoginFilling();
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
	
	private boolean isUserLoggedIn(){
		return user.isLoggedIn();
	}
	
	private void getGreetingFromServer(){
		Date timeSource = getCurrentDate();
		
		greetingService.getGreetingForTime(timeSource, user.getSessionId(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				greeting = result;
				greetingServerNotAvaible = false;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				greetingServerNotAvaible = true;
			}
		});
	}
	
	private Date getCurrentDate(){
		return new Date();
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
		String userName = user.getUserName();
		
		return greeting + ", " + userName + ".";
	}
	
	public void logIn(String login, String password){
		loginWithServer(login, password);
		
		if(loginServerNotAvaible){
			displayServerNotAvaibleMessageOnLoginFilling();
			logger.warning("Error: server not avaible.");
		}else if(isUserLoggedIn()){
			getGreetingFromServer();
			
			if(greetingServerNotAvaible){
				displayServerNotAvaibleMessageOnLoginFilling();
			}else{
				displayGreetUserFilling();
			}
		}else{
			displayWrongLoginOrPasswordMessage();
			logger.fine("Displayed:login filling.");
		}
	}	
	
	private void loginWithServer(String login, String password){		
		loginService.loginServer(login, password,  new AsyncCallback<UserDTO>() {
			
			@Override
			public void onSuccess(UserDTO result) {
				user = result;
				loginServerNotAvaible = false;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				loginServerNotAvaible = true;
			}
		});
	}
	
	private void displayWrongLoginOrPasswordMessage(){
		view.displayLoginError(Constants.INCORRECT_CREDENTIALS);
	}
	
	public void executeLogout(){
		logoutWithServer();
		
		if(loginServerNotAvaible){
			displayServerNotAvaibleMessageOnGreetingFilling();
		}else{
			displayLoginFilling();
		}
	}
	
	private void logoutWithServer(){		
		loginService.logout(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				loginServerNotAvaible = false;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				loginServerNotAvaible = true;
			}
		});
	}
	
	private void displayServerNotAvaibleMessageOnGreetingFilling(){
		view.displayGreetingFilling(Constants.SERVER_NOT_AVAIBLE);
	}
}
