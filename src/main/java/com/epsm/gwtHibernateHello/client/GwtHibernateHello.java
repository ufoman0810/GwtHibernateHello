package com.epsm.gwtHibernateHello.client;

import java.util.logging.Logger;

import com.epsm.gwtHibernateHello.client.presenter.PagePresenter;
import com.epsm.gwtHibernateHello.client.service.GreetingService;
import com.epsm.gwtHibernateHello.client.service.GreetingServiceAsync;
import com.epsm.gwtHibernateHello.client.service.LoginService;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.Page;
import com.epsm.gwtHibernateHello.client.view.PageView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class GwtHibernateHello implements EntryPoint {
	private LoginServiceAsync loginService;
	private GreetingServiceAsync greetingService;
	private PageView page = new Page();
	private PagePresenter presenter;
	private static Logger logger = Logger.getLogger("GwtHibernateHello");
	
	public GwtHibernateHello() {
		loginService = GWT.create(LoginService.class);
		greetingService = GWT.create(GreetingService.class);
		page = new Page();
		presenter = new PagePresenter(loginService, greetingService, page);
		logger = Logger.getLogger("GwtHibernateHello");
		
		page.setPresenter(presenter);
		logger.config("Created: client part of GwtHibernateHello module.");
	}
	
	@Override
	public void onModuleLoad() {
		logger.fine("Requested: GwtHibernateHello module.");
		presenter.showPage();
	}

}
