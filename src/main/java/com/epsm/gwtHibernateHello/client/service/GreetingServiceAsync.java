package com.epsm.gwtHibernateHello.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreetingServiceAsync {
	void getGreetingForTime(AsyncCallback<String> callback);
}
