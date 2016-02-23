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
public class ConfigurationServlet extends HttpServlet{
	private static UserDao userDao;
	private static MessageFactory messageFactory;
	private Logger logger;

	@Override
	public void init(){
		Configurator configurator = new Configurator();
		logger = LoggerFactory.getLogger(ConfigurationServlet.class);
		userDao = new UserDaoImpl();
		messageFactory = configurator.getConfiguredFactory();
		
		try{
			fillDatabaseFromScript();
		}catch(IOException e){
			logger.error("Error: while filling database from script.", e);
			throw new RuntimeException(e);
		}
		
		logger.info("Created ConfigurationServlet.");
	}
	
	private void fillDatabaseFromScript() throws IOException{
		byte[] encoded = Files.readAllBytes(Paths.get("src/main/resources/script.sql"));
		String script = new  String(encoded, Charset.forName("UTF-8"));
		userDao.executeNativeSQL(script);
	}
	
	public static UserDao getUsedDao(){
		return userDao;
	}
	
	public static MessageFactory getMesageFactory(){
		return messageFactory;
	}
}
