package com.epsm.gwtHibernateHello.server.service;

import com.epsm.gwtHibernateHello.client.service.LoginService;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	@Override
	public UserDTO loginServer(String name, String password) {
		if(name.equals("john") && password.equals("smith")){
			return createLogedInDTO();
		}else{
			return createNotLogedInDTO();
		}
	}
	
	private UserDTO createLogedInDTO(){
		UserDTO dto = new UserDTO();
		dto.setUserName("name");
		dto.setSessionId("846");
		dto.setLoggedIn(true);
		
		return dto;
	}
	
	private UserDTO createNotLogedInDTO(){
		UserDTO dto = new UserDTO();
		dto.setUserName("");
		dto.setSessionId("");
		dto.setLoggedIn(false);
		
		return dto;
	}
	
	@Override
	public UserDTO loginFromSessionServer() {
		return createLogedInDTO();
	}
}
