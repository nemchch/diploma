package dao;

public interface UserDao {
    long getId(String login);

    String getPassword(String login);

    boolean isLogin(String login);
}
