package com.epsm.gwtHibernateHello.client.presenter;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

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
	private PageView view;
	private PagePresenter presenter;
	private final String USERNAME = "John";
	private final String SESSION_ID = "someSessionId";
	private final String GREETING = "Hello";
	
	@Before
	public void setUp(){
		loginService = mock(LoginServiceAsync.class);
		view = mock(PageView.class);
		presenter = new PagePresenter(loginService, view);
		PowerMockito.mockStatic(Cookies.class);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void showPageMethodDoesNotTryCheckWithServerIfSessionStillLegalIfThereIsNotSessionIdInCookies(){
		makeSessionIdNotExistInCookies();
		
		presenter.showPage();
		
		verify(loginService, never()).isSessionIdStillLegal(eq(SESSION_ID), isA(AsyncCallback.class));
	}
	
	private void makeSessionIdNotExistInCookies(){
		when(Cookies.getCookie(Constants.COOKIE_SESSION_ID)).thenReturn(null);
	}
	
	@Test
	public void displaysLoginFillingIfSessionIdNotExistsInCookies(){
		makeSessionIdNotExistInCookies();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
	}
	
	@Test
	public void displaysLoginFillingIfSessionIsNotLegalOnServer(){
		makeSessionNotLegalOnServer();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
	}
	
	private void makeSessionNotLegalOnServer(){
		makeSessionIdExistInCookies();
		makeLoginServerReturnNotLogedInUserDTOWithCheckWithServerIsSessionIdStillLegalMethod();
	}
	
	private void makeSessionIdExistInCookies(){
		when(Cookies.getCookie(Constants.COOKIE_SESSION_ID)).thenReturn(SESSION_ID);
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerReturnNotLogedInUserDTOWithCheckWithServerIsSessionIdStillLegalMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[1];
				callback.onSuccess(makeNotLogedInUserDTO());
				return null;
			}
		}).when(loginService).isSessionIdStillLegal(eq(SESSION_ID), isA(AsyncCallback.class));
	}
	
	private UserDTO makeNotLogedInUserDTO(){
		UserDTO dto = new UserDTO();
		dto.setLoggedIn(false);
		
		return dto;
	}
	
	@Test
	public void displaysGreetingFillingIfSessionLegalOnServer(){
		makeSessionActiveOnServerWithLoginFromSessionMethod();
		
		presenter.showPage();
		
		verify(view).displayGreetingFilling(anyString());
	}
	
	private void makeSessionActiveOnServerWithLoginFromSessionMethod(){
		makeSessionIdExistInCookies();
		makeLoginServerReturnLogedInUserDTOWithCheckWithServerIsSessionIdStillLegalMethod();
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerReturnLogedInUserDTOWithCheckWithServerIsSessionIdStillLegalMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[1];
				callback.onSuccess(makeLogedInUserDTO());
				return null;
			}
		}).when(loginService).isSessionIdStillLegal(eq(SESSION_ID), isA(AsyncCallback.class));
	}
	
	private UserDTO makeLogedInUserDTO(){
		UserDTO user = new UserDTO();
		user.setUserName(USERNAME);
		user.setUserGreeting(GREETING);
		user.setSessionId(SESSION_ID);
		user.setLoggedIn(true);
		
		return user;
	}
	
	@Test
	public void displaysLoginFillingWithErrorIfServerIsNotAvaible(){
		makeServerNotAvaible();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
		verify(view).displayLoginError(Constants.SERVER_NOT_AVAIBLE);
	}
	
	private void makeServerNotAvaible(){
		makeSessionIdExistInCookies();
		makeLoginServerNotAvaibleWithLoginFromSessionServerMethod();
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerNotAvaibleWithLoginFromSessionServerMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[1];
				callback.onFailure(new Throwable());
				return null;
			}
		}).when(loginService).isSessionIdStillLegal(eq(SESSION_ID), isA(AsyncCallback.class));
	}
	
	@Test
	public void erasesLoginPasswordAndErrorFieldsBeforeDisplayLoginFilling(){
		makeShowPageMethodShowLoginFillingWithoutErrors();
		
		InOrder inOrder = inOrder(view);
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
	public void erasesErrorFieldBeforeDisplayGreetingFilling(){
		makeShowPageMethodShowFreetingFillingWithoutErrors();
		
		InOrder inOrder = inOrder(view);
		inOrder.verify(view).eraseGreetingFillingErrorField();
		inOrder.verify(view).displayGreetingFilling(anyString());
		inOrder.verifyNoMoreInteractions();
	}
	
	private void makeShowPageMethodShowFreetingFillingWithoutErrors(){
		makeSessionIdExistInCookies();
		makeLoginServerReturnLogedInUserDTOWithCheckWithServerIsSessionIdStillLegalMethod();
		presenter.showPage();
	}
	
	@Test
	public void createsExpectedUserGreeting(){
		String expectedGreeting = GREETING + ", " + USERNAME + ".";
		
		makeShowPageMethodShowFreetingFillingWithoutErrors();
		
		verify(view).displayGreetingFilling(expectedGreeting);
	}
}
