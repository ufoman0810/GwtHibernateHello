package com.epsm.gwtHibernateHello.client.presenter;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

import com.epsm.gwtHibernateHello.client.service.GreetingServiceAsync;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.PageView;
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
	
	@Before
	public void setUp(){
		loginService = mock(LoginServiceAsync.class);
		greetingService = mock(GreetingServiceAsync.class);
		view = mock(PageView.class);
		presenter = new PagePresenter(loginService, greetingService, view);
		PowerMockito.mockStatic(Cookies.class);
	}
	
	@Test
	public void whenShowPageMethodCalledTriesloginFromSessionServerIfSessionIdExistInCookies(){
		makeSessionIdExistInCookies();
		
		presenter.showPage();
		
		verify(loginService).loginFromSessionServer(isA(AsyncCallback.class));
	}
	
	private void makeSessionIdExistInCookies(){
		when(Cookies.getCookie("sessionId")).thenReturn("someId");
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void whenShowPageMethodCalledDoesNotTryloginFromSessionServerIfSessionIdNotExistInCookies(){
		makeSessionIdNotExistInCookies();
		
		presenter.showPage();
		
		verify(loginService, never()).loginFromSessionServer(isA(AsyncCallback.class));
	}
	
	private void makeSessionIdNotExistInCookies(){
		when(Cookies.getCookie("sessionId")).thenReturn(null);
	}
	
	@Test
	public void displaysLoginFillingIfSessionIdNotExistsInCookies(){
		makeSessionIdNotExistInCookies();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
	}
	
	@Test
	public void displaysLoginFillingIfThisIsNewSessionInloginServer(){
		makeSessionNewInLoginServer();
		
		presenter.showPage();
		
		verify(view).displayLoginFilling();
	}
	
	private void makeSessionNewInLoginServer(){
		makeSessionIdExistInCookies();
		makeLoginServerReturnNotLogedInUserDTOWithLoginFromSessionServerMethod();
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerReturnNotLogedInUserDTOWithLoginFromSessionServerMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[0];
				callback.onSuccess(makeNotLogedInUserDTO());
				return null;
			}
		}).when(loginService).loginFromSessionServer(isA(AsyncCallback.class));
	}
	
	private UserDTO makeNotLogedInUserDTO(){
		UserDTO dto = new UserDTO();
		dto.setLoggedIn(false);
		
		return dto;
	}
}
