package com.epsm.gwtHibernteHello.client.service;

import com.epsm.gwtHibernteHello.shared.UserDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService{
	UserDTO loginServer(String name, String password);
	UserDTO loginFromSessionServer();
}
