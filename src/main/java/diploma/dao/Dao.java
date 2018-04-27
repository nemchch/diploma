package diploma.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

public class Dao {
    private SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();

    protected Session getSession() {
        return sessionFactory.openSession();
    }

}
