package dao.impl;

import dao.Dao;
import dao.UserActivityDao;
import lombok.extern.log4j.Log4j2;
import model.UserActivityEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class UserActivityDaoImpl extends Dao implements UserActivityDao {
    @Override
    public Integer connected(UserActivityEntity userActivityEntity) {
        Session session = null;
        try {
            session = this.getSession();
            session.beginTransaction();
            session.save(userActivityEntity);
            session.getTransaction().commit();
        } catch (Exception e) {
            //log.error(e, new RuntimeException("EventDao error on removeEvent"));
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
        return userActivityEntity.getId();
    }

    @Override
    public void disconnected(Integer id, String disconnectionTime) {
        Session session = null;
        try {
            session = this.getSession();
            session.beginTransaction();
            UserActivityEntity userActivityEntity = session.get(UserActivityEntity.class, id);
            userActivityEntity.setDisconnectionTime(disconnectionTime);
            session.update(userActivityEntity);
            session.getTransaction().commit();
        } catch (Exception e) {
            //log.error(e, new RuntimeException("EventDao error on removeEvent"));
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }
}
