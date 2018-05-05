package services.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import services.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean isPassword(String login, String password) {
        return userDao.isPassword(login, password);
    }

    @Override
    public boolean isLogin(String login) {
        return userDao.isLogin(login);
    }

    @Override
    public long getId(String login) {
        return userDao.getId(login);
    }
}
