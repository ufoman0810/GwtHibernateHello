package com.epsm.gwtHibernateHello.client.presenter;

import java.util.logging.Logger;

import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.ContentContainer;
import com.epsm.gwtHibernateHello.client.view.ErrorMessages;
import com.epsm.gwtHibernateHello.client.view.View;
import com.google.gwt.event.shared.HandlerManager;

public abstract class Presenter{
	protected LoginServiceAsync loginService;
	protected ErrorMessages messages;
	protected HandlerManager eventBus;
	protected View view;
	protected Logger logger;
	
	public Presenter(LoginServiceAsync loginService, ErrorMessages messages,
			HandlerManager eventBus, View view){
		
		logger = Logger.getLogger(this.getClass().getSimpleName());

		if(loginService == null){
			String message = "Constructor: LoginServiceAsync can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}else if(messages == null){
			String message = "Constructor: ErrorMessages can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}else if(eventBus == null){
			String message = "Constructor: HandlerManager can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}else if(view == null){
			String message = "Constructor: View can't be null.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}
		
		this.loginService = loginService;
		this.messages = messages;
		this.eventBus = eventBus;
		this.view = view;
		view.setPresenter(this);
	}
	
	public abstract void showPage(ContentContainer container);
}
