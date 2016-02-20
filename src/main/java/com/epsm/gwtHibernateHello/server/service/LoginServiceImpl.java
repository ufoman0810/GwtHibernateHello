package com.epsm.gwtHibernateHello.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epsm.gwtHibernateHello.client.service.LoginService;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	@Override
	public UserDTO loginServer(String login, String password) {
		if(login.equals("john") && password.equals("smith")){
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
	public UserDTO isSessionIdStillLegal(String clientSessionId){
		String realSessionId = getRealSessionId();
		
		if(clientSessionId.equals(realSessionId)){
			return createLogedInDTO();
		}else{
			return createNotLogedInDTO();
		}
	}
	
	private String getRealSessionId(){
		HttpServletRequest request = getThreadLocalRequest();
		HttpSession session = request.getSession();
		
		return session.getId();
	}
}
