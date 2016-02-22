package com.epsm.gwtHibernateHello.share;

import org.junit.Assert;
import org.junit.Test;

import com.epsm.gwtHibernateHello.shared.UserDTO;

public class UserDTOTest {
	private UserDTO user = new UserDTO();
	private String actualString;
	private final String NAME = "someName";
	private final String LOGIN = "someLogin";
	private final String TOKEN = "someToken";
	private final boolean LOGGED_IN = true;
	private final String EXPECTED_STRING = "UserDTO [token not null: true, login: someLogin, "
			+ "userName: someName, loggedIn: true]";
	
	@Test
	public void toStringMethodReturnsExpectedString(){
		user.setName(NAME);
		user.setLogin(LOGIN);
		user.setLoggedIn(LOGGED_IN);
		user.setToken(TOKEN);
		
		actualString = user.toString();
		
		Assert.assertEquals(EXPECTED_STRING, actualString);
	}
}
