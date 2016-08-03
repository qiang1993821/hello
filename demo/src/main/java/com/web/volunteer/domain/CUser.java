package com.web.volunteer.domain;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/7/22.
 */
@Entity
@Table(name = "zhenaiba")
public class CUser {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = true, name="nickname")
    private String nickname;
    @Column(nullable = true, name="age")
    private String age;
    @Column(nullable = true, name="uid")
    private long uid;
    @Column(nullable = true, name="height")
    private String height;
    @Column(nullable = true, name="income")
    private String income;
    @Column(nullable = true, name="loaction")
    private String loaction;
    @Column(nullable = true, name="marry")
    private String marry;
    @Column(nullable = true, name="education")
    private String education;
    @Column(nullable = true, name="job")
    private String job;
    @Column(nullable = true, name="birthday")
    private String birthday;
    @Column(nullable = true, name="weight")
    private String weight;
    @Column(nullable = true, name="constellation")
    private String constellation;
    @Column(nullable = true, name="goal")
    private String goal;
    @Column(nullable = true, name="opinion")
    private String opinion;
    @Column(nullable = true, name="meeting")
    private String meeting;
    @Column(nullable = true, name="mlplace")
    private String mlplace;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getLoaction() {
        return loaction;
    }

    public void setLoaction(String loaction) {
        this.loaction = loaction;
    }

    public String getMarry() {
        return marry;
    }

    public void setMarry(String marry) {
        this.marry = marry;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getMeeting() {
        return meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }

    public String getMlplace() {
        return mlplace;
    }

    public void setMlplace(String mlplace) {
        this.mlplace = mlplace;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
