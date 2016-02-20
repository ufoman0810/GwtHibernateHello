package com.epsm.gwtHibernateHello.client.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreetingServiceAsync {
	void getGreetingForTime(Date timeSource, String sessionID, AsyncCallback<String> callback);
}