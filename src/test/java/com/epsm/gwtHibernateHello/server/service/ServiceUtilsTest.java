package com.epsm.gwtHibernateHello.server.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.gwtHibernateHello.server.service.ServiceUtils;
import com.epsm.gwtHibernateHello.shared.UserDTO;

public class ServiceUtilsTest {
	private ServiceUtils utils;
	private HttpServletRequest request;
	private HttpSession session;
	private UserDTO userDto;
	private final String RIGHT_TOKEN = "rightToken";
	private final String WRONG_TOKEN = "wrongToken";
	private final String REMOTE_ADDRESS = "someAddress";
	private final String NO_USER_IN_SESSION_MESSAHE = "there is no user in session.";
	private final String LOGIN = "someLogin";
	
	@Before
	public void setUp(){
		utils = spy(ServiceUtils.class);
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		userDto = new UserDTO();
		
		
		when(utils.getRequest()).thenReturn(request);
		when(request.getSession()).thenReturn(session);
		when(request.getRemoteAddr()).thenReturn(REMOTE_ADDRESS);
		userDto.setToken(RIGHT_TOKEN);
		userDto.setLogin(LOGIN);
	}
	
	@Test
	public void getUserDTOfromSessionMethodReturnsExpectedObjectIfThereIsUserDTO(){
		putUserDTOInSession();
		
		UserDTO dto = utils.getUserDTOfromSession();
		
		Assert.assertEquals(userDto, dto);
	}
	
	private void putUserDTOInSession(){
		when(session.getAttribute("user")).thenReturn(userDto);
	}
	
	@Test
	public void isTokenCorrectMethodReturnsFalseIfTokenIsNull(){
		boolean answer = utils.isTokenCorrect(null);
		
		Assert.assertFalse(answer);
	}
	
	@Test
	public void isTokenCorrectMethodReturnsFalseIfTokenDoesNotEqualToSavedInUserDTO(){
		putUserDTOInSession();
		
		boolean answer = utils.isTokenCorrect(WRONG_TOKEN);
		
		Assert.assertFalse(answer);
	}
	
	@Test
	public void isTokenCorrectMethodReturnsFalseIfThereIsNoUserDto(){
		boolean answer = utils.isTokenCorrect(WRONG_TOKEN);
		
		Assert.assertFalse(answer);
	}
	
	@Test
	public void isTokenCorrectMethodReturnsTueIfTokenEqualsToSavedInUserDTO(){
		putUserDTOInSession();
		
		boolean answer = utils.isTokenCorrect(RIGHT_TOKEN);
		
		Assert.assertTrue(answer);
	}
	
	@Test 
	public void getRemoteAddrMethodReturnsExpectedValue(){
		String actualAdress = utils.getRemoteAddr();
		
		Assert.assertEquals(REMOTE_ADDRESS, actualAdress);
	}
	
	@Test 
	public void getLoginMethodReturnsExpextedMessageIfThereIsNoUserInSession(){
		String login = utils.getUserLogin();
		
		Assert.assertEquals(NO_USER_IN_SESSION_MESSAHE, login);
	}
	
	@Test 
	public void getUserLoginMethodReturnsUserLoginIfUserIsInSession(){
		putUserDTOInSession();
		
		String login = utils.getUserLogin();
		
		Assert.assertEquals(LOGIN, login);
	}
}
