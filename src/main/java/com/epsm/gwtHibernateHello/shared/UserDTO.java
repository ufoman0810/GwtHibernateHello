package com.epsm.gwtHibernateHello.shared;

import java.io.Serializable;

public class UserDTO implements Serializable{
	private static final long serialVersionUID = -3817844216950723658L;
	private String sessionId;
	private String userName;
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
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	@Override
	public String toString() {
		return "UserDTO [sessionId=" + sessionId + ", userName=" + userName + ", "
				+ " loggedIn=" + loggedIn + "]";
	}
}
