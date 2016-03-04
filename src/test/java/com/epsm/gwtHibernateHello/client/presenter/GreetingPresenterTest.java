package com.epsm.gwtHibernateHello.client.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

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

import com.epsm.gwtHibernateHello.client.event.LogoutEvent;
import com.epsm.gwtHibernateHello.client.service.GreetingServiceAsync;
import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.ContentContainer;
import com.epsm.gwtHibernateHello.client.view.ErrorMessages;
import com.epsm.gwtHibernateHello.client.view.GreetingView;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Cookies.class, DateTimeFormat.class})
public class GreetingPresenterTest {
	private LoginServiceAsync loginService;
	private GreetingServiceAsync greetingService;
	private GreetingView view;
	private ErrorMessages messages;
	private HandlerManager eventBus;
	private GreetingPresenter presenter;
	private ContentContainer container;
	private DateTimeFormat formatter;
	private final String TOKEN = "someToken";
	private final String GREETING = "Hello";
	private final String MESSAGE_SERVER_UNAVAIBLE = "server_uanavaible";
	private final String TIME = "12-34-56";
	
	@Before
	public void setUp(){
		loginService = mock(LoginServiceAsync.class);
		greetingService = mock(GreetingServiceAsync.class);
		view = mock(GreetingView.class);
		messages = mock(ErrorMessages.class);
		eventBus = spy(new HandlerManager(null));
		PowerMockito.mockStatic(DateTimeFormat.class);
		formatter = mock(DateTimeFormat.class);
		when(DateTimeFormat.getFormat(Constants.TIME_PATTERN)).thenReturn(formatter);
		PowerMockito.mockStatic(Cookies.class);
		presenter = new GreetingPresenter(loginService, greetingService, messages, eventBus, view);
		container = mock(ContentContainer.class);
		
		when(messages.serverUnavaible()).thenReturn(MESSAGE_SERVER_UNAVAIBLE);
		when(formatter.format(isA(Date.class))).thenReturn(TIME);
		when(Cookies.getCookie(Constants.COOKIE_TOKEN)).thenReturn(TOKEN);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfGreetingServiceAsyncIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: GreetingServiceAsync can't be null.");
	    
	    new GreetingPresenter(loginService, null, messages, eventBus, view);
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
	@SuppressWarnings("unchecked")
	public void showPageMethodTriesToGetGreetingFromGreetingServerWithRightParameters(){
		presenter.showPage(container);
		
		verify(greetingService).getGreeting(eq(TIME), eq(TOKEN), isA(AsyncCallback.class));
	}
	
	@Test
	public void showsGreetingIfGreetingServerAnsweres(){
		makeGreetingServiceToAnswer();
		
		presenter.showPage(container);
		
		verify(view).displayGreeting(GREETING);
	}
	
	@SuppressWarnings("unchecked")
	private void makeGreetingServiceToAnswer(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<String> callback = (AsyncCallback<String>) invocation.getArguments()[2];
				callback.onSuccess(GREETING);
				return null;
			}
		}).when(greetingService).getGreeting(eq(TIME), eq(TOKEN), isA(AsyncCallback.class));
	}
	
	@Test
	public void showsServerUnavaibleMessageIfGreetingServerNotAnsweres(){
		makeGreetingServiceNotToAnswer();
		
		presenter.showPage(container);
		
		verify(view).displayError(MESSAGE_SERVER_UNAVAIBLE);
	}
	
	@SuppressWarnings("unchecked")
	private void makeGreetingServiceNotToAnswer(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<String> callback = (AsyncCallback<String>) invocation.getArguments()[2];
				callback.onFailure(new Throwable());
				return null;
			}
		}).when(greetingService).getGreeting(eq(TIME), eq(TOKEN), isA(AsyncCallback.class));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void executeLogoutMethodMakesRequestToLoginServiceWithRightParameters(){
		presenter.executeLogout();
		
		verify(loginService).logout(eq(TOKEN), isA(AsyncCallback.class));
	}
	
	@Test
	public void reportsIfLoginServerPerformedLogoutRequest(){
		makeLoginServiceExecuteLogoutRequest();
		
		presenter.executeLogout();
		
		verify(eventBus).fireEvent(isA(LogoutEvent.class));
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
	public void displayServerNotAvaibleMessageIfServerDoesNotPerformedLogoutRequest(){
		makeLoginServiceNotToExecuteLogoutRequest();
		
		presenter.executeLogout();
		
		verify(view).displayError(MESSAGE_SERVER_UNAVAIBLE);
	}
	
	@SuppressWarnings("unchecked")
	private void makeLoginServiceNotToExecuteLogoutRequest(){
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				AsyncCallback<Void> callback = (AsyncCallback<Void>) invocation.getArguments()[1];
				callback.onFailure(new Throwable());
				return null;
			}
		}).when(loginService).logout(anyString(), isA(AsyncCallback.class));
	}
}
