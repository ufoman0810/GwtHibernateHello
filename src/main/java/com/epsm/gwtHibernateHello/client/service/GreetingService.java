package com.epsm.gwtHibernateHello.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greeting")
public interface GreetingService  extends RemoteService{
	String getGreeting(String timeAsString, String token);
}