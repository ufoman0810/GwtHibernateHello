package com.epsm.gwtHibernateHello.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TokenVerifier extends RemoteServiceServlet{
	protected boolean isTokenCorrect(String token){
		if(token == null){
			return false;
		}
		
		UserDTO userDto = getUserDTOfromSession();
		
		if(userDto != null && token.equals(userDto.getToken())){
			return true;
		}else{
			return false;
		}
	}
	
	protected  UserDTO getUserDTOfromSession(){
		UserDTO userDto = null;
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		Object userObject = session.getAttribute("user");
		
		if(userObject instanceof UserDTO){
			userDto = (UserDTO) userObject;
		}
		
		return userDto;
	}
	
	protected String getRemoteAddr(){
		HttpServletRequest request = getRequest();
		
		return request.getRemoteAddr();
	}
	
	//Temporary solution, as I didn't find how to test without it
	protected HttpServletRequest getRequest(){
		return getThreadLocalRequest();
	}
}
