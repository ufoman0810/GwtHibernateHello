package com.epsm.gwtHibernateHello.server.service;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.epsm.gwtHibernateHello.server.domain.User;
import com.epsm.gwtHibernateHello.server.repository.UserDao;
import com.epsm.gwtHibernateHello.shared.UserDTO;

public class LoginServiceImplTest{
	private UserDao dao;
	private LoginServiceImpl service;
	private User user;
	private UserDTO userDto;
	private HttpServletRequest request;
	private HttpSession session;
	private final String USER_NAME = "someName";
	private final String RIGHT_LOGIN = "someLogin";
	private final String WRONG_LOGIN = "wrongLogin";
	private final String RIGHT_PASSWORD = "somePassword";
	private final String WRONG_PASSWORD = "wrongPassword";
	private final String HASHED_PASSWORD = "$2a$10$QqD0GGoNWD8XrDlUddMnUuZgP.cqjjM.rnAe/NsTpOwf8GvyJ8Paq";
	private final String WRONG_TOKEN = "wrongToken";
	private String rightToken;
	
	@Before
	public void setUp() {
		dao = mock(UserDao.class);
		service = spy(new LoginServiceImpl(dao));
		user = new User();
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		
		user.setName(USER_NAME);
		user.setLogin(RIGHT_LOGIN);
		user.setPassword(HASHED_PASSWORD);
		when(dao.findUserByLogin(RIGHT_LOGIN)).thenReturn(user);
		when(service.getRequest()).thenReturn(request);
		when(request.getSession()).thenReturn(session);
		
		when(session.getAttribute("user")).thenAnswer(new Answer<UserDTO>() {
			
			@Override
			public UserDTO answer(InvocationOnMock invocation) throws Throwable {
				return userDto;
			}
		});
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfUserDaoIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: UserDao can't be null.");
	    
	    new LoginServiceImpl(null);
	}
	
	@Test
	public void loginServerMethodReturnsNotLogdedInUserDTOIfLoginAndPasswordAreWronf(){
		loginServerWithWrongLoginAndWrongPassword();
		
		Assert.assertFalse(userDto.isLoggedIn());
	}
	
	private void loginServerWithWrongLoginAndWrongPassword(){
		userDto = service.loginServer(WRONG_LOGIN, WRONG_PASSWORD);
	}
	
	@Test
	public void loginServerMethodReturnsNotLogdedInUserDTOIfOnlyPasswordIsWrong(){
		loginServerWithRightLoginAndWrongPassword();
		
		Assert.assertFalse(userDto.isLoggedIn());
	}
	
	private void loginServerWithRightLoginAndWrongPassword(){
		userDto = service.loginServer(RIGHT_LOGIN, WRONG_PASSWORD);
	}
	
	@Test
	public void loginServerMethodReturnsRightUserDTOIfCredentialsAreRight(){
		loginServerWithRightLoginAndRightPassword();
		
		Assert.assertTrue(userDto.isLoggedIn());
		Assert.assertEquals(USER_NAME, userDto.getUserName());
		Assert.assertNotNull(userDto.getToken());
	}
	
	private void loginServerWithRightLoginAndRightPassword(){
		userDto = service.loginServer(RIGHT_LOGIN, RIGHT_PASSWORD);
	}
	
	@Test
	public void isSessionStillLegalMethodReturnsNotLogdedInUserDTOIfSessionWasNotActiveBefore(){
		tryToJoinNewSession();
		
		Assert.assertFalse(userDto.isLoggedIn());
	}
	
	private void tryToJoinNewSession(){
		userDto = service.isSessionStillLegal(null);
	}
	
	@Test
	public void isSessionStillLegalMethodReturnsNotLogdedInUserDTOIfSessionIsActiveButTokenIsWrong(){
		loginServerWithRightLoginAndRightPassword();
		tryToJoinSessionWithWrongToken();
		
		Assert.assertFalse(userDto.isLoggedIn());
	}
	
	private void tryToJoinSessionWithWrongToken(){
		userDto = service.isSessionStillLegal(WRONG_TOKEN);
	}
	
	@Test
	public void isSessionStillLegalMethodReturnsTheSameInstanceOfUserDTOIfSessionActive(){
		loginServerWithRightLoginAndRightPassword();
		tryToJoinSessionWithRightToken();
		
		Assert.assertTrue(userDto.isLoggedIn());
		Assert.assertEquals(USER_NAME, userDto.getUserName());
		Assert.assertNotNull(userDto.getToken());
	}
	
	private void tryToJoinSessionWithRightToken(){
		rightToken = userDto.getToken();
		userDto = service.isSessionStillLegal(rightToken);
	}
	
	@Test
	public void logoutMethodDoNothingIfCalledForNewSession(){
		tryToLogoutFromNewSession();
		
		verify(session, never()).removeAttribute("user");
	}
	
	private void tryToLogoutFromNewSession(){
		service.logout(null);
	}
	
	@Test
	public void logoutMethodDoNothingIfCalledWithWrongToken(){
		loginServerWithRightLoginAndRightPassword();
		tryToLogoutWithWrongToken();
		
		verify(session, never()).removeAttribute("user");
	}
	
	private void tryToLogoutWithWrongToken(){
		service.logout(WRONG_TOKEN);
	}
	
	@Test
	public void logoutMethodBrakesSessionIfCalledWithRightToken(){
		loginServerWithRightLoginAndRightPassword();
		tryToLogoutWithRightToken();
		
		verify(session).removeAttribute("user");
	}
	
	private void tryToLogoutWithRightToken(){
		rightToken = userDto.getToken();
		service.logout(rightToken);
	}
}
