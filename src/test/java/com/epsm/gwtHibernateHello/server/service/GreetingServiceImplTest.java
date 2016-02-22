package com.epsm.gwtHibernateHello.server.service;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
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

import com.epsm.gwtHibernateHello.server.configuration.ConfigurationServlet;
import com.epsm.gwtHibernateHello.shared.UserDTO;
import com.epsm.hello.model.Message;
import com.epsm.hello.model.MessageFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Locale.class, ConfigurationServlet.class})
public class GreetingServiceImplTest {
	private MessageFactory messageFactory;
	private GreetingServiceImpl service;
	private String greeting;
	private HttpServletRequest request;
	private HttpSession session;
	private UserDTO userDto;
	private Locale actualLocale;
	private LocalTime actualTime;
	private LocalTime expextedTime;
	private Message message;
	private ArgumentCaptor<Locale> localeCaptor;
	private ArgumentCaptor<LocalTime> timeCaptor;
	private final Locale LOCALE = PowerMockito.mock(Locale.class);
	private final String RIGHT_TOKEN = "someToken";
	private final String WRONG_TOKEN = "wrongToken";
	private final String GREETING = "greetingMessage";
	private final Date TIME_SOURCE = new Date();
	
	@Before
	public void setUp(){
		messageFactory = mock(MessageFactory.class);
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		localeCaptor = ArgumentCaptor.forClass(Locale.class);
		timeCaptor= ArgumentCaptor.forClass(LocalTime.class);
		userDto = new UserDTO();
		message = mock(Message.class);
		PowerMockito.mockStatic(ConfigurationServlet.class);
		when(ConfigurationServlet.getMesageFactory()).thenReturn(messageFactory);
		service = spy(new GreetingServiceImpl());
		
		userDto.setToken(RIGHT_TOKEN);
		when(service.getRequest()).thenReturn(request);
		when(request.getLocale()).thenReturn(LOCALE);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(userDto);
		when(messageFactory.getMessage(eq(LOCALE), isA(LocalTime.class))).thenReturn(message);
		when(message.toLocalizedString()).thenReturn(GREETING);
	}
	
	@Test
	public void getGreetingForTimeMethodReturnsNullIfDateIsNull(){
		tryGetGreetingWithNullDate();
		
		Assert.assertNull(greeting);
	}
	
	private void tryGetGreetingWithNullDate(){
		greeting = service.getGreetingForTime(null, RIGHT_TOKEN);
	}
	
	@Test
	public void getGreetingForTimeMethodReturnsNullIfTokenWrong(){		
		tryToGetGreetingWithWrongToken();
		
		Assert.assertNull(greeting);
	}
	
	private void tryToGetGreetingWithWrongToken(){
		greeting = service.getGreetingForTime(TIME_SOURCE, WRONG_TOKEN);
	}
	
	@Test
	public void getGreetingForTimeMethodPassesRightLocaleAndLocalTimeToMessageFactory(){
		makeServiceReturnGreeting();
		
		captureParameters();
		calculateExpectedTime(TIME_SOURCE);
		
		Assert.assertEquals(expextedTime, actualTime);
		Assert.assertTrue(LOCALE == actualLocale);
	}
	
	private void makeServiceReturnGreeting(){
		greeting = service.getGreetingForTime(TIME_SOURCE, RIGHT_TOKEN);
	}
	
	private void captureParameters(){
		verify(messageFactory).getMessage(localeCaptor.capture(), timeCaptor.capture());
		actualTime = timeCaptor.getValue();
		actualLocale = localeCaptor.getValue();
	}
	
	private void calculateExpectedTime(Date timeSource){
		Instant instant = Instant.ofEpochMilli(timeSource.getTime());
		expextedTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
	}
	
	@Test
	public void getGreetingForTimeMethodReturnsTheSameMessageThatObtainFromFactory(){
		makeServiceReturnGreeting();
		
		Assert.assertEquals(GREETING, greeting);
	}
}
