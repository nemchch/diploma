package diploma.dao.impl;

import diploma.dao.Dao;
import diploma.dao.UserActivityDao;
import lombok.extern.log4j.Log4j2;
import diploma.model.UserActivityEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

public class UserActivityDaoImpl extends Dao implements UserActivityDao {
    @Override
    public long connected(UserActivityEntity userActivityEntity) {
        Session session = null;
        try {
            session = this.getSession();
            session.beginTransaction();
            session.save(userActivityEntity);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
        return userActivityEntity.getId();
    }

    @Override
    public void disconnected(long id, String disconnectionTime) {
        Session session = null;
        try {
            session = this.getSession();
            session.beginTransaction();
            UserActivityEntity userActivityEntity = session.get(UserActivityEntity.class, id);
            userActivityEntity.setDisconnectionTime(disconnectionTime);
            session.update(userActivityEntity);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }
}
