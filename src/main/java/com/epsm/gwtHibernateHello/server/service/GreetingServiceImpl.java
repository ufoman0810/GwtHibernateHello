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
import com.epsm.gwtHibernateHello.server.configuration.Configuration;
import com.epsm.hello.model.Message;
import com.epsm.hello.model.MessageFactory;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends ServiceUtils implements GreetingService {
	private MessageFactory messageFactory;
	private Logger logger;
	
	public GreetingServiceImpl(){
		logger = LoggerFactory.getLogger(GreetingServiceImpl.class);
		messageFactory = Configuration.getMesageFactory();
		logger.info("Created: GreetingServiceImpl.");
	}
	
	@Override
	public String getGreeting(Date timeSource, String token) {
		if(timeSource == null){
			logger.warn("Attempt: get greeting with null Date from: {}.", getRemoteAddr());
			return null;
		}else if(isTokenCorrect(token)){
			logger.info("Prepearing: greeting for user with login: {}, date: {}, from: {}.",
					getUserLogin(), timeSource, getRemoteAddr());
			return createMessage(timeSource);
		}else{
			logger.warn("Denied: getting greeting with wrong token from: {}.", getRemoteAddr());
			return null;
		}
	}
	
	private String createMessage(Date timeSource){
		Locale locale = getRequestLocale();
		LocalTime time = getRequestTime(timeSource);
		Message	message = messageFactory.getMessage(locale, time);
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