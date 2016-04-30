package com.web.domain;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/4/20.
 */
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false, name="access_token")
    private String token;
    @Column(nullable = true, name="name")
    private String name;
    @Column(nullable = true, name="regtime")
    private String regTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }
}
