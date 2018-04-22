package controllers;

import model.UserActivityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import services.UserActivityService;
import services.UserService;

@Controller
public class UserActivityController {

    @Autowired
    UserActivityService userActivityService;

    @Autowired
    UserService userService;

    public Integer connected(String login, String connectionTime) {
        Integer id = userService.getId(login);
        UserActivityEntity userActivityEntity = new UserActivityEntity(id, connectionTime);
        return userActivityService.connected(userActivityEntity);
    }

    public void disconnected(Integer id, String disconnectionTime) {
        userActivityService.disconnected(id, disconnectionTime);
    }
}
