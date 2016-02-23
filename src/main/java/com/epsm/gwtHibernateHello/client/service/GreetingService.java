package com.epsm.gwtHibernateHello.client.service;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greeting")
public interface GreetingService  extends RemoteService{
	String getGreetingForTime(Date timeSource, String token);
}