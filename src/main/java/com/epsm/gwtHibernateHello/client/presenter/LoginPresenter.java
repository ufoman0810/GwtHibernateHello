package com.epsm.gwtHibernateHello.client.presenter;

import java.util.Date;

import com.epsm.gwtHibernateHello.client.event.LoginEvent;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.ContentContainer;
import com.epsm.gwtHibernateHello.client.view.ErrorMessages;
import com.epsm.gwtHibernateHello.client.view.LoginView;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LoginPresenter extends Presenter{
	public LoginPresenter(LoginServiceAsync loginService, ErrorMessages messages,
			HandlerManager eventBus, LoginView view){
		
		super(loginService, messages, eventBus, view);
		logger.config("LoginPresenter created.");
	}
		
	@Override
	public void showPage(ContentContainer container) {
		logger.finest("Invoked: showPage(...).");
		view.resetState();
		container.setContent(view.asComposite());
	}	
	
	public void logIn(String login, String password){
		logger.finer("Invoked: logIn(" + login + ", ...)");
		
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
		String message = messages.tooShortLoginOrPassword(Constants.MINIMAL_LENGHT);
		view.displayError(message);
		logger.finer("Displayed: message: " + message + ".");
	}
	
	private void tryToLoginWithServer(String login, String password){		
		loginService.loginServer(login, password,  new loginWithServerRequest());
		logger.finer("Requested: loginServer(" + login + ", ...) to LoginServer.");
	}
	
	private class loginWithServerRequest implements AsyncCallback<UserDTO>{
		
		@Override
		public void onSuccess(UserDTO result) {
			logger.finer("Executed: loginServer(..) request to a LoginService. Got: " + result + ".");
			
			if(result.isLoggedIn()){
				refreshCookies(result);
				reportLoginSuccessful();
			}else{
				displayWrongLoginOrPasswordMessage();
			}
		}
		
		@Override
		public void onFailure(Throwable caught) {
			displayServerUnvaibleMessage();
			logger.finer("Failed: loginServer(..) request to a LoginService.");
		}
	}
	
	private void refreshCookies(UserDTO result){
		final long DURATION = 1000 * 60 * 30;
		Date expires = new Date(System.currentTimeMillis() + DURATION);
		String token = result.getToken();
		Cookies.setCookie(Constants.COOKIE_TOKEN, token, expires, null, "/", false);
	}
	
	private void reportLoginSuccessful(){
		eventBus.fireEvent(new LoginEvent());
		logger.finer("Executed: LoginEvent fired.");
	}
	
	private void displayWrongLoginOrPasswordMessage(){
		String message = messages.wrongLoginOrPassword();
		view.displayError(message);
		logger.finer("Displayed: message: " + message + ".");
	}
	
	private void displayServerUnvaibleMessage(){
		String message = messages.serverUnavaible();
		view.displayError(message);
		logger.finer("Displayed: message: " + message + ".");
	}
}
