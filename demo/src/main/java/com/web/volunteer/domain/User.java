package com.web.volunteer.domain;

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
    @Column(nullable = true, name="sex")
    private int sex;//1男，0女
    @Column(nullable = true, name="name")
    private String name;
    @Column(nullable = true, name="regtime")
    private String regTime;
    @Column(nullable = true, name="phone")
    private String phone;
    @Column(nullable = true, name="mail")
    private String mail;
    @Column(nullable = true, name="academy")
    private String academy;
    @Column(nullable = true, name="class_name")
    private String className;
    @Column(nullable = true, name="wechat")
    private String wechat;
    @Column(nullable = true, name="is_show")
    private int show;//1显示，0不显示
    @Column(nullable = true, name="partake")
    private String partake;
    @Column(nullable = true, name="launch")
    private String launch;
    @Column(nullable = true, name="friends")
    private String friends;

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

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public String getPartake() {
        return partake;
    }

    public void setPartake(String partake) {
        this.partake = partake;
    }

    public String getLaunch() {
        return launch;
    }

    public void setLaunch(String launch) {
        this.launch = launch;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
