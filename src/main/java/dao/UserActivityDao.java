package dao;

import model.UserActivityEntity;

public interface UserActivityDao {
    long connected(UserActivityEntity userActivityEntity);
    void disconnected(long id, String disconnectionTime);
}
