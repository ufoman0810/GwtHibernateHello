package com.epsm.gwtHibernateHello.client.presenter;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.epsm.gwtHibernateHello.client.service.GreetingServiceAsync;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.PageView;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cookies.class)
public class PagePresenterTest {
	private LoginServiceAsync loginService;
	private GreetingServiceAsync greetingService;
	private PageView view;
	private PagePresenter presenter;
	private final String USERNAME = "John";
	private final String SESSION_ID = "someSessionId";
	private final String GREETING = "Hello";
	private final String LOGIN = "someLogin";
	private final String PASSWORD = "somePassword";
	private final String TOO_SHORT_PASSWORD = "123";
	private final String TOO_SHORT_LOGIN = "abc";
	
	@Before
	public void setUp(){
		loginService = mock(LoginServiceAsync.class);
		greetingService = mock(GreetingServiceAsync.class);
		view = mock(PageView.class);
		presenter = new PagePresenter(loginService, greetingService, view);
		PowerMockito.mockStatic(Cookies.class);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfloginServiceIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: loginService can't be null.");
	    
	    new PagePresenter(null, greetingService,  view);
	}
	
	@Test
	public void exceptionInConstructorIfGreetingServicIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: greetingService can't be null.");
	    
	    new PagePresenter(loginService, null, view);
	}
	
	@Test
	public void exceptionInConstructorIfViewIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: view can't be null.");
	    
	    new PagePresenter(loginService, greetingService, null);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void showPageMethodDoesNotTryCheckWithServerIfSessionStillLegalIfThereIsNotSessionIdInCookies(){
		makeSessionIdNotExistInCookies();
		
		presenter.showPage();
		
		verify(loginService, never()).isSessionIdStillLegal(isA(AsyncCallback.class));
	}
	
	private void makeSessionIdNotExistInCookies(){
		when(Cookies.getCookie(Constants.COOKIE_SESSION_ID)).thenReturn(null);
	}
	
	@Test
	public void displaysLoginFillingWithoutErrorIfSessionIdNotExistsInCookies(){
		makeSessionIdNotExistInCookies();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
		verify(view, never()).displayLoginError(anyString());
	}
	
	@Test
	public void displaysLoginFillingWithoutErrorIfSessionIsNotLegalOnLoginServer(){
		makeSessionNotLegalOnLoginServer();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
		verify(view, never()).displayLoginError(anyString());
	}
	
	private void makeSessionNotLegalOnLoginServer(){
		makeSessionIdExistInCookies();
		makeLoginServerReturnNotLoggedInUserDTOWithCheckWithServerIsSessionIdStillLegalMethod();
	}
	
	private void makeSessionIdExistInCookies(){
		when(Cookies.getCookie(Constants.COOKIE_SESSION_ID)).thenReturn(SESSION_ID);
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerReturnNotLoggedInUserDTOWithCheckWithServerIsSessionIdStillLegalMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[0];
				callback.onSuccess(makeNotLoggedInUserDTO());
				return null;
			}
		}).when(loginService).isSessionIdStillLegal(isA(AsyncCallback.class));
	}
	
	private UserDTO makeNotLoggedInUserDTO(){
		UserDTO dto = new UserDTO();
		dto.setLoggedIn(false);
		
		return dto;
	}
	
	@Test
	public void displaysLoginFillingWithServerErrorIfLoginServerIsNotAvaible(){
		makeLoginServerNotAvaible();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
		verify(view).displayLoginError(Constants.SERVER_NOT_AVAIBLE);
	}
	
	private void makeLoginServerNotAvaible(){
		makeSessionIdExistInCookies();
		makeLoginServerNotAvaibleWithLoginFromSessionServerMethod();
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerNotAvaibleWithLoginFromSessionServerMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[0];
				callback.onFailure(new Throwable());
				return null;
			}
		}).when(loginService).isSessionIdStillLegal(isA(AsyncCallback.class));
	}
	
	@Test
	public void displaysLoginFillingWithServerErrorIfSessionIsActiveButGreetingServiceIsNotAvaible(){
		makeSessionActiveOnLoginServerWithLoginFromSessionMethod();
		makeGreetingServiceNotAvaible();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
		verify(view).displayLoginError(Constants.SERVER_NOT_AVAIBLE);
	}
	
	private void makeSessionActiveOnLoginServerWithLoginFromSessionMethod(){
		makeSessionIdExistInCookies();
		makeLoginServerReturnLoggedInUserDTOWithCheckWithServerIsSessionIdStillLegalMethod();
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerReturnLoggedInUserDTOWithCheckWithServerIsSessionIdStillLegalMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[0];
				callback.onSuccess(makeLoggedInUserDTO());
				return null;
			}
		}).when(loginService).isSessionIdStillLegal(isA(AsyncCallback.class));
	}
	
	private UserDTO makeLoggedInUserDTO(){
		UserDTO user = new UserDTO();
		user.setUserName(USERNAME);
		user.setUserGreeting(GREETING);
		user.setSessionId(SESSION_ID);
		user.setLoggedIn(true);
		
		return user;
	}
	
	@SuppressWarnings("unchecked")
	private void makeGreetingServiceNotAvaible(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<String> callback = (AsyncCallback<String>) invocation.getArguments()[2];
				callback.onFailure(new Throwable());
				return null;
			}
		}).when(greetingService).getGreetingForTime(isA(Date.class), eq(SESSION_ID), isA(AsyncCallback.class));
	}
	
	@Test
	public void displaysGreetingFillingIfSessionLegalOnServerAndGreetingServerAvaible(){
		makeSessionActiveOnLoginServerWithLoginFromSessionMethod();
		makeGreetingServiceAvaible();
		
		presenter.showPage();
		
		verify(view).displayGreetingFilling(anyString());
	}
	
	@SuppressWarnings("unchecked")
	private void makeGreetingServiceAvaible(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<String> callback = (AsyncCallback<String>) invocation.getArguments()[2];
				callback.onSuccess(GREETING);
				return null;
			}
		}).when(greetingService).getGreetingForTime(isA(Date.class), eq(SESSION_ID), isA(AsyncCallback.class));
	}
	
	@Test
	public void createsExpectedUserGreeting(){
		String expectedGreeting = GREETING + ", " + USERNAME + ".";
		
		makeShowPageMethodShowGreetingFillingWithoutErrors();
		
		verify(view).displayGreetingFilling(expectedGreeting);
	}
	
	private void makeShowPageMethodShowGreetingFillingWithoutErrors(){
		makeSessionIdExistInCookies();
		makeLoginServerReturnLoggedInUserDTOWithCheckWithServerIsSessionIdStillLegalMethod();
		makeGreetingServiceAvaible();
		presenter.showPage();
	}
	
	@Test
	public void showPageMethodShowsPage(){
		presenter.showPage();
		
		verify(view).displayPage();
	}
	
	@Test
	public void erasesErrorsAndFieldsAndHidesGreetingFillingBeforeDisplaysLoginFilling(){
		makeShowPageMethodShowLoginFillingWithoutErrors();
		
		InOrder inOrder = inOrder(view);
		inOrder.verify(view).hideGreetingFilling();
		inOrder.verify(view).eraseLoginAndPassword();
		inOrder.verify(view).eraseLoginFillingErrorsFields();
		inOrder.verify(view).displayLoginFilling();
		inOrder.verifyNoMoreInteractions();
	}
	
	private void makeShowPageMethodShowLoginFillingWithoutErrors(){
		makeSessionIdNotExistInCookies();
		presenter.showPage();
	}
	
	@Test
	public void erasesErrorFieldAndHidesLoginFllingBeforeDisplayGreetingFilling(){
		makeShowPageMethodShowGreetingFillingWithoutErrors();
		
		InOrder inOrder = inOrder(view);
		inOrder.verify(view).hideLoginFilling();
		inOrder.verify(view).eraseGreetingFillingErrorField();
		inOrder.verify(view).displayGreetingFilling(anyString());
		inOrder.verifyNoMoreInteractions();
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServiceNotAvaibleWithLoginServerMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[2];
				callback.onFailure(new Throwable());
				return null;
			}
		}).when(loginService).loginServer(anyString(), anyString(), isA(AsyncCallback.class));
	}
	
	@Test
	public void displaysServerErrorIfLoginServerIsNotAvaibleWithLoginServerMethodOnLoginFilling(){
		makeLoginServiceNotAvaibleWithLoginServerMethod();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(view).displayLoginError(Constants.SERVER_NOT_AVAIBLE);
	}
	
	@Test
	public void displaysCredentialsErrorIfLoginAndPasswordAreNotCorrectInLoggingFilling(){
		makeLoginServiceReturnsNotLoggedInUserDTOWithLoginWithServerMethod();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(view).displayLoginError(Constants.INCORRECT_CREDENTIALS);
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServiceReturnsNotLoggedInUserDTOWithLoginWithServerMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[2];
				callback.onSuccess(makeNotLoggedInUserDTO());
				return null;
			}
		}).when(loginService).loginServer(anyString(), anyString(), isA(AsyncCallback.class));
	}
	
	@Test
	public void displaysServerErrorIfLoginAndPasswordCorrectButGreetingServerIsUnavaibleInLOginFilling(){
		makeLoginServiceReturnsLoggedInUserDTOWithLoginWithServerMethod();
		makeGreetingServiceNotAvaible();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(view).displayLoginError(Constants.SERVER_NOT_AVAIBLE);
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServiceReturnsLoggedInUserDTOWithLoginWithServerMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[2];
				callback.onSuccess(makeLoggedInUserDTO());
				return null;
			}
		}).when(loginService).loginServer(anyString(), anyString(), isA(AsyncCallback.class));
	}
	
	@Test
	public void displaysLoginOrPasswordTooShortMessageIfLoginTooShort(){
		makeLoginServiceReturnsLoggedInUserDTOWithLoginWithServerMethod();
		makeGreetingServiceAvaible();
		
		presenter.logIn(TOO_SHORT_LOGIN, PASSWORD);
		
		verify(view).displayLoginError(Constants.LOGIN_OR_PASSWORD_TOO_SHORT);
	}
	
	@Test
	public void displaysLoginOrPasswordTooShortMessageIfPasswordTooShort(){
		makeLoginServiceReturnsLoggedInUserDTOWithLoginWithServerMethod();
		makeGreetingServiceAvaible();
		
		presenter.logIn(LOGIN, TOO_SHORT_PASSWORD);
		
		verify(view).displayLoginError(Constants.LOGIN_OR_PASSWORD_TOO_SHORT);
	}
	
	@Test
	public void displaysGreetingFillingWithoutErrorIfLoginAndPasswordCorrectAndGreetingServerIsAvaible(){
		makeLoginServiceReturnsLoggedInUserDTOWithLoginWithServerMethod();
		makeGreetingServiceAvaible();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(view).displayGreetingFilling(anyString());
		verify(view, never()).displayLoginError(anyString());
	}
	
	@Test
	public void displaysServerErrorIfLoggingServerNotAvaibleForExecuteLogoutMethodInGreetingFilling(){
		makeLoginServiceUnavaibleForExecuteLogoutMethod();
		
		presenter.executeLogout();
		
		verify(view).displayGreetingFilling(Constants.SERVER_NOT_AVAIBLE);
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServiceUnavaibleForExecuteLogoutMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<Void> callback = (AsyncCallback<Void>) invocation.getArguments()[0];
				callback.onFailure(new Throwable());
				return null;
			}
		}).when(loginService).logout(isA(AsyncCallback.class));
	}

	@Test
	public void displaysLoggingFilingWithoutErrorIfLoginServerExecutedLogoutRequest(){
		makeLoginServiceExecuteLogoutRequest();
		
		presenter.executeLogout();
		
		verify(view).displayLoginFilling();
		verify(view, never()).displayLoginError(anyString());
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServiceExecuteLogoutRequest(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<Void> callback = (AsyncCallback<Void>) invocation.getArguments()[0];
				callback.onSuccess(null);
				return null;
			}
		}).when(loginService).logout(isA(AsyncCallback.class));
	}
}
