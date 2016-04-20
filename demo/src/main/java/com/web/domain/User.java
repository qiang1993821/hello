package com.web.domain;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/4/20.
 */
@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false, name="NAME") // 使用小写会不起作用，修改为大写便正常了。不知道为何，如果遇到一样问题的可以尝试下。
    private String name;
    @Column(nullable = false, name="JOB")
    private String job;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
