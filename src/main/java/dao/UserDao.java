package dao;

public interface UserDao {
    long getId(String login);

    boolean isPassword(String login, String password);

    boolean isLogin(String login);
}
