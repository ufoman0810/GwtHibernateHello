package com.epsm.gwtHibernateHello.server.configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.shared.Constants;

@SuppressWarnings("serial")
public class ConfigurationServlet extends HttpServlet{
	private Logger logger = LoggerFactory.getLogger(ConfigurationServlet.class);
	
	@Override
	public void init(){
		try{
			fillDatabaseFromScript();
		}catch(IOException e){
			logger.error("Error: while filling database from script.", e);
			throw new RuntimeException(e);
		}
	}
	
	private void fillDatabaseFromScript() throws IOException{
		NativeSqlExecuter sqlExecuter = NativeSqlExecuter.getInstance();
		String path = PropertiesExtractor.getProperty(Constants.SQL_SCRIPT_PATH);
		byte[] scriptAsBytes = Files.readAllBytes(Paths.get(path));
		String script = new  String(scriptAsBytes, Charset.forName("UTF-8"));
		sqlExecuter.executeNativeSQL(script);
		logger.info("Filled: database from {}.", path);
	}
}
