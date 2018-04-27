package services.impl;

import dao.UserActivityDao;
import dao.UserDao;
import dao.impl.UserActivityDaoImpl;
import dao.impl.UserDaoImpl;
import model.UserActivityEntity;
import services.UserActivityService;

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
