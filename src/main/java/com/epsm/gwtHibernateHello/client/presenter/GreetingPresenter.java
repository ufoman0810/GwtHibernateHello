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
		logger.config("PagePresenter created.");
	}
	
	@Override
	public void showPage(ContentContainer container) {
		view.resetState();
		container.setContent(view.asComposite());
		tryToGetGreetingFromGreetingServer();
	}
	
	private void tryToGetGreetingFromGreetingServer(){
		Date timeSource = new Date();
		String token = Cookies.getCookie(Constants.COOKIE_TOKEN);
		String timeAsString = formatter.format(timeSource);
		greetingService.getGreeting(timeAsString, token, new GetGreetingRequest());
		logger.finer("Invoked: greetingService.GetGreetingRequest(" + timeAsString + ",...).");
	}
	
	private class GetGreetingRequest implements AsyncCallback<String>{
		
		@Override
		public void onSuccess(String result) {
			logger.finer("Invoked: greetingService.getGreetingForTime(...), returned '" + result + "'.");
			((GreetingView) view).displayGreeting(result);
		}
		
		@Override
		public void onFailure(Throwable caught) {
			logger.warning("Invoked: greetingService.getGreetingForTime(...), server unavaible.");
			view.displayError(messages.serverUnavaible());
		}
	}
	
	public void executeLogout(){	
		String token = Cookies.getCookie(Constants.COOKIE_TOKEN);
		loginService.logout(token, new LogoutRequest());
	}
	
	private class LogoutRequest implements AsyncCallback<Void>{

		@Override
		public void onSuccess(Void result) {
			logger.info("Invoked: loginService.logout(...), request executed.");
			eventBus.fireEvent(new LogoutEvent());
		}

		@Override
		public void onFailure(Throwable caught) {
			logger.warning("Invoked: loginService.logout(...), server unavaible.");
			view.displayError(messages.serverUnavaible());
		}
	}
}
