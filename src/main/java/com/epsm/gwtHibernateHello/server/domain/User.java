package com.epsm.gwtHibernateHello.server.domain;

import com.epsm.gwtHibernateHello.shared.UserDTO;

public class User {
	private String login;
	private String name;
	private String password;
	
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
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
