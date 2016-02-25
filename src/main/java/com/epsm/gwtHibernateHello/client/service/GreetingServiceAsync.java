package com.epsm.gwtHibernateHello.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreetingServiceAsync {
	void getGreeting(String timeAsString, String torken, AsyncCallback<String> callback);
}