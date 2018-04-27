package diploma.services.impl;

import diploma.dao.UserActivityDao;
import diploma.dao.UserDao;
import diploma.dao.impl.UserActivityDaoImpl;
import diploma.dao.impl.UserDaoImpl;
import diploma.model.UserActivityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import diploma.services.UserActivityService;

public class UserActivityServiceImpl implements UserActivityService {

    private UserActivityDao userActivityDao = new UserActivityDaoImpl();

    private UserDao userDao = new UserDaoImpl();

    @Override
    public long connected(String login, String connectionTime) {
        long id = userDao.getId(login);
        UserActivityEntity userActivityEntity = new UserActivityEntity(id, connectionTime);
        return userActivityDao.connected(userActivityEntity);
    }

    @Override
    public void disconnected(long id, String disconnectionTime) {
        userActivityDao.disconnected(id, disconnectionTime);
    }
}
