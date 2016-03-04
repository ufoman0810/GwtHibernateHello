package com.epsm.gwtHibernateHello.client;

import java.util.logging.Logger;

import com.epsm.gwtHibernateHello.client.event.LoginEvent;
import com.epsm.gwtHibernateHello.client.event.LoginEventHandler;
import com.epsm.gwtHibernateHello.client.event.LogoutEvent;
import com.epsm.gwtHibernateHello.client.event.LogoutEventHandler;
import com.epsm.gwtHibernateHello.client.presenter.GreetingPresenter;
import com.epsm.gwtHibernateHello.client.presenter.LoginPresenter;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.ContentContainer;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AppController {
	private LoginServiceAsync loginService;
	private LoginPresenter loginPresenter;
	private GreetingPresenter greetingPresenter;
	private HandlerManager eventBus;
	private ContentContainer container;
	private Logger logger;
	
	public AppController(LoginServiceAsync loginService, HandlerManager eventBus,
			LoginPresenter loginPresenter, GreetingPresenter greetingPresenter) {
		
		logger = Logger.getLogger("AppController");
		
		if(loginService == null){
			String message = "Constructor: LoginServiceAsync can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}else if(eventBus == null){
			String message = "Constructor: HandlerManager can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}else if(loginPresenter == null){
			String message = "Constructor: LoginPresenter can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}else if(greetingPresenter == null){
			String message = "Constructor: GreetingPresenter can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}
		
		this.loginService = loginService;
		this.eventBus = eventBus;
		this.loginPresenter = loginPresenter;
		this.greetingPresenter = greetingPresenter;
		container = ContentContainer.getInstance();
		bindEventBus();
	}
	
	private void bindEventBus(){
		eventBus.addHandler(LoginEvent.TYPE,
			new LoginEventHandler() {

			@Override
			public void onLogin(LoginEvent event) {
				displayGreeting();
			}
		});  

		eventBus.addHandler(LogoutEvent.TYPE,
			new LogoutEventHandler() {

			@Override
			public void onLogout(LogoutEvent event) {
				displayLogin();
			}
		});
	}
	
	private void displayGreeting(){
		greetingPresenter.showPage(container);
	}
	
	private void displayLogin(){
		loginPresenter.showPage(container);
	}
	
	public void showPage(){ 
		logger.fine("Requested: GwtHibernateHello module.");
		
		String token = Cookies.getCookie(Constants.COOKIE_TOKEN);
		
		if(token != null){
			checkWithServerIsSessionStillLegal(token);
		}else{
			displayLogin();
		}
	}
	
	private void checkWithServerIsSessionStillLegal(String token){		
		loginService.isSessionStillLegal(token, new SessionLegalityRequest());
	}
	
	private class SessionLegalityRequest implements AsyncCallback<UserDTO>{

		@Override
		public void onSuccess(UserDTO result) {
			logger.finer("Invoked: loginserver.isTokenStillLegal(), returned '" + result + "'." );
			if(result.isLoggedIn()){
				displayGreeting();
			}else{
				displayLogin();
			}
		}
		
		@Override
		public void onFailure(Throwable caught) {
			logger.warning("Invoked: loginserver.isTokenStillLegal(), server unavaible.");
			displayLogin();
		}
	}
}
