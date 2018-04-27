package diploma.services.impl;

import diploma.dao.UserDao;
import diploma.dao.impl.UserDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import diploma.services.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public long getId(String login) {
        return userDao.getId(login);

    }
}
