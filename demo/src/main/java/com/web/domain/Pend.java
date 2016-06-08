package com.web.domain;

import javax.persistence.*;

/**
 * Created by Roc on 2016/5/22.
 */
@Entity
@Table(name = "pend")
public class Pend {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = true, name="username")
    private String username;
    @Column(nullable = false, name="uid")
    private long uid;
    @Column(nullable = false, name="activity_id")
    private long activityId;
    @Column(nullable = true, name="activity_name")
    private String activityName;
    @Column(nullable = false, name="type")
    private int type;//0是申请，1是邀请
    @Column(nullable = false, name="status")
    private int status;//-1为被拒绝

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
