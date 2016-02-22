package com.epsm.gwtHibernateHello.shared;

import java.io.Serializable;

public class UserDTO implements Serializable{
	private static final long serialVersionUID = -3817844216950723658L;
	private String token;
	private String login;
	private String name;
	private boolean loggedIn;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	@Override
	public String toString() {
		return "UserDTO [token not null: " +(token != null) + ", login: "
				+ login + ", userName: " + name + ", loggedIn: " + loggedIn + "]";
	}
}
