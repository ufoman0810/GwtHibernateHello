package com.epsm.gwtHibernateHello.server.domain;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {
	private User user = new User();
	private String actualString;
	private final String NAME = "someName";
	private final String LOGIN = "someLogin";
	private final String EXPECTED_STRING = "User [login: someLogin, name: someName]";
	
	@Test
	public void toStringMethodReturnsExpectedString(){
		user.setName(NAME);
		user.setLogin(LOGIN);
		
		actualString = user.toString();
		
		Assert.assertEquals(EXPECTED_STRING, actualString);
	}
}
