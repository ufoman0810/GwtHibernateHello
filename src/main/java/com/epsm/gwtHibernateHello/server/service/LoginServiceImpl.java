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
	private ThreadLocal<User> user = new ThreadLocal<User>();
	private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	public LoginServiceImpl(UserDao dao) {
		if(dao == null){
			String message = "Constructor: UserDao can't be null.";
			throw new IllegalArgumentException(message);
		}
		
		this.dao = dao;
	}
	
	@Override
	public UserDTO loginServer(String login, String password){
		getUserByLoginFromDB(login);
		
		if(isUserExists() && isPasswordCorrect(password)){
			return createUserDTOAndSaveInSession();
		}else{
			return createNotLogedInUserDTO();
		}
	}
	
	private void getUserByLoginFromDB(String login){
		user.set(dao.findUserByLogin(login));
	}
	
	private boolean isUserExists(){
		return user.get() != null;
	}
	
	private boolean isPasswordCorrect(String password){
		String storedHash = user.get().getPassword();

		return BCrypt.checkpw(password, storedHash);
	}
	
	private UserDTO createUserDTOAndSaveInSession(){
		UserDTO userDto = createLogedInUserDTO();
		saveUserDTOInSession(userDto);
		
		return userDto;
	}
	
	private UserDTO createLogedInUserDTO(){
		UserDTO dto = new UserDTO();
		dto.setUserName(user.get().getName());
		dto.setToken(generateToken());
		dto.setLoggedIn(true);
		
		return dto;
	}
	
	private String generateToken(){
		return UUID.randomUUID().toString();
	}
	
	private void saveUserDTOInSession(UserDTO userDto){
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		session.setAttribute("user", userDto);
	}
	
	private UserDTO createNotLogedInUserDTO(){
		UserDTO dto = new UserDTO();
		dto.setUserName("");
		dto.setToken("");
		dto.setLoggedIn(false);
		
		return dto;
	}

	@Override
	public UserDTO isSessionStillLegal(String token){
		if(isTokenCorrect(token)){
			return getUserDTOfromSession();
		}else{
			return createNotLogedInUserDTO();
		}
	}
	
	@Override
	public void logout(String token) {
		if(isTokenCorrect(token)){
			deleteUserDTOFromSession();
		}
	}
	
	private void deleteUserDTOFromSession(){
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("user");
	}
}
