package services.impl;

import dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import services.UserService;

public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public Integer getId(String login) {
        return userDao.getId(login);

    }
}
