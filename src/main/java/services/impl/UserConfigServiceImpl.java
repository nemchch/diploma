package services.impl;

import dao.UserConfigDao;
import dao.impl.UserConfigDaoImpl;
import services.UserConfigService;

public class UserConfigServiceImpl implements UserConfigService {

    private UserConfigDao userConfigDao = new UserConfigDaoImpl();

    @Override
    public String getConfig(long userId) {
        return userConfigDao.getConfig(userId);
    }
}
