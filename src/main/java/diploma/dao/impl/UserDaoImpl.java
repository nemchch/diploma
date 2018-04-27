package diploma.dao.impl;

import diploma.dao.Dao;
import diploma.dao.UserDao;
import org.springframework.stereotype.Repository;

public class UserDaoImpl extends Dao implements UserDao {

    @Override
    public long getId(String login) {
        long id = 0;
        try {
            id = (long) this.getSession().createQuery("select userId from UserEntity where login = :login")
                    .setParameter("login", login)
                    .list().get(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
}
