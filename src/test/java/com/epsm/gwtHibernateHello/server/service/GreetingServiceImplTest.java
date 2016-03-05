package com.epsm.gwtHibernateHello.server.service;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.epsm.hello.configutation.Configurator;
import com.epsm.hello.model.Message;
import com.epsm.hello.model.MessageFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Locale.class, Configurator.class})
public class GreetingServiceImplTest {
	private MessageFactory messageFactory;
	private GreetingServiceImpl service;
	private String greeting;
	private HttpServletRequest request;
	private HttpSession session;
	private UserDTO userDto;
	private Locale actualLocale;
	private LocalTime actualTime;
	private Message message;
	private ArgumentCaptor<Locale> localeCaptor;
	private ArgumentCaptor<LocalTime> timeCaptor;
	private final Locale LOCALE = PowerMockito.mock(Locale.class);
	private final String RIGHT_TOKEN = "someToken";
	private final String WRONG_TOKEN = "wrongToken";
	private final String USER_NAME = "someName";
	private final String GREETING = "greetingMessage";
	private final String TIME = "12-34-56";
	private final LocalTime EXPECTED_TIME = LocalTime.of(12, 34, 56);
	private final String EXPECTED_GREETING = GREETING + ", " + USER_NAME + ".";
	
	@Before
	public void setUp(){
		messageFactory = mock(MessageFactory.class);
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		localeCaptor = ArgumentCaptor.forClass(Locale.class);
		timeCaptor= ArgumentCaptor.forClass(LocalTime.class);
		userDto = new UserDTO();
		message = mock(Message.class);
		PowerMockito.mockStatic(Configurator.class);
		when(Configurator.getConfiguredFactory(anyString())).thenReturn(messageFactory);
		service = spy(new GreetingServiceImpl());
		PowerMockito.mockStatic(DateTimeFormatter.class);
		
		userDto.setToken(RIGHT_TOKEN);
		userDto.setName(USER_NAME);
		when(service.getRequest()).thenReturn(request);
		when(request.getLocale()).thenReturn(LOCALE);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(userDto);
		when(messageFactory.getMessage(eq(LOCALE), isA(LocalTime.class))).thenReturn(message);
		when(message.toLocalizedString()).thenReturn(GREETING);
	}
	
	@Test
	public void getGreetingForTimeMethodReturnsNullIfTokenIsCorrectAndTimeAsStringIsNull(){
		tryGetGreetingWithNullTimeAsString();
		
		Assert.assertNull(greeting);
	}
	
	private void tryGetGreetingWithNullTimeAsString(){
		greeting = service.getGreeting(null, RIGHT_TOKEN);
	}
	
	@Test
	public void getGreetingForTimeMethodPassesRightLocaleAndLocalTimeToMessageFactory(){
		makeServiceReturnGreeting();
		
		captureParameters();
		
		Assert.assertEquals(EXPECTED_TIME, actualTime);
		Assert.assertTrue(LOCALE == actualLocale);
	}
	
	private void makeServiceReturnGreeting(){
		greeting = service.getGreeting(TIME, RIGHT_TOKEN);
	}
	
	private void captureParameters(){
		verify(messageFactory).getMessage(localeCaptor.capture(), timeCaptor.capture());
		actualTime = timeCaptor.getValue();
		actualLocale = localeCaptor.getValue();
	}
	
	@Test
	public void getGreetingForTimeMethodMakesExpectedGreeting(){
		makeServiceReturnGreeting();
		
		Assert.assertEquals(EXPECTED_GREETING, greeting);
	}
	
	@Test
	public void getGreetingForTimeMethodReturnsNullIfTokenWrong(){		
		tryToGetGreetingWithWrongToken();
		
		Assert.assertNull(greeting);
	}
	
	private void tryToGetGreetingWithWrongToken(){
		greeting = service.getGreeting(TIME, WRONG_TOKEN);
	}
}
