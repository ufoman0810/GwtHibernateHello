package com.epsm.gwtHibernateHello.server.service;

import com.epsm.gwtHibernateHello.client.service.LoginService;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	@Override
	public UserDTO loginServer(String name, String password) {
		System.out.println("here_1");
		
		UserDTO dto = new UserDTO();
		dto.setUserName("a");
		dto.setSessionId("b");
		dto.setLoggedIn(true);
		
		return dto;
	}

	@Override
	public UserDTO loginFromSessionServer() {
		System.out.println("here_2");
		
		UserDTO dto = new UserDTO();
		dto.setUserName("a");
		dto.setSessionId("b");
		dto.setLoggedIn(true);
		
		return dto;
	}
}
