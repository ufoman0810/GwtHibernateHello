package com.epsm.gwtHibernateHello.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
	
	@Column(name="login", columnDefinition="VARCHAR (20) NOT NULL UNIQUE")
	private String login;
	
	@Column(name="name", columnDefinition="VARCHAR (20) NOT NULL")
	private String name;
	
	@Column(name="password", columnDefinition="VARCHAR (60) NOT NULL")
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
	
	@Override
	public String toString() {
		return String.format("User [login: %s, name: %s]", login, name);
	}
}
