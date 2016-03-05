package com.epsm.gwtHibernateHello.client.presenter;

import java.util.Date;

import com.epsm.gwtHibernateHello.client.event.LogoutEvent;
import com.epsm.gwtHibernateHello.client.service.GreetingServiceAsync;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.ContentContainer;
import com.epsm.gwtHibernateHello.client.view.ErrorMessages;
import com.epsm.gwtHibernateHello.client.view.GreetingView;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GreetingPresenter extends Presenter{
	private GreetingServiceAsync greetingService;
	private DateTimeFormat formatter;
	
	public GreetingPresenter(LoginServiceAsync loginService, GreetingServiceAsync greetingService,
			ErrorMessages messages, HandlerManager eventBus, GreetingView view){
		
		super(loginService, messages, eventBus, view);

		if(greetingService == null){
			String message = "Constructor: GreetingServiceAsync can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}

		this.greetingService = greetingService;
		formatter = DateTimeFormat.getFormat(Constants.TIME_PATTERN);
		logger.config("GreetingPresenter created.");
	}
	
	@Override
	public void showPage(ContentContainer container) {
		logger.finest("Invoked: showPage(...).");
		view.resetState();
		container.setContent(view.asComposite());
		tryToGetGreetingFromGreetingServer();
	}
	
	private void tryToGetGreetingFromGreetingServer(){
		Date timeSource = new Date();
		String token = Cookies.getCookie(Constants.COOKIE_TOKEN);
		String timeAsString = formatter.format(timeSource);
		greetingService.getGreeting(timeAsString, token, new GreetingRequest());
		logger.finer("Requested: getGreeting(" + timeAsString + ",...) to a GreetingService.");
	}
	
	private class GreetingRequest implements AsyncCallback<String>{
		
		@Override
		public void onSuccess(String result) {
			((GreetingView) view).displayGreeting(result);
			logger.finer("Executed: getGreeting(..) request to a GreetingService. Displayed greeting: "
					+ result);
		}
		
		@Override
		public void onFailure(Throwable caught) {
			String message = messages.serverUnavaible();
			view.displayError(message);
			logger.warning("Failed: getGreeting(..) request to a GreetingService. Displayed: " 
					+ message);
		}
	}
	
	public void executeLogout(){	
		String token = Cookies.getCookie(Constants.COOKIE_TOKEN);
		loginService.logout(token, new LogoutRequest());
		logger.finer("Requested: executeLogout() to a LoginService.");
	}
	
	private class LogoutRequest implements AsyncCallback<Void>{

		@Override
		public void onSuccess(Void result) {
			eventBus.fireEvent(new LogoutEvent());
			logger.finer("Executed: executeLogout() request to LoginService. LogoutEvent fired.");
		}

		@Override
		public void onFailure(Throwable caught) {
			String message = messages.serverUnavaible();
			view.displayError(message);
			logger.finer("Failed: executeLogout() request to LoginService. Displayed message: "
					+ message + ".");
		}
	}
}
