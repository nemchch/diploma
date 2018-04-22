package dao;

import model.UserActivityEntity;

public interface UserActivityDao {
    Integer connected(UserActivityEntity userActivityEntity);
    void disconnected(Integer id, String disconnectionTime);
}
