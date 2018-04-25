package diploma.services;

public interface UserActivityService {
    Integer connected(String login, String connectionTime);
    void disconnected(Integer id, String disconnectionTime);
}
