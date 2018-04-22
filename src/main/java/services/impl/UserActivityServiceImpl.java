package services.impl;

import dao.UserActivityDao;
import model.UserActivityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.UserActivityService;

@Service
public class UserActivityServiceImpl implements UserActivityService {

    @Autowired
    UserActivityDao userActivityDao;

    @Override
    public Integer connected(UserActivityEntity userActivityEntity) {
        return userActivityDao.connected(userActivityEntity);
    }

    @Override
    public void disconnected(Integer id, String disconnectionTime) {
        userActivityDao.disconnected(id, disconnectionTime);
    }
}
