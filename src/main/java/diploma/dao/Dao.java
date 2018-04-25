package diploma.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class Dao {
    @Autowired
    SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.openSession();
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
