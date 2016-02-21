package com.epsm.gwtHibernateHello.server.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.client.service.GreetingService;
import com.epsm.hello.model.Message;
import com.epsm.hello.model.MessageFactory;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends TokenVerifier implements GreetingService {
	private MessageFactory messageFactory;
	private Logger logger;
	
	public GreetingServiceImpl(MessageFactory messageFactory) {
		logger = LoggerFactory.getLogger(GreetingServiceImpl.class);
		
		if(messageFactory == null){
			String message = "Constructor: MessageFactory can't be null.";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		
		this.messageFactory = messageFactory;
	}
	
	@Override
	public String getGreetingForTime(Date timeSource, String token) {
		if(timeSource == null){
			logger.warn("Attempt: get greeting with null Date from: {}.", getRemoteAddr());
			return null;
		}else if(isTokenCorrect(token)){
			logger.info("Prepearing: greeting for user from: {}.", getRemoteAddr());
			return createMessage(timeSource);
		}else{
			logger.warn("Denied: getting greeting with wrong token from: {}.", getRemoteAddr());
			return null;
		}
	}
	
	private String createMessage(Date timeSource){
		Locale locale = getRequestLocale();
		LocalTime time = getRequestTime(timeSource);
		Message message = messageFactory.getMessage(time, locale);
		String greetingMessage = message.toLocalizedString();
		logger.debug("Invoked: createMessage(...) for Date:{}, Locale: {}, returned {}.",
				timeSource, locale, greetingMessage);
		
		return greetingMessage;
	}

	private Locale getRequestLocale(){
		HttpServletRequest request = getRequest();
		return request.getLocale();
	}
	
	private LocalTime getRequestTime(Date timeSource){
		Instant instant = Instant.ofEpochMilli(timeSource.getTime());
		
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
	}
}