package com.epsm.gwtHibernateHello.shared;

import java.io.Serializable;

public class UserDTO implements Serializable{
	private static final long serialVersionUID = -5882483518395338946L;
	private String sessionId;
	private String userName;
	private String userGreeting;
	private boolean loggedIn;
	
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserGreeting() {
		return userGreeting;
	}
	
	public void setUserGreeting(String userGreeting) {
		this.userGreeting = userGreeting;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
}
