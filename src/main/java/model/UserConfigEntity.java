package model;

import javax.persistence.*;

@Entity
@Table(name = "user_config", schema = "public", catalog = "diploma")
public class UserConfigEntity {
    @Id
    @Column(name = "user_id")
    private long userId;

    @Column(name = "config", columnDefinition = "varchar(4000)")
    private String config;

    public long getUserId() {
        return userId;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
