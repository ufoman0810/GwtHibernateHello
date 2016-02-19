package com.epsm.gwtHibernateHello.client;

import java.util.logging.Logger;

import com.epsm.gwtHibernateHello.client.service.LoginService;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.Page;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class GwtHibernateHello implements EntryPoint {
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	
	private Page page = new Page();
	private static Logger logger = Logger.getLogger("GwtHibernateHello");
	
	@Override
	public void onModuleLoad() {
		logger.info("Requested: GwtHibernateHello module.");
		
		
	}

}
