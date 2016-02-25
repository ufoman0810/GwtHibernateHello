package com.epsm.gwtHibernateHello.server.service;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.client.service.GreetingService;
import com.epsm.gwtHibernateHello.server.configuration.Configuration;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.epsm.hello.model.Message;
import com.epsm.hello.model.MessageFactory;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends ServiceUtils implements GreetingService {
	private MessageFactory messageFactory;
	private DateTimeFormatter formatter;
	private Logger logger;
	
	public GreetingServiceImpl(){
		logger = LoggerFactory.getLogger(GreetingServiceImpl.class);
		messageFactory = Configuration.getMesageFactory();
		formatter = DateTimeFormatter.ofPattern(Constants.TIME_PATTERN);
		logger.info("Created: GreetingServiceImpl.");
	}
	
	@Override
	public String getGreeting(String timeAsString, String token) {
		if(timeAsString == null){
			logger.warn("Attempt: get greeting with null time from: {}.", getRemoteAddr());
			return null;
		}else if(isTokenCorrect(token)){
			logger.info("Prepearing: greeting for user with login: {}, time: {}, from: {}.",
					getUserLogin(), timeAsString, getRemoteAddr());
			return createMessage(timeAsString);
		}else{
			logger.warn("Denied: getting greeting with wrong token from: {}.", getRemoteAddr());
			return null;
		}
	}
	
	private String createMessage(String timeAsString){
		Locale locale = getRequestLocale();
		LocalTime time = LocalTime.parse(timeAsString, formatter);
		Message	message = messageFactory.getMessage(locale, time);
		String greetingMessage = message.toLocalizedString();
		logger.debug("Invoked: createMessage(...) for timeAsString: {}, Locale: {}, returned: {}.",
				timeAsString, locale, greetingMessage);

		return greetingMessage;
	}
	
	private Locale getRequestLocale(){
		HttpServletRequest request = getRequest();
		return request.getLocale();
	}
}