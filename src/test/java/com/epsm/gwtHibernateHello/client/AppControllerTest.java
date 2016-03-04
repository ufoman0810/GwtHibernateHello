package com.epsm.gwtHibernateHello.client;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.epsm.gwtHibernateHello.client.event.LoginEvent;
import com.epsm.gwtHibernateHello.client.event.LogoutEvent;
import com.epsm.gwtHibernateHello.client.presenter.GreetingPresenter;
import com.epsm.gwtHibernateHello.client.presenter.LoginPresenter;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.ContentContainer;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Cookies.class, DateTimeFormat.class, ContentContainer.class})
public class AppControllerTest {
	private LoginServiceAsync loginService;
	private HandlerManager eventBus;
	private LoginPresenter loginPresenter;
	private GreetingPresenter greetingPresenter;
	private ContentContainer container;
	private AppController controller;
	private final String TOKEN = "someToken";
	
	@Before
	public void setUp(){
		loginService = mock(LoginServiceAsync.class);
		eventBus = spy(new HandlerManager(null));
		loginPresenter = mock(LoginPresenter.class);
		greetingPresenter = mock(GreetingPresenter.class);
		PowerMockito.mockStatic(Cookies.class);
		PowerMockito.mockStatic(ContentContainer.class);
		container = mock(ContentContainer.class);
		when(ContentContainer.getInstance()).thenReturn(container);
		controller = new AppController(loginService, eventBus, loginPresenter, greetingPresenter);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void exceptionInConstructorIfLoginServiceAsyncIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: LoginServiceAsync can't be null.");
	    
	    new AppController(null, eventBus, loginPresenter, greetingPresenter);
	}
	
	@Test
	public void exceptionInConstructorIfHandlerManagerIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: HandlerManager can't be null.");
	    
	    new AppController(loginService, null, loginPresenter, greetingPresenter);
	}
	
	@Test
	public void exceptionInConstructorIfErrorLoginPresenterIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: LoginPresenter can't be null.");
	    
	    new AppController(loginService, eventBus, null, greetingPresenter);
	}
	
	@Test
	public void exceptionInConstructorIfGreetingPresenterIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: GreetingPresenter can't be null.");
	    
	    new AppController(loginService, eventBus, loginPresenter, null);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void showPageMethodDoesNotTryCheckWithServerIfSessionStillLegalIfThereIsNoInCookies(){
		makeTokenNotExistInCookies();
		
		controller.showPage();
		
		verify(loginService, never()).isSessionStillLegal(eq(TOKEN), isA(AsyncCallback.class));
	}
	
	private void makeTokenNotExistInCookies(){
		when(Cookies.getCookie(Constants.COOKIE_TOKEN)).thenReturn(null);
	}
	
	@Test
	public void showPageMethodDisplaysGreetingIfSessionIsLegal(){
		makeSessionLegal();
		
		controller.showPage();
		
		verify(greetingPresenter).showPage(container);
	}
	
	private void makeSessionLegal(){
		makeTokenExistInCookies();
		makeLoginServerReturnLoggedInUserDTOWithCheckWithIsSessionStillLegalMethod();
	}
	
	private void makeTokenExistInCookies(){
		when(Cookies.getCookie(Constants.COOKIE_TOKEN)).thenReturn(TOKEN);
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
		user.setToken(TOKEN);
		user.setLoggedIn(true);
		
		return user;
	}
	
	@Test
	public void showPageMethodDisplaysLoginIfSessionIsNotLegal(){
		makeSessionNotLegal();
		
		controller.showPage();
		
		verify(loginPresenter).showPage(container);
	}
	
	private void makeSessionNotLegal(){
		makeTokenExistInCookies();
		makeLoginServerReturnNotLoggedInUserDTOWithCheckWithIsSessionStillLegalMethod();
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServerReturnNotLoggedInUserDTOWithCheckWithIsSessionStillLegalMethod(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[1];
				callback.onSuccess(makeNotLoggedInUserDTO());
				return null;
			}
		}).when(loginService).isSessionStillLegal(anyString(), isA(AsyncCallback.class));
	}
	
	private UserDTO makeNotLoggedInUserDTO(){
		UserDTO user = new UserDTO();
		user.setLoggedIn(false);
		
		return user;
	}
	
	@Test
	public void showPageMethodDisplaysLoginIfLoginServerNotAnsveres(){
		makeServerNotAnsveres();
		
		controller.showPage();
		
		verify(loginPresenter).showPage(container);
	}
	
	private void makeServerNotAnsveres(){
		makeTokenExistInCookies();
		makeServerNotAnsveresOnSessionLegalityRequest();
	}

	@SuppressWarnings("unchecked")
	private void makeServerNotAnsveresOnSessionLegalityRequest(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<UserDTO> callback = (AsyncCallback<UserDTO>) invocation.getArguments()[1];
				callback.onFailure(new Throwable());
				return null;
			}
		}).when(loginService).isSessionStillLegal(anyString(), isA(AsyncCallback.class));
	}
	
	@Test
	public void displaysLoginIfLogoutEventFired(){
		eventBus.fireEvent(new LogoutEvent());
		
		verify(loginPresenter).showPage(container);
	}
	
	@Test
	public void displaysGreetingIfLoginEventFired(){
		eventBus.fireEvent(new LoginEvent());
		
		verify(greetingPresenter).showPage(container);
	}
}