package dao.impl;

import dao.Dao;
import dao.UserConfigDao;

public class UserConfigDaoImpl extends Dao implements UserConfigDao {

    @Override
    public String getConfig(long userId) {
        String config = "";
        try {
            config = (String) this.getSession().createQuery("select config from UserConfigEntity where userId = :userId")
                    .setParameter("userId", userId)
                    .list().get(0);
        } catch (Exception ignored) {
        }
        return config;
    }
}
