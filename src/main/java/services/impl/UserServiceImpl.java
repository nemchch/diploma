package services.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import services.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public String getPassword(String login) {
        return userDao.getPassword(login);
    }

    @Override
    public boolean isLogin(String login) {
        return userDao.isLogin(login);
    }
}
