package com.epsm.gwtHibernateHello.server.service;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.client.service.LoginService;
import com.epsm.gwtHibernateHello.server.domain.User;
import com.epsm.gwtHibernateHello.server.repository.UserDao;
import com.epsm.gwtHibernateHello.server.repository.UserDaoImpl;
import com.epsm.gwtHibernateHello.shared.UserDTO;

@SuppressWarnings("serial")
public class LoginServiceImpl extends ServiceUtils implements LoginService {
	private UserDao dao = UserDaoImpl.getInstatnce();
	private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Override
	public UserDTO loginServer(String login, String password){
		User user = dao.findUserByLogin(login);
		
		if(user != null && isGrantedPasswordCorrect(user, password)){
			logger.info("Logged in: user with login: {}, from: {}.", login, getRemoteAddr());
			UserDTO userDto = createLoggedInUserDTO(user);
			saveUserDTOInSession(userDto);
			
			return userDto;
		}else{
			logger.info("Access denied: for user with login: {}, from: {}.", login, getRemoteAddr());
			return createNotLogedInUserDTO();
		}
	}
	
	private boolean isGrantedPasswordCorrect(User user, String password){
		String storedHash = user.getPassword();
		boolean correct = BCrypt.checkpw(password, storedHash);
		logger.debug("Executed: isPasswordCorrect(...), returned: {}.", correct);
		
		return correct;
	}
	
	private UserDTO createLoggedInUserDTO(User user){
		UserDTO dto = new UserDTO();
		dto.setLogin(user.getLogin());
		dto.setName(user.getName());
		dto.setToken(UUID.randomUUID().toString());
		dto.setLoggedIn(true);
		logger.debug("Executed: createLoggedInUserDTO(), returned: {}.", dto);
		
		return dto;
	}
	
	private void saveUserDTOInSession(UserDTO userDto){
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("user", userDto);
		logger.debug("Executed: saveUserDTOInSession(...) for user with login: {}.", userDto.getLogin());
	}
	
	private UserDTO createNotLogedInUserDTO(){
		UserDTO dto = new UserDTO();
		dto.setLogin("");
		dto.setName("");
		dto.setToken("");
		dto.setLoggedIn(false);
		logger.debug("Executed: createNotLogedInUserDTO(), returned: {}.", dto);
		
		return dto;
	}

	@Override
	public UserDTO isSessionStillLegal(String token){
		logger.info("Invoked: isSessionStillLegal(...), from: {}.", getRemoteAddr());

		if(isTokenCorrect(token)){
			logger.info("Session restored: for user with login: {}, from: {}.",	getUserLogin(), getRemoteAddr());
			return getUserDTOfromSession();
		}else{
			logger.info("Session restoring denied: for user from: {}.", getRemoteAddr());
			return createNotLogedInUserDTO();
		}
	}
	
	@Override
	public void logout(String token) {
		logger.info("Invoked: logout(...) from: {}.", getRemoteAddr());
		
		if(isTokenCorrect(token)){
			logger.info("Logged out: user with login: {}, from: {}.", getUserLogin(), getRemoteAddr());
			deleteUserDTOFromSession();
		}else{
			logger.warn("Denied: logging out for user with wrong token from: {}.", getRemoteAddr());
		}
	}
	
	private void deleteUserDTOFromSession(){
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		logger.debug("Executed: deleteUserDTOFromSession().");
	}
}
