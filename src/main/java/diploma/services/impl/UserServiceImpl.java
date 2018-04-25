package diploma.services.impl;

import diploma.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import diploma.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public Integer getId(String login) {
        return userDao.getId(login);

    }
}
