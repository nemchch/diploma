package service;

public interface UserService {
    boolean isPassword(String login, String password);

    boolean isLogin(String login);

    long getId(String login);
}
