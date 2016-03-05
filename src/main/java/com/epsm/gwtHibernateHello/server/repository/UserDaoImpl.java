package com.epsm.gwtHibernateHello.server.repository;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.gwtHibernateHello.server.configuration.SessionFactorySource;
import com.epsm.gwtHibernateHello.server.domain.User;

public class UserDaoImpl implements UserDao{
	private static UserDao usedDao;
	private SessionFactory factory = SessionFactorySource.getSessionFactory();
	private static Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	
	private UserDaoImpl(){
	}
	
	public static synchronized UserDao getInstatnce(){
		if(usedDao == null){
			usedDao = new UserDaoImpl();
			logger.info("Created: UserDaoImpl.");
		}
		
		return usedDao;
	}
	
	@Override
	public User findUserByLogin(String login) {
		Session session = factory.openSession();
		Transaction tx = null;
		User user = null;
		
		try{
			tx = session.beginTransaction();
			
			Query query = session.createQuery("FROM User e WHERE e.login = :login");
			query.setParameter("login", login);
			user = (User) query.uniqueResult();

			tx.commit();
		}catch (HibernateException e) {
			logger.warn("Error: while querying User from DB.", e); 
		}finally {
			session.close(); 
		}
		
		logger.debug("Executed: findUserByLogin({}), returned: {}.", login, user);
		
		return user;
	}
}
