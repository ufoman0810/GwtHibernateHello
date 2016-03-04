package com.epsm.gwtHibernateHello.client;

import java.util.logging.Logger;

import com.epsm.gwtHibernateHello.client.presenter.GreetingPresenter;
import com.epsm.gwtHibernateHello.client.presenter.LoginPresenter;
import com.epsm.gwtHibernateHello.client.service.GreetingService;
import com.epsm.gwtHibernateHello.client.service.GreetingServiceAsync;
import com.epsm.gwtHibernateHello.client.service.LoginService;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.ErrorMessages;
import com.epsm.gwtHibernateHello.client.view.Greeting;
import com.epsm.gwtHibernateHello.client.view.GreetingView;
import com.epsm.gwtHibernateHello.client.view.Login;
import com.epsm.gwtHibernateHello.client.view.LoginView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;

public class GwtHibernateHello implements EntryPoint {

	@Override
	public void onModuleLoad() {
		Logger logger = Logger.getLogger("GwtHibernateHello");
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
		ErrorMessages messages = GWT.create(ErrorMessages.class);
		HandlerManager eventBus = new HandlerManager(null);
		LoginView loginView = new Login();
		GreetingView greetingView = new Greeting();
		LoginPresenter loginPresenter = new LoginPresenter(loginService, messages, eventBus, loginView);
		GreetingPresenter greetingPresenter = new GreetingPresenter(loginService, greetingService, messages,
				eventBus, greetingView);
		AppController controller = new AppController(loginService, eventBus, loginPresenter, greetingPresenter);
		
		controller.showPage();
		logger.fine("Executed: onModuleLoad().");
	}
}
