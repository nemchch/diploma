package service.impl;

import dao.UserConfigDao;
import dao.impl.UserConfigDaoImpl;
import service.UserConfigService;

public class UserConfigServiceImpl implements UserConfigService {

    private UserConfigDao userConfigDao = new UserConfigDaoImpl();

    @Override
    public String getConfig(long userId) {
        return userConfigDao.getConfig(userId);
    }
}
