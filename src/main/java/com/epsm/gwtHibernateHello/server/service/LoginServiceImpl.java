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
import com.epsm.gwtHibernateHello.shared.UserDTO;

@SuppressWarnings("serial")
public class LoginServiceImpl extends TokenVerifier implements LoginService {
	private UserDao dao;
	private ThreadLocal<User> user;
	private Logger logger;
	
	public LoginServiceImpl(UserDao dao) {
		logger = LoggerFactory.getLogger(LoginServiceImpl.class);
		
		if(dao == null){
			String message = "Constructor: UserDao can't be null.";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		
		user = new ThreadLocal<User>();
		this.dao = dao;
		logger.debug("Created: LoginServiceImpl.");
	}
	
	@Override
	public UserDTO loginServer(String login, String password){
		getUserByLoginFromDB(login);
		
		if(isUserExists() && isPasswordCorrect(password)){
			logger.info("Logged in: user with login: {}, from: {}.", login, getRemoteAddr());
			return createUserDTOAndSaveInSession();
		}else{
			logger.info("Access denied: for user with login: {}, from: {}.", login, getRemoteAddr());
			return createNotLogedInUserDTO();
		}
	}
	
	private void getUserByLoginFromDB(String login){
		User retreivedUser = dao.findUserByLogin(login);
		user.set(retreivedUser);
		logger.debug("Invoked: getUserByLoginFromDB({}), DB returned user: {}.",
				login, retreivedUser != null);
	}
	
	private boolean isUserExists(){
		return user.get() != null;
	}
	
	private boolean isPasswordCorrect(String password){
		String storedHash = user.get().getPassword();
		boolean correct = BCrypt.checkpw(password, storedHash);
		logger.debug("Invoked: isPasswordCorrect(...), returned: {}.", correct);
		
		return correct;
	}
	
	private UserDTO createUserDTOAndSaveInSession(){
		UserDTO userDto = createLoggedInUserDTO();
		saveUserDTOInSession(userDto);
		
		return userDto;
	}
	
	private UserDTO createLoggedInUserDTO(){
		UserDTO dto = new UserDTO();
		dto.setUserName(user.get().getName());
		dto.setToken(generateToken());
		dto.setLoggedIn(true);
		logger.debug("Invoked: createLoggedInUserDTO(), returned: {}.", dto);
		
		return dto;
	}
	
	private String generateToken(){
		return UUID.randomUUID().toString();
	}
	
	private void saveUserDTOInSession(UserDTO userDto){
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("user", userDto);
		logger.debug("Invoked: saveUserDTOInSession(...) for user with login: {}.", user.get().getLogin());
	}
	
	private UserDTO createNotLogedInUserDTO(){
		UserDTO dto = new UserDTO();
		dto.setUserName("");
		dto.setToken("");
		dto.setLoggedIn(false);
		logger.debug("Invoked: createNotLogedInUserDTO(), returned: {}.", dto);
		
		return dto;
	}

	@Override
	public UserDTO isSessionStillLegal(String token){
		if(isTokenCorrect(token)){
			logger.info("Session restored: for user with login: {}, from: {}.",
					user.get().getLogin(), getRemoteAddr());
			return getUserDTOfromSession();
		}else{
			logger.info("Session restoring denied: for user from: {}.", getRemoteAddr());
			return createNotLogedInUserDTO();
		}
	}
	
	@Override
	public void logout(String token) {
		if(isTokenCorrect(token)){
			logger.info("Logged out: user with login: {}, from: {}.",	
					user.get().getLogin(), getRemoteAddr());
			deleteUserDTOFromSession();
		}else{
			logger.warn("Denied: logging out fo user with wrong token from: {}.", getRemoteAddr());
		}
	}
	
	private void deleteUserDTOFromSession(){
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		logger.debug("Invoked: deleteUserDTOFromSession().");
	}
}
