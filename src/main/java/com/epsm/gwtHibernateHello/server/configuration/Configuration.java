package com.epsm.gwtHibernateHello.server.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.server.repository.UserDao;
import com.epsm.gwtHibernateHello.server.repository.UserDaoImpl;
import com.epsm.gwtHibernateHello.shared.Constants;
import com.epsm.hello.configutation.Configurator;
import com.epsm.hello.model.MessageFactory;

@SuppressWarnings("serial")
public class Configuration extends HttpServlet{
	private static UserDao userDao;
	private static MessageFactory messageFactory;
	private static Properties properties;
	private static Logger logger = LoggerFactory.getLogger(Configuration.class);

	public static synchronized UserDao getUsedDao(){
		if(userDao == null){
			createDaoAndFillDatabase();
		}
		
		return userDao;
	}
	
	private static void createDaoAndFillDatabase(){
		createDao();
		fillDatabase();
	}
	
	private static void createDao(){
		userDao = new UserDaoImpl();
	}
	
	private static void fillDatabase(){
		try{
			fillDatabaseFromScript();
		}catch(IOException e){
			logger.error("Error: while filling database from script.", e);
			throw new RuntimeException(e);
		}
	}
	
	private static void fillDatabaseFromScript() throws IOException{
		byte[] encoded = Files.readAllBytes(Paths.get(getSqlScriptPath()));
		String script = new  String(encoded, Charset.forName("UTF-8"));
		userDao.executeNativeSQL(script);
		logger.info("Filled: database from script.sql");
	}

	public static synchronized MessageFactory getMesageFactory(){
		if(messageFactory == null){
			createMessageFactory();
		}
		
		return messageFactory;
	}
	
	private static void createMessageFactory(){
		Configurator configurator = new Configurator(getLocalizationsPath());
		messageFactory = configurator.getConfiguredFactory();
		logger.info("Created: MessageFactory.");
	}
	
	public static synchronized String getLocalizationsPath(){
		if(properties == null){
			loadProperties();
		}
		
		return getPropertyFromProperties(Constants.LOCALIZATIONS_PATH);
	}
	
	private static void loadProperties(){
		try {
			loadPropertiesFromFile();
		} catch (IOException e) {
			//as there no container error should be logged
			logger.error("Error: while filling database from script.", e);
			throw new RuntimeException(e);
		}	
	}
	
	private static void loadPropertiesFromFile() throws IOException{
		properties = new Properties();
		
		InputStream inputStream = Configuration.class.getClassLoader()
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
		
		logger.debug("Invoked: getSqlScriptPathFromProperties(), returned:{}.", property);
		
		return property;
	}
	
	public static synchronized String getSqlScriptPath(){
		if(properties == null){
			loadProperties();
		}
		
		return getPropertyFromProperties(Constants.SQL_SCRIPT_PATH);
	}
}
