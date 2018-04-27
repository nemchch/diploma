package services;

public interface UserActivityService {
    long connected(String login, String connectionTime);
    void disconnected(long id, String disconnectionTime);
}
