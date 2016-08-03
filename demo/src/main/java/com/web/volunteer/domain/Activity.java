package com.web.volunteer.domain;

import javax.persistence.*;

/**
 * Created by qiangyipeng on 2016/5/22.
 */
@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false, name="name")
    private String name;
    @Column(nullable = false, name="sponsor")
    private long sponsor;
    @Column(nullable = true, name="start_time")
    private String startTime;
    @Column(nullable = true, name="end_time")
    private String endTime;
    @Column(nullable = true, name="invitation")
    private String invitation;
    @Column(nullable = true, name="status")
    private int status;//0审核中，1招募中，2即将开始，3正在进行，4已结束
    @Column(nullable = true, name="member")
    private String member;
    @Column(nullable = true, name="hour")
    private float hour;
    @Column(nullable = true, name="details")
    private String details;

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

    public long getSponsor() {
        return sponsor;
    }

    public void setSponsor(long sponsor) {
        this.sponsor = sponsor;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInvitation() {
        return invitation;
    }

    public void setInvitation(String invitation) {
        this.invitation = invitation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public float getHour() {
        return hour;
    }

    public void setHour(float hour) {
        this.hour = hour;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
