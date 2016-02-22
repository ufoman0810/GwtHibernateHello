package com.epsm.gwtHibernateHello.server.repository;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.server.configuration.SessionFactorySource;
import com.epsm.gwtHibernateHello.server.domain.User;

public class UserDaoImpl implements UserDao{
	private SessionFactory factory = SessionFactorySource.getSessionFactory();
	private Logger logger = LoggerFactory.getLogger(SessionFactorySource.class);
	
	@Override
	public User findUserByLogin(String login) {
		Session session = factory.openSession();
		Transaction tx = null;
		User user = null;
		
		try{
			tx = session.beginTransaction();
			
			Query query = session.createQuery("FROM User e WHERE e.login = :login");
			query.setParameter("login",login);
			user = (User) query.uniqueResult();

			tx.commit();
		}catch (HibernateException e) {
			logger.warn("Error: while querying User from DB.", e); 
		}finally {
			session.close(); 
		}
		
		logger.debug("Invoked: findUserByLogin({}), returned {}.", login, user);
		
		return user;
	}

	@Override
	public void executeNativeSQL(String script) {
		Session session = factory.openSession();
		Transaction tx = null;
		User user = null;
		
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
		
		logger.debug("Invoked: executeNativeSQL(...), returned {}.", user);
	}
}
