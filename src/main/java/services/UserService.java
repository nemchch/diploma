package services;

public interface UserService {
    String getPassword(String login);

    boolean isLogin(String login);
}
