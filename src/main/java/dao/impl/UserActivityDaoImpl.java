package dao.impl;

import dao.Dao;
import dao.UserActivityDao;
import model.UserActivityEntity;
import org.hibernate.Session;

public class UserActivityDaoImpl extends Dao implements UserActivityDao {
    @Override
    public long connected(UserActivityEntity userActivityEntity) {
        Session session = null;
        try {
            session = this.getSession();
            session.beginTransaction();
            session.save(userActivityEntity);
            session.getTransaction().commit();
        } catch (Exception ignored) {
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
        } catch (Exception ignored) {
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }
}
