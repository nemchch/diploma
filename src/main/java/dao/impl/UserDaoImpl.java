package dao.impl;

import dao.Dao;
import dao.UserDao;

public class UserDaoImpl extends Dao implements UserDao {

    @Override
    public long getId(String login) {
        long id = 0;
        try {
            id = (long) this.getSession().createQuery("select userId from UserEntity where login = :login")
                    .setParameter("login", login)
                    .list().get(0);
        } catch (Exception ignored) {
        }
        return id;
    }

    @Override
    public String getPassword(String login) {
        String password = "";
        try {
            password = (String) this.getSession().createQuery("select password from UserEntity where login = :login")
                    .setParameter("login", login)
                    .list().get(0);
        } catch (Exception ignored) {
        }
        return password;
    }

    @Override
    public boolean isLogin(String login) {
        String realLogin = "";
        try {
            realLogin = (String) this.getSession().createQuery("select password from UserEntity where login = :login")
                    .setParameter("login", login)
                    .list().get(0);
        } catch (Exception ignored) {
        }
        return realLogin.equals(login);
    }
}
