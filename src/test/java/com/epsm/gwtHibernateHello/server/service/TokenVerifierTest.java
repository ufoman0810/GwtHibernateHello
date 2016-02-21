package com.epsm.gwtHibernateHello.server.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.gwtHibernateHello.shared.UserDTO;

import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class TokenVerifierTest {
	private TokenVerifier verifier;
	private HttpServletRequest request;
	private HttpSession session;
	private UserDTO userDto;
	private final String RIGHT_TOKEN = "rightToken";
	private final String WRONG_TOKEN = "wrongToken";
	private final String REMOTE_ADDRESS = "someAddress";
	
	@Before
	public void setUp(){
		verifier = spy(TokenVerifier.class);
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		userDto = new UserDTO();
		
		
		when(verifier.getRequest()).thenReturn(request);
		when(request.getSession()).thenReturn(session);
		when(request.getRemoteAddr()).thenReturn(REMOTE_ADDRESS);
		when(session.getAttribute("user")).thenReturn(userDto);
		userDto.setToken(RIGHT_TOKEN);
	}
	
	@Test
	public void getUserDTOfromSessionMethodReturnsExpectedObject(){
		UserDTO dto = verifier.getUserDTOfromSession();
		
		Assert.assertEquals(userDto, dto);
	}
	
	@Test
	public void isTokenCorrectMethodReturnsFalseIfTokenIsNull(){
		boolean answer = verifier.isTokenCorrect(null);
		
		Assert.assertFalse(answer);
	}
	
	@Test
	public void isTokenCorrectMethodReturnsFalseIfTokenDoesNotEqualToSavedInUserDTO(){
		boolean answer = verifier.isTokenCorrect(WRONG_TOKEN);
		
		Assert.assertFalse(answer);
	}
	
	@Test
	public void isTokenCorrectMethodReturnsFalseIfThereIsNoUserDto(){
		when(session.getAttribute("user")).thenReturn(null);
		
		boolean answer = verifier.isTokenCorrect(WRONG_TOKEN);
		
		Assert.assertFalse(answer);
	}
	
	@Test
	public void isTokenCorrectMethodReturnsTueIfTokenEqualsToSavedInUserDTO(){
		boolean answer = verifier.isTokenCorrect(RIGHT_TOKEN);
		
		Assert.assertTrue(answer);
	}
	
	@Test 
	public void getRemoteAddrMethodReturnsExpectedValue(){
		String actualAdress = verifier.getRemoteAddr();
		
		Assert.assertEquals(REMOTE_ADDRESS, actualAdress);
	}
}
