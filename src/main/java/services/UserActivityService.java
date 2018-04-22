package services;

import model.UserActivityEntity;

public interface UserActivityService {
    Integer connected(UserActivityEntity userActivityEntity);
    void disconnected(Integer id, String disconnectionTime);
}
