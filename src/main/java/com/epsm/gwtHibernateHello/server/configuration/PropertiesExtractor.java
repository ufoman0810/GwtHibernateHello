package com.epsm.gwtHibernateHello.server.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.shared.Constants;

public class PropertiesExtractor{
	private static Properties properties;
	private static Logger logger = LoggerFactory.getLogger(PropertiesExtractor.class);
	
	public static synchronized String getProperty(String property){
		if(properties == null){
			try{
				loadPropertiesFromClasspath();
			}catch(IOException e){
				logger.error("Error: while loading properties from classpath.", e);
				throw new RuntimeException(e);
			}
		}
		
		return getPropertyFromProperties(property);
	}
	
	private static void loadPropertiesFromClasspath() throws IOException{
		properties = new Properties();
		
		InputStream inputStream = PropertiesExtractor.class.getClassLoader()
				.getResourceAsStream(Constants.APPLICATION_PROPERTIES);
		
		if (inputStream != null) {
			properties.load(inputStream);
		} else {
			String message = String.format("property file: %s not found in the classpath",
					Constants.APPLICATION_PROPERTIES);
			throw new FileNotFoundException(message);
		}
	}
	
	private static String getPropertyFromProperties(String propertyName){
		String property = properties.getProperty(propertyName);
		
		if(property == null){
			String message = String.format("Error: property: %s not found in: %s file", 
					propertyName, Constants.APPLICATION_PROPERTIES);
			logger.error(message);
			throw new RuntimeException(message);
		}
		
		logger.debug("Invoked: getPropertyFromProperties({}), returned: {}.", propertyName, property);
		
		return property;
	}
}
