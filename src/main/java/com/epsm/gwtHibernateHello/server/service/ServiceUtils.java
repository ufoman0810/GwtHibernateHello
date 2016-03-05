package com.epsm.gwtHibernateHello.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ServiceUtils extends RemoteServiceServlet{
	private Logger logger = LoggerFactory.getLogger(ServiceUtils.class);
	
	protected boolean isTokenCorrect(String token){
		if(token == null){
			logger.debug("Executed: isTokenCorrect(...), returned: false, token is null.");
			return false;
		}
		
		UserDTO userDto = getUserDTOfromSession();
		
		if(userDto != null && isGrantedTokenEqualsToSaved(token, userDto)){
			logger.debug("Executed: isTokenCorrect(...), returned: true.");
			return true;
		}else{
			logger.debug("Executed: isTokenCorrect(...), returned: false.");
			return false;
		}
	}
	
	private boolean isGrantedTokenEqualsToSaved(String grantedToken, UserDTO userDto){
		boolean equals = grantedToken.equals(userDto.getToken());
		logger.debug("Executed: isGrantedTokenEqualsToSaved(), returned: {}.", equals);
		
		return equals;
	}
	
	protected  UserDTO getUserDTOfromSession(){
		UserDTO userDto = null;
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		Object userObject = session.getAttribute("user");
		
		if(userObject instanceof UserDTO){
			userDto = (UserDTO) userObject;
		}
		
		logger.debug("Executed: getUserDTOfromSession(), returned: {}.", userDto);
		
		return userDto;
	}
	
	protected String getRemoteAddr(){
		HttpServletRequest request = getRequest();
		
		return request.getRemoteAddr();
	}
	
	protected String getUserLogin(){
		UserDTO userDTO = getUserDTOfromSession();
		
		if(userDTO == null){
			return "there is no user in session.";
		}else{
			return userDTO.getLogin();
		}
	}
	
	//Temporary solution, as I didn't find how to test services without it.
	protected HttpServletRequest getRequest(){
		return getThreadLocalRequest();
	}
}
