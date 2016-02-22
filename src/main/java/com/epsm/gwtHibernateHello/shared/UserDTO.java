package com.epsm.gwtHibernateHello.shared;

import java.io.Serializable;

public class UserDTO implements Serializable{
	private static final long serialVersionUID = -3817844216950723658L;
	private String token;
	private String userName;
	private boolean loggedIn;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
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
		return String.format("UserDTO [token not null: %s, userName: %s, loggedIn: %s.", 
				(token != null), userName, userName);
	}
}
