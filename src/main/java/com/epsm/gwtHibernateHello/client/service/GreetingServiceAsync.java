package com.epsm.gwtHibernateHello.client.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreetingServiceAsync {
	void getGreeting(Date timeSource, String torken, AsyncCallback<String> callback);
}