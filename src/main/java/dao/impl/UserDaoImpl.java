package dao.impl;

import dao.Dao;
import dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends Dao implements UserDao {
    @Override
    public Integer getId(String login) {
        Integer id;
        id = this.getSession().createQuery("select user_id from UserEntity where login = :login")
                .setParameter("login", login)
                .getFirstResult();
        return id;
    }
}
