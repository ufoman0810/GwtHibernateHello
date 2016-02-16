package com.epsm.gwtHibernteHello.client.page.login;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface LoginResources extends ClientBundle {
	public interface MyCss extends CssResource {
		String blackText();
		String loginButton();
		String box();
		String all();
		String background();
		String footerStyle();
	}

	@Source("Login.css")
	MyCss style();
}