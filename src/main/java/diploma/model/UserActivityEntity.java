package diploma.model;

import javax.persistence.*;

@Entity
@Table(name = "user_activity", schema = "public", catalog = "diploma")
public class UserActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    @Column(name = "user_id")
    private long userId;


    @Column(name = "connection_time")
    private String connectionTime;


    @Column(name = "disconnection_time")
    private String disconnectionTime;

    public UserActivityEntity(long userId, String connectionTime) {
        this.connectionTime = connectionTime;
        this.userId = userId;
    }

    public UserActivityEntity() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(String connectionTime) {
        this.connectionTime = connectionTime;
    }

    public String getDisconnectionTime() {
        return disconnectionTime;
    }

    public void setDisconnectionTime(String disconnectionTime) {
        this.disconnectionTime = disconnectionTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
