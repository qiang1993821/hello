package com.web.service;

import com.alibaba.fastjson.JSONObject;
import com.web.domain.Activity;
import com.web.domain.Pend;

import java.util.List;

/**
 * Created by Administrator on 2016/5/22.
 */
public interface ActivityService {
    /**
     * 新增/修改活动
     * @param activity
     * @return
     */
    int save(Activity activity);

    /**
     * 发起活动
     * @param activity
     * @return
     */
    int launch(Activity activity);

    /**
     * 通过id获取活动
     * @param id
     * @return
     */
    Activity findOneById(long id);

    /**
     * 报名活动
     * @param pend
     * @param type 1是报名，2是邀请
     * @return
     */
    int addPend(Pend pend,int type);

    /**
     * 批准报名
     * @param pend
     */
    int approve(Pend pend);

    /**
     * 拒绝报名
     * @param pend
     * @return
     */
    int reject(Pend pend);

    /**
     * 分页查询活动信息
     * @param page
     * @return
     */
    List<JSONObject> queryByPage(int page);

    /**
     * 根据活动名称查询，会有重名
     * @param name
     * @return
     */
    List<JSONObject> queryByName(String name);

    /**
     * 模糊查询
     * @param name
     * @return
     */
    List<String> fuzzyQuery(String name);

    /**
     * 获取成员列表
     * @param activityId
     * @return
     */
    List<JSONObject> getMemberList(long activityId);

    /**
     * 获取受邀活动
     * @param uid
     * @return
     */
    List<JSONObject> getInviteList(long uid);

    /**
     * 获取报名列表
     * @param activityId
     * @return
     */
    List<JSONObject> getApproveList(long activityId);

    /**
     * 我发起的活动招募中的成员列表
     * @param activityId
     * @return
     */
    List<JSONObject> partakeList(long activityId);

    /**
     * 确认列表
     * @param activityId
     * @return
     */
    List<JSONObject> ensureList(long activityId);

    /**
     * 招募中活动邀请朋友列表页
     * @param uid
     * @param activityId
     * @return
     */
    List<JSONObject> friendList(long uid,long activityId);

    /**
     * 判断是否已经报名参加发起
     * @param uid
     * @param activityId
     * @return
     */
    boolean hasJoined(long uid,long activityId);

    /**
     * 获取活动详情展示页的相关属性
     * @param activityId
     * @return
     */
    JSONObject getActivityInfo(long activityId);
}
