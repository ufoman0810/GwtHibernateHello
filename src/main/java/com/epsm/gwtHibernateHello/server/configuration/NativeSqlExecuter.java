package com.epsm.gwtHibernateHello.server.configuration;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeSqlExecuter {
	private static NativeSqlExecuter executer;
	private SessionFactory factory = SessionFactorySource.getSessionFactory();
	private static Logger logger = LoggerFactory.getLogger(NativeSqlExecuter.class);
	
	private NativeSqlExecuter(){
	}
	
	public static synchronized NativeSqlExecuter getInstance(){
		if(executer == null){
			executer = new NativeSqlExecuter();
			logger.info("Created: NativeSqlExecuter.");
		}
		
		return executer;
	}
	
	public void executeNativeSQL(String script) {
		Session session = factory.openSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			SQLQuery query = session.createSQLQuery(script);
			query.executeUpdate();

			tx.commit();
		}catch (HibernateException e) {
			if(tx != null){
				tx.rollback();
			}
			
			logger.warn("Error: while executing native SQL.", e); 
		}finally {
			session.close(); 
		}
		
		logger.debug("Invoked: executeNativeSQL(...).");
	}
}
