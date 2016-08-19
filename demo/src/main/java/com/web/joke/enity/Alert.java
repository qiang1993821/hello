package com.web.joke.enity;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/8/18.
 */
@Entity
@Table(name = "joke_alert")
public class Alert {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = true, name="uid")
    private long uid;
    @Column(nullable = true, name="title")
    private String title;
    @Column(nullable = true, name="addtime")
    private String addTime;
    @Column(nullable = true, name="edittime")
    private String editTime;
    @Column(nullable = true, name="alert_list")
    private String alertList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getAlertList() {
        return alertList;
    }

    public void setAlertList(String alertList) {
        this.alertList = alertList;
    }
}
