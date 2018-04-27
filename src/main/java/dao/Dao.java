package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Dao {
    private SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();

    protected Session getSession() {
        return sessionFactory.openSession();
    }

}
