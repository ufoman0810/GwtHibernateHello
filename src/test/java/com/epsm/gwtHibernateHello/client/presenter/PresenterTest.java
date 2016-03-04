package com.epsm.gwtHibernateHello.client.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.gwtHibernateHello.client.service.LoginServiceAsync;
import com.epsm.gwtHibernateHello.client.view.ContentContainer;
import com.epsm.gwtHibernateHello.client.view.ErrorMessages;
import com.epsm.gwtHibernateHello.client.view.View;
import com.google.gwt.event.shared.HandlerManager;

public class PresenterTest {
	private LoginServiceAsync loginService = mock(LoginServiceAsync.class);
	private ErrorMessages messages = mock(ErrorMessages.class);
	private HandlerManager eventBus = mock(HandlerManager.class);
	private View view = mock(View.class);
	private Presenter presenter;
	
	private class PresenterImpl extends Presenter{

		public PresenterImpl(LoginServiceAsync loginService, ErrorMessages messages, HandlerManager eventBus,
				View view) {
			super(loginService, messages, eventBus, view);
		}

		@Override
		public void showPage(ContentContainer container) {
		}
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfLoginServiceAsyncIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: LoginServiceAsync can't be null.");
	    
	    new PresenterImpl(null, messages, eventBus, view);
	}
	
	@Test
	public void exceptionInConstructorIfErrorMessagesIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: ErrorMessages can't be null.");
	    
	    new PresenterImpl(loginService, null, eventBus, view);
	}
	
	@Test
	public void exceptionInConstructorIfHandlerManagerIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: HandlerManager can't be null.");
	    
	    new PresenterImpl(loginService, messages, null, view);
	}
	
	@Test
	public void exceptionInConstructorIfViewIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Constructor: View can't be null.");
	    
	    new PresenterImpl(loginService, messages, eventBus, null);
	}
	
	@Test
	public void constructorPassesThisPresenterToView(){
		presenter = new PresenterImpl(loginService, messages, eventBus, view);
		
		verify(view).setPresenter(presenter);
	}
}
