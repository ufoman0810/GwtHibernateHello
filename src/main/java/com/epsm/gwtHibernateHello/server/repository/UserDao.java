package com.epsm.gwtHibernateHello.server.repository;

import com.epsm.gwtHibernateHello.server.domain.User;

public interface UserDao {
	User findUserByLogin(String login);
}
