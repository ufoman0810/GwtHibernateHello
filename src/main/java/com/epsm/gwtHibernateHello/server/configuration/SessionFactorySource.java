package com.epsm.gwtHibernateHello.server.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionFactorySource {
	private static final SessionFactory sessionFactory;
	private static Logger logger;

	static {
		logger = LoggerFactory.getLogger(SessionFactorySource.class);
		
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable e) {
			//That is antipattern but as there isn't any container that will log exception again I log it.
			logger.error("Error: while creating sessionFactory.", e);
			throw new ExceptionInInitializerError(e);
	    }
		
		logger.info("Created: SessionFactorySource.");
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
