package model;

import javax.persistence.*;

@Entity
@Table(name = "user_activity", schema = "public", catalog = "diploma")
public class UserActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private Integer userId;
    private String connectionTime;
    private String disconnectionTime;

    public UserActivityEntity(Integer userId, String connectionTime) {
        this.connectionTime = connectionTime;
        this.userId = userId;
    }

    public UserActivityEntity() {
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "connection_time")
    public String getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(String connectionTime) {
        this.connectionTime = connectionTime;
    }

    @Basic
    @Column(name = "disconnection_time")
    public String getDisconnectionTime() {
        return disconnectionTime;
    }

    public void setDisconnectionTime(String disconnectionTime) {
        this.disconnectionTime = disconnectionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserActivityEntity that = (UserActivityEntity) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (connectionTime != null ? !connectionTime.equals(that.connectionTime) : that.connectionTime != null)
            return false;
        if (disconnectionTime != null ? !disconnectionTime.equals(that.disconnectionTime) : that.disconnectionTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (connectionTime != null ? connectionTime.hashCode() : 0);
        result = 31 * result + (disconnectionTime != null ? disconnectionTime.hashCode() : 0);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
