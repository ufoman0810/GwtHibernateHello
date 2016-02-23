package com.epsm.gwtHibernateHello.server.configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.server.repository.UserDao;
import com.epsm.gwtHibernateHello.server.repository.UserDaoImpl;
import com.epsm.hello.configutation.Configurator;
import com.epsm.hello.model.MessageFactory;

@SuppressWarnings("serial")
public class Configuration extends HttpServlet{
	private static UserDao userDao;
	private static MessageFactory messageFactory;
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
		byte[] encoded = Files.readAllBytes(Paths.get("src/main/resources/script.sql"));
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
		Configurator configurator = new Configurator();
		messageFactory = configurator.getConfiguredFactory();
		logger.info("Created ConfigurationServlet.");
	}
}
