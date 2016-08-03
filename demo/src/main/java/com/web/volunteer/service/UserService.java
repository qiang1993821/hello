package com.web.volunteer.service;

import com.alibaba.fastjson.JSONObject;
import com.web.volunteer.domain.User;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface UserService {
    /**
     * 新增/修改用户
     * @param user
     * @return
     */
    int save(User user);

    /**
     * 根据名字获取用户
     * @param name
     * @return
     */
    List<User> findUserByName(String name);

    /**
     * 获取一个用户
     * @return
     */
    User findOneUser(long id);

    /**
     * 根据用户名字删除
     * @param name
     */
    int deleteUserByName(String name);

    /**
     * 根据id更新用户信息
     * @param name
     * @param id
     * @return
     */
    int updateUserNameById(String name,Integer id);


    /**
     * 根据用户名获取
     * @param name
     * @return
     */
    Long findIdByName(String name);


    /**
     *
     * @param id
     * @return
     */
    String getPhoneById(long id);

    /**
     * 是否有活跃事件，更新活动进展
     * @param uid
     * @return
     */
    int current(long uid);

    /**
     * 必要个人信息是否填写完善
     * @param uid
     * @return
     */
    boolean isFullInfo(long uid);

    /**
     * 删除或添加好友
     * @param uid
     * @param friendId
     * @param status true添加 false删除
     * @return
     */
    boolean doFriend(long uid,long friendId,boolean status);

    /**
     * 判断是否已经是好友
     * @param uid
     * @param friendId
     * @return
     */
    boolean isFriend(long uid,long friendId);

    /**
     * 获取活动列表
     * @param uid
     * @param type launch或partake
     * @param from 0是普通的，1是我参与列表，2是我发起的
     * @return
     */
    List<JSONObject> getActivityList(long uid,String type,int from);
}
