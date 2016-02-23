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
	private final String TOKEN = "someToken";
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
	public void showPageMethodDoesNotTryCheckWithServerIfSessionStillLegalIfThereIsNoInCookies(){
		makeTokenNotExistInCookies();
		
		presenter.showPage();
		
		verify(loginService, never()).isSessionStillLegal(eq(TOKEN), isA(AsyncCallback.class));
	}
	
	private void makeTokenNotExistInCookies(){
		when(Cookies.getCookie(Constants.COOKIE_TOKEN)).thenReturn(null);
	}
	
	@Test
	public void displaysLoginFillingWithoutErrorIfTokenNotExistsInCookies(){
		makeTokenNotExistInCookies();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
		verify(view, never()).displayLoginError(anyString());
	}
	
	@Test
	public void displaysLoginFillingWithoutErrorIfSessionNotLegalOnLoginServer(){
		makeTokenNotLegalOnLoginServer();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
		verify(view, never()).displayLoginError(anyString());
	}
	
	private void makeTokenNotLegalOnLoginServer(){
		makeTokenExistInCookies();
		makeLoginServerReturnNotLoggedInUserDTOWithCheckWithServerIsSessionStillLegalMethod();
	}
	
	private void makeTokenExistInCookies(){
		when(Cookies.getCookie(Constants.COOKIE_TOKEN)).thenReturn(TOKEN);
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerReturnNotLoggedInUserDTOWithCheckWithServerIsSessionStillLegalMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[1];
				callback.onSuccess(makeNotLoggedInUserDTO());
				return null;
			}
		}).when(loginService).isSessionStillLegal(anyString(), isA(AsyncCallback.class));
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
		makeTokenExistInCookies();
		makeLoginServerNotAvaibleWithIsSessionStillLegalMethod();
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerNotAvaibleWithIsSessionStillLegalMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[1];
				callback.onFailure(new Throwable());
				return null;
			}
		}).when(loginService).isSessionStillLegal(anyString(), isA(AsyncCallback.class));
	}
	
	@Test
	public void displaysLoginFillingWithServerErrorIfSessionIsLegalButGreetingServiceIsNotAvaible(){
		makeTokenLegalOnLoginServerWithIsSessionStillLegalMethod();
		makeGreetingServiceNotAvaible();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
		verify(view).displayLoginError(Constants.SERVER_NOT_AVAIBLE);
	}
	
	private void makeTokenLegalOnLoginServerWithIsSessionStillLegalMethod(){
		makeTokenExistInCookies();
		makeLoginServerReturnLoggedInUserDTOWithCheckWithIsSessionStillLegalMethod();
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerReturnLoggedInUserDTOWithCheckWithIsSessionStillLegalMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[1];
				callback.onSuccess(makeLoggedInUserDTO());
				return null;
			}
		}).when(loginService).isSessionStillLegal(anyString(), isA(AsyncCallback.class));
	}
	
	private UserDTO makeLoggedInUserDTO(){
		UserDTO user = new UserDTO();
		user.setName(USERNAME);
		user.setToken(TOKEN);
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
		}).when(greetingService).getGreeting(isA(Date.class), eq(TOKEN), isA(AsyncCallback.class));
	}
	
	@Test
	public void displaysGreetingFillingIfSessionLegalOnServerAndGreetingServerAvaible(){
		makeTokenLegalOnLoginServerWithIsSessionStillLegalMethod();
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
		}).when(greetingService).getGreeting(isA(Date.class), eq(TOKEN), isA(AsyncCallback.class));
	}
	
	@Test
	public void createsExpectedUserGreeting(){
		String expectedGreeting = GREETING + ", " + USERNAME + ".";
		
		makeShowPageMethodDisplayGreetingFillingWithoutErrors();
		
		verify(view).displayGreetingFilling(expectedGreeting);
	}
	
	private void makeShowPageMethodDisplayGreetingFillingWithoutErrors(){
		makeTokenExistInCookies();
		makeLoginServerReturnLoggedInUserDTOWithCheckWithIsSessionStillLegalMethod();
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
		makeTokenNotExistInCookies();
		presenter.showPage();
	}
	
	@Test
	public void erasesErrorFieldAndHidesLoginFllingBeforeDisplayGreetingFilling(){
		makeShowPageMethodDisplayGreetingFillingWithoutErrors();
		
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
		makeLoginServiceReturnLoggedInUserDTOWithLoginWithServerMethod();
		makeGreetingServiceNotAvaible();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(view).displayLoginError(Constants.SERVER_NOT_AVAIBLE);
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServiceReturnLoggedInUserDTOWithLoginWithServerMethod(){
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
		makeLoginServiceReturnLoggedInUserDTOWithLoginWithServerMethod();
		makeGreetingServiceAvaible();
		
		presenter.logIn(TOO_SHORT_LOGIN, PASSWORD);
		
		verify(view).displayLoginError(Constants.LOGIN_OR_PASSWORD_TOO_SHORT);
	}
	
	@Test
	public void displaysLoginOrPasswordTooShortMessageIfPasswordTooShort(){
		makeLoginServiceReturnLoggedInUserDTOWithLoginWithServerMethod();
		makeGreetingServiceAvaible();
		
		presenter.logIn(LOGIN, TOO_SHORT_PASSWORD);
		
		verify(view).displayLoginError(Constants.LOGIN_OR_PASSWORD_TOO_SHORT);
	}
	
	@Test
	public void displaysGreetingFillingWithoutErrorIfLoginAndPasswordCorrectAndGreetingServerIsAvaible(){
		makeLoginServiceReturnLoggedInUserDTOWithLoginWithServerMethod();
		makeGreetingServiceAvaible();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(view).displayGreetingFilling(anyString());
		verify(view, never()).displayLoginError(anyString());
	}
	
	@Test
	public void displaysServerErrorIfLoggingServerNotAvaibleForExecuteLogoutMethodInGreetingFilling(){
		makeLoginServiceUnavaibleForExecuteLogoutMethod();
		
		presenter.executeLogout();
		
		verify(view).displayLogoutError(Constants.SERVER_NOT_AVAIBLE);
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServiceUnavaibleForExecuteLogoutMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<Void> callback = (AsyncCallback<Void>) invocation.getArguments()[1];
				callback.onFailure(new Throwable());
				return null;
			}
		}).when(loginService).logout(anyString(), isA(AsyncCallback.class));
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
				AsyncCallback<Void> callback = (AsyncCallback<Void>) invocation.getArguments()[1];
				callback.onSuccess(null);
				return null;
			}
		}).when(loginService).logout(anyString(), isA(AsyncCallback.class));
	}
	
	@Test
	public void refreshesCookieAfterSuccessfullLogin(){
		makeLoginServiceReturnLoggedInUserDTOWithLoginWithServerMethod();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		PowerMockito.verifyStatic();
		Cookies.setCookie(eq(Constants.COOKIE_TOKEN), eq(TOKEN), isA(Date.class), 
				anyString(), eq("/"), eq(false));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void storesTokenAfterSuccessfullLoginAndUseItWhereCallsGetGreetingForTime(){
		makeLoginServiceReturnLoggedInUserDTOWithLoginWithServerMethod();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(greetingService).getGreeting(isA(Date.class), eq(TOKEN),
				isA(AsyncCallback.class));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void storesTokenAfterSuccessfullLoginAndUseItWhereCallsLogout(){
		makeLoginServiceReturnLoggedInUserDTOWithLoginWithServerMethod();
		
		presenter.logIn(LOGIN, PASSWORD);
		presenter.executeLogout();
		
		verify(loginService).logout(eq(TOKEN), isA(AsyncCallback.class));
	}
}
