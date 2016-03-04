package com.epsm.gwtHibernateHello.client.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.epsm.gwtHibernateHello.client.event.LoginEvent;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.ContentContainer;
import com.epsm.gwtHibernateHello.client.view.ErrorMessages;
import com.epsm.gwtHibernateHello.client.view.LoginView;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cookies.class)
public class LoginPresenterTest {
	private LoginServiceAsync loginService;
	private LoginView view;
	private ErrorMessages messages;
	private HandlerManager eventBus;
	private LoginPresenter presenter;
	private ContentContainer container;
	private final String TOKEN = "someToken";
	private final String LOGIN = "someLogin";
	private final String PASSWORD = "somePassword";
	private final String TOO_SHORT_PASSWORD = "123";
	private final String TOO_SHORT_LOGIN = "abc";
	private final String MESSAGE_SERVER_UNAVAIBLE = "server_uanavaible";
	private final String MESSAGE_TOO_SHORT_LOGIN_OR_PASSSWORD = "login_or_password_too_short";
	private final String MESSAGE_WRONG_LOGIN_OR_PASSSWORD = "wrong_login_or_password";
	
	@Before
	public void setUp(){
		loginService = mock(LoginServiceAsync.class);
		view = mock(LoginView.class);
		messages = mock(ErrorMessages.class);
		eventBus = spy(new HandlerManager(null));
		PowerMockito.mockStatic(DateTimeFormat.class);
		PowerMockito.mockStatic(Cookies.class);
		presenter = new LoginPresenter(loginService, messages, eventBus, view);
		container = mock(ContentContainer.class);
		
		when(messages.serverUnavaible()).thenReturn(MESSAGE_SERVER_UNAVAIBLE);
		when(messages.tooShortLoginOrPassword(anyInt())).thenReturn(MESSAGE_TOO_SHORT_LOGIN_OR_PASSSWORD);
		when(messages.wrongLoginOrPassword()).thenReturn(MESSAGE_WRONG_LOGIN_OR_PASSSWORD);
	}

	@Test
	public void showPageMethodTriesToPassViewAsCompositeToContentContainer(){
		presenter.showPage(container);
		
		verify(container).setContent(any());//any() because I can't mock Composite
	}
	
	@Test
	public void showPageMethodTriesToResetViewState(){
		presenter.showPage(container);
		
		verify(view).resetState();
	}
	
	@Test
	public void logInMethodDisplaysLoginOrPasswordTooShortMessageIfLoginTooShort(){
		presenter.logIn(TOO_SHORT_LOGIN, PASSWORD);
		
		verify(view).displayError(MESSAGE_TOO_SHORT_LOGIN_OR_PASSSWORD);
	}
	
	@Test
	public void logInMethodDoesNotTryToLoginWithLoginServiceIfLoginTooShort(){
		presenter.logIn(TOO_SHORT_LOGIN, PASSWORD);
		
		verifyNoMoreInteractions(loginService);
	}
	
	@Test
	public void logInMethodDisplaysLoginOrPasswordTooShortMessageIfPasswordTooShort(){
		presenter.logIn(LOGIN, TOO_SHORT_PASSWORD);
		
		verify(view).displayError(MESSAGE_TOO_SHORT_LOGIN_OR_PASSSWORD);
	}
	
	@Test
	public void logInMethodDoesNotTryToLoginWithLoginServiceIfPasswordTooShort(){
		presenter.logIn(LOGIN, TOO_SHORT_PASSWORD);
		
		verifyNoMoreInteractions(loginService);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void logInMethodTriesToLoginWithLoginServerWithRightParametersIfLoginAndPasswordAreNotShort(){
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(loginService).loginServer(eq(LOGIN), eq(PASSWORD), isA(AsyncCallback.class));
	}
	
	@Test
	public void logInMethodRefreshesCookieAfterSuccessfullLogin(){
		makeLoginServiceReturnLoggedInUserDTOWithLoginWithServerMethod();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		PowerMockito.verifyStatic();
		Cookies.setCookie(eq(Constants.COOKIE_TOKEN), eq(TOKEN), isA(Date.class), 
				anyString(), eq("/"), eq(false));
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
	
	private UserDTO makeLoggedInUserDTO(){
		UserDTO user = new UserDTO();
		user.setToken(TOKEN);
		user.setLoggedIn(true);
		
		return user;
	}
	
	@Test
	public void logInMethodfiresLoginEventIfLoginServerReturnsLoggedInUserDTO(){
		makeLoginServiceReturnLoggedInUserDTOWithLoginWithServerMethod();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(eventBus).fireEvent(isA(LoginEvent.class));
	}
	
	@Test
	public void displaysCredentialsErrorIfLoginServiceRetunedNotLoggedInUserDTO(){
		makeLoginServiceReturnsNotLoggedInUserDTOWithLoginWithServerMethod();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(view).displayError(MESSAGE_WRONG_LOGIN_OR_PASSSWORD);
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
	
	private UserDTO makeNotLoggedInUserDTO(){
		UserDTO dto = new UserDTO();
		dto.setLoggedIn(false);
		
		return dto;
	}
	
	@Test
	public void displaysServerErrorIfLoginServerIsNotAvaibleWithLoginServerMethod(){
		makeLoginServiceNotAvaibleWithLoginServerMethod();
		
		presenter.logIn(LOGIN, PASSWORD);
		
		verify(view).displayError(MESSAGE_SERVER_UNAVAIBLE);
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
}
