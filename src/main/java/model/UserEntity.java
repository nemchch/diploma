package model;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "public", catalog = "diploma")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private long userId;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
