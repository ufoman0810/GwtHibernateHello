package com.epsm.gwtHibernateHello.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TokenVerifier extends RemoteServiceServlet{
	private Logger logger = LoggerFactory.getLogger(TokenVerifier.class);
	
	protected boolean isTokenCorrect(String token){
		if(token == null){
			logger.debug("Invoked: isTokenCorrect(...), returned: false, token is null.");
			return false;
		}
		
		UserDTO userDto = getUserDTOfromSession();
		
		if(isUserExist(userDto) && isGrantedTokenEqualsToSavedToken(token, userDto)){
			logger.debug("Invoked: isTokenCorrect(...), returned: true.");
			return true;
		}else{
			logger.debug("Invoked: isTokenCorrect(...), returned: false.");
			return false;
		}
	}
	
	private boolean isUserExist(UserDTO userDto){
		return userDto != null;
	}
	
	private boolean isGrantedTokenEqualsToSavedToken(String token, UserDTO userDto){
		boolean equals = token.equals(userDto.getToken());
		logger.debug("Invoked: isGrantedTokenEqualsToSavedToken(), returned: {}.", equals);
		
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
		
		logger.debug("Invoked: getUserDTOfromSession(), returned: {}.", userDto);
		
		return userDto;
	}
	
	protected String getRemoteAddr(){
		HttpServletRequest request = getRequest();
		
		return request.getRemoteAddr();
	}
	
	//Temporary solution, as I didn't find how to test services without it.
	protected HttpServletRequest getRequest(){
		return getThreadLocalRequest();
	}
}
