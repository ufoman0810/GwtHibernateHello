package com.epsm.gwtHibernateHello.server.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.client.service.GreetingService;
import com.epsm.gwtHibernateHello.server.configuration.PropertiesExtractor;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.epsm.hello.configutation.Configurator;
import com.epsm.hello.model.Message;
import com.epsm.hello.model.MessageFactory;

@SuppressWarnings("serial")
public class GreetingServiceImpl extends ServiceUtils implements GreetingService {
	private MessageFactory messageFactory;
	private DateTimeFormatter formatter;
	private Logger logger;
	
	public GreetingServiceImpl(){
		logger = LoggerFactory.getLogger(GreetingServiceImpl.class);
		String pathToLocalizations = PropertiesExtractor.getProperty(Constants.LOCALIZATIONS_PATH); 
		messageFactory = Configurator.getConfiguredFactory(pathToLocalizations);
		formatter = DateTimeFormatter.ofPattern(Constants.TIME_PATTERN);
		logger.info("Created: GreetingServiceImpl.");
	}
	
	@Override
	public String getGreeting(String timeAsString, String token) {
		if(isTokenCorrect(token)){
			if(timeAsString == null){
				logger.warn("Attempt: get greeting with null time from: {}.", getRemoteAddr());
				return null;
			}else{
				logger.info("Prepearing: greeting for user with login: {}, time: {}, from: {}.",
						getUserLogin(), timeAsString, getRemoteAddr());
				return createMessage(timeAsString);
			}
		}else{
			logger.warn("Denied: getting greeting with wrong token from: {}.", getRemoteAddr());
			return null;
		}
	}
	
	private String createMessage(String timeAsString){
		StringBuilder builder = new StringBuilder();
		Locale locale = getRequest().getLocale();
		LocalTime time = LocalTime.parse(timeAsString, formatter);
		Message	message = messageFactory.getMessage(locale, time);
		builder.append(message.toLocalizedString());
		builder.append(", ");
		builder.append(getUserDTOfromSession().getName());
		builder.append(".");
		logger.debug("Invoked: createMessage(...) for timeAsString: {}, Locale: {}, returned: {}",
				timeAsString, locale, builder.toString());

		return builder.toString();
	}
}