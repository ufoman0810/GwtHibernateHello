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
	private String userName;
	private String token;
	private String userGreeting;
	private static Logger logger = Logger.getLogger("PagePresenter");
	
	public PagePresenter(LoginServiceAsync loginService, GreetingServiceAsync greetingService,
			PageView view){
		
		if(loginService == null){
			String message = "Constructor: loginService can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}else if(greetingService == null){
			String message = "Constructor: greetingService can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}else if(view == null){
			String message = "Constructor: view can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}
		
		this.loginService = loginService;
		this.greetingService = greetingService;
		this.view = view;
		logger.config("PagePresenter created.");
	}
	
	public void showPage(){
		logger.fine("Invoked: showPage() method.");
		displayPage();
		
		if(isTokenExistInCookies()){
			checkWithServerIsTokenStillLegal();
		}else{
			displayLoginFilling();
		}
	}
	
	private void displayPage(){
		view.displayPage();
	}
	
	private boolean isTokenExistInCookies(){
		boolean exist = Cookies.getCookie(Constants.COOKIE_TOKEN_NAME) != null;
		logger.finer("Invoked: isTokenExistInCookies(), returned '" + exist + "'." );
		
		return exist;
	}
	
	private void checkWithServerIsTokenStillLegal(){		
		loginService.isTokenStillLegal(token, new TokenLegalityRequest());
	}
	
	private class TokenLegalityRequest implements AsyncCallback<UserDTO>{

		@Override
		public void onSuccess(UserDTO result) {
			logger.finer("Invoked: loginserver.isTokenStillLegal(), returned '" + result + "'." );
			if(isUserLoggedIn(result)){
				saveUserData(result);
				tryToGetGreetingFromGreetingServer();
			}else{
				displayLoginFilling();
			}
		}
		
		@Override
		public void onFailure(Throwable caught) {
			logger.warning("Invoked: loginserver.isTokenStillLegal(), server unavaible.");
			displayLoginFillingWithServerNotAvaibleMessage();
		}
	}
	
	private boolean isUserLoggedIn(UserDTO user){
		return user.isLoggedIn();
	}
	
	private void saveUserData(UserDTO user){
		token = user.getToken();
		userName = user.getUserName();
	}
	
	private void tryToGetGreetingFromGreetingServer(){
		Date timeSource = getCurrentDate();
		greetingService.getGreetingForTime(timeSource, token, new GetGreetingRequest());
	}
	
	private Date getCurrentDate(){
		return new Date();
	}
	
	private class GetGreetingRequest implements AsyncCallback<String>{
		
		@Override
		public void onSuccess(String result) {
			logger.finer("Invoked: greetingService.getGreetingForTime(...), returned '" + result + "'.");
			createGreeting(result);
			displayGreetUserFilling();
		}
		
		@Override
		public void onFailure(Throwable caught) {
			logger.warning("Invoked: greetingService.getGreetingForTime(...), server unavaible.");
			displayLoginFillingWithServerNotAvaibleMessage();
		}
	}
	
	private void createGreeting(String greeting){
		userGreeting = greeting + ", " + userName + ".";
		logger.finest("Invoked: createGreeting(), created '" + userGreeting + "'.");
	}
	
	private void displayGreetUserFilling(){
		hideLoginFilling();
		eraseGreetingFillingState();
		makeViewDisplayGreetingFilling();
		logger.fine("Displayed: greeting filling.");
	}
	
	private void hideLoginFilling(){
		view.hideLoginFilling();
	}
	
	private void eraseGreetingFillingState(){
		view.eraseGreetingFillingErrorField();
	}
	
	private void makeViewDisplayGreetingFilling(){
		view.displayGreetingFilling(userGreeting);
	}
	
	private void displayLoginFillingWithServerNotAvaibleMessage(){
		displayLoginFilling();
		displayServerNotAvaibleMessageOnLoginFilling();
	}
	
	private void displayLoginFilling(){
		hideGreetingFilling();
		eraseLoginFillingState();
		makeViewDisplayLoginFilling();
		logger.fine("Displayed: login filling.");
	}
	
	private void hideGreetingFilling(){
		view.hideGreetingFilling();
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
		logger.finer("Displayed: message on login filling '" + Constants.SERVER_NOT_AVAIBLE + "'.");
	}
		
	public void logIn(String login, String password){
		if(areLoginOrPasswordTooShort(login, password)){
			showLoginOrPasswordTooShortMessage();
		}else{
			tryToLoginWithServer(login, password);
		}
	}
	
	private boolean areLoginOrPasswordTooShort(String login, String password){
		return login.length() < Constants.MINIMAL_LENGHT 
				|| password.length() < Constants.MINIMAL_LENGHT;
	}
	
	private void showLoginOrPasswordTooShortMessage(){
		view.displayLoginError(Constants.LOGIN_OR_PASSWORD_TOO_SHORT);
		logger.finer("Displayed: message on login filling '" 
				+ Constants.LOGIN_OR_PASSWORD_TOO_SHORT + "'.");
	}
	
	private void tryToLoginWithServer(String login, String password){		
		loginService.loginServer(login, password,  new loginWithServerRequest());
	}
	
	private class loginWithServerRequest implements AsyncCallback<UserDTO>{
		
		@Override
		public void onSuccess(UserDTO result) {
			logger.finer("Invoked: loginService.loginServer(...), returned '" + result + "'." );
			if(isUserLoggedIn(result)){
				saveUserData(result);
				tryToGetGreetingFromGreetingServer();
				refreshCookies();
			}else{
				displayWrongLoginOrPasswordMessage();
			}
		}
		
		private void refreshCookies(){
			final long DURATION = 1000 * 60 * 30;
			Date expires = new Date(System.currentTimeMillis() + DURATION);
			Cookies.setCookie(Constants.COOKIE_TOKEN_NAME, token, expires);
		}
		
		@Override
		public void onFailure(Throwable caught) {
			displayServerNotAvaibleMessageOnLoginFilling();
			logger.warning("Invoked: loginService.loginServer(...), server unavaible.");
		}
	}
	
	private void displayWrongLoginOrPasswordMessage(){
		view.displayLoginError(Constants.INCORRECT_CREDENTIALS);
		logger.finer("Displayed: message on login filling '" + Constants.INCORRECT_CREDENTIALS+ "'.");
	}
	
	public void executeLogout(){		
		loginService.logout(token, new LogoutRequest());
	}
	
	private class LogoutRequest implements AsyncCallback<Void>{

		@Override
		public void onSuccess(Void result) {
			logger.warning("Invoked: loginService.logout(...), request executed.");
			displayLoginFilling();
		}

		@Override
		public void onFailure(Throwable caught) {
			logger.warning("Invoked: loginService.logout(...), server unavaible.");
			displayServerNotAvaibleMessageOnGreetingFilling();
		}
	}
	
	private void displayServerNotAvaibleMessageOnGreetingFilling(){
		view.displayGreetingFilling(Constants.SERVER_NOT_AVAIBLE);
		logger.finer("Displayed: message on greeting filling '" + Constants.SERVER_NOT_AVAIBLE + "'.");
	}
}
