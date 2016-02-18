package com.epsm.gwtHibernateHello.server.service;

import java.util.Date;

import com.epsm.gwtHibernateHello.client.service.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	@Override
	public String getGreetingForTime(Date timeSource) {
		return "good time";
	}
}
