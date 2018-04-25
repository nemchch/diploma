package diploma.services.impl;

import diploma.dao.UserActivityDao;
import diploma.dao.UserDao;
import diploma.model.UserActivityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import diploma.services.UserActivityService;

@Service
public class UserActivityServiceImpl implements UserActivityService {

    @Autowired
    UserActivityDao userActivityDao;

    @Autowired
    UserDao userDao;

    @Override
    public Integer connected(String login, String connectionTime) {
        Integer id = userDao.getId(login);
        UserActivityEntity userActivityEntity = new UserActivityEntity(id, connectionTime);
        return userActivityDao.connected(userActivityEntity);
    }

    @Override
    public void disconnected(Integer id, String disconnectionTime) {
        userActivityDao.disconnected(id, disconnectionTime);
    }
}
