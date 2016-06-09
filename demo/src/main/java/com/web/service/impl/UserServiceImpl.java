package com.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.web.dao.ActivityDao;
import com.web.dao.PendDao;
import com.web.dao.UserDao;
import com.web.domain.Activity;
import com.web.domain.Pend;
import com.web.domain.User;
import com.web.service.UserService;
import com.web.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
@SpringBootApplication
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private PendDao pendDao;

    @Override
    public int save(User user) {
        try {
            userDao.save(user);
            return 1;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public List<User> findUserByName(String name) {
        return userDao.findUserByName(name);
    }

    @Override
    public User findOneUser(long id) {
        return userDao.findOneById(id).get(0);
    }

    @Override
    @Transactional
    public int deleteUserByName(String name) {
        return userDao.deleteUserByName(name);
    }

    @Override
    @Transactional
    public int updateUserNameById(String name, Integer id) {
        return userDao.updateNameById(name,id);
    }

    @Override
    public String getPhoneById(long id) {
        String key = "phone-"+id;
        String phone = (String)CacheUtil.getCache(key);
        if (StringUtils.isBlank(phone)){
            try {
                phone = userDao.getPhoneById(id).get(0);
            }catch (Exception e){
                logger.error("getPhoneById:"+e.getMessage());
            }
        }
        return phone;
    }

    @Override
    public Long findIdByName(String name) {
        if (name!=null) {
            Long uid = (Long) CacheUtil.getCache(name);
            if (uid == null) {
                try {
                    uid = userDao.findIdByName(name).get(0);
                }catch (Exception e){
                    logger.error("findIdByName:"+e.getMessage());
                }
            }
            return uid;
        }else {
            return null;
        }
    }

    @Override
    public int current(long uid) {
        int current = 0;
        try {
            User user = userDao.findOneById(uid).get(0);
            if (StringUtils.isNotBlank(user.getLaunch())){
                List<Long> activityList = (List<Long>)JSON.parseObject(user.getLaunch()).get("activityList");
                current = checkStatus(activityList);
            }
            if (StringUtils.isNotBlank(user.getPartake())){
                List<Long> activityList = (List<Long>)JSON.parseObject(user.getPartake()).get("activityList");
                current = checkStatus(activityList);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return current;
    }

    /**
     * 检查和修改活动阶段，返回是否有活跃事件
     * @param activityList
     * @return 1有，0没有
     */
    public int checkStatus(List<Long> activityList){
        int current = 0;
        if (activityList.size()>0){
            for (Number activityId:activityList){
                Activity activity = activityDao.findOneById(activityId.longValue()).get(0);
                int oldStatus = activity.getStatus();
                if (oldStatus>=1&&oldStatus<=3){
                    int status = TimeUtil.changeStatus(activity.getStartTime(),activity.getEndTime());
                    if (status!=oldStatus){//状态发生了改变
                        activity.setStatus(status);
                        activityDao.save(activity);
                        logger.error("ACTIVITY_STATUS_CHANGE|from "+oldStatus+" to "+status+" at time "+TimeUtil.getNowTime());
                        List<Pend> pendList = pendDao.queryByActivityId(activityId.longValue());
                        if (pendList.size() > 0) {
                            switch (status) {
                                case 3://进行中，将报名和邀请设为拒绝
                                    for (Pend pend : pendList) {
                                        pend.setStatus(-1);
                                        pendDao.save(pend);
                                        User user = userDao.findOneById(pend.getUid()).get(0);
                                        String msg = "志愿者，你好！很遗憾活动发起者拒绝了你对开始于"+activity.getStartTime()+"的"+activity.getName()+"的申请。";
                                        String title = activity.getName()+"活动报名结果反馈";
                                        MailUtil.sendMail(MailUtil.ustbMail, MailUtil.ustbPwd, user.getMail(), title, msg);
                                    }
                                    break;
                                case 4://已结束，删除报名和邀请记录
                                    pendDao.delByActivityId(activityId.longValue());
                                    break;
                            }
                        }
                    }
                }
                if (activity.getStatus()==2||activity.getStatus()==3)//即将开始或正在进行
                    current = 1;
            }
        }
        return current;
    }

    @Override
    public boolean isFullInfo(long uid) {
        try {
            User user = userDao.findOneById(uid).get(0);
            if (StringUtils.isNotBlank(user.getName())&&StringUtils.isNotBlank(user.getPhone())&&StringUtils.isNotBlank(user.getMail()))
                return true;
        }catch (Exception e){
            logger.error("isFullInfo|"+e.getMessage());
        }
        return false;
    }

    @Override
    public boolean doFriend(long uid, long friendId, boolean status) {
        try {
            User user = userDao.findOneById(uid).get(0);
            User friend = userDao.findOneById(friendId).get(0);
            user.setFriends(UserUtil.doFriend(user.getFriends(),friendId,friend.getName(),status));
            userDao.save(user);
            return true;
        }catch (Exception e){
            logger.error("doFriend|"+e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isFriend(long uid, long friendId) {
        try {
            User user = userDao.findOneById(uid).get(0);
            if (StringUtils.isBlank(user.getFriends())){
                return false;
            }else {
                List<JSONObject> friends = (List<JSONObject>)JSON.parseObject(user.getFriends()).get("friendList");
                for (JSONObject jsonObject:friends){
                    if (jsonObject.getString("id").equals(friendId+"")){
                        return true;
                    }
                }
            }
        }catch (Exception e){
            logger.error("isFriend|"+e.getMessage());
        }
        return false;
    }

    @Override
    public List<JSONObject> getActivityList(long uid, String type, int from) {
        try {
            User user = userDao.findOneById(uid).get(0);
            if (user == null || StringUtils.isBlank(type))
                return null;
            if (type.equals("launch") && StringUtils.isNotBlank(user.getLaunch())){
                List<Long> launch = (List<Long>)JSON.parseObject(user.getLaunch()).get("activityList");
                List<Activity> activityList = new ArrayList<Activity>();
                for (int i = launch.size()-1; i >= 0; i--){//倒序，最新的在上面
                    Number activityId = launch.get(i);
                    Activity activity = activityDao.findOneById(activityId.longValue()).get(0);
                    if (activity == null)
                        continue;
                    activityList.add(activity);
                }
                List<JSONObject> newLaunch = ActivityUtil.getActivityList(activityList,from);
                return newLaunch;
            }else if (type.equals("partake") && StringUtils.isNotBlank(user.getPartake())){
                List<Long> partake = (List<Long>)JSON.parseObject(user.getPartake()).get("activityList");
                List<Activity> activityList = new ArrayList<Activity>();
                List<Pend> pendList = pendDao.getPartake(uid);
                List<JSONObject> newPartake = new ArrayList<JSONObject>();
                for (Pend pend:pendList){
                    Activity activity = activityDao.findOneById(pend.getActivityId()).get(0);
                    if (activity == null)
                        continue;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",activity.getName());
                    jsonObject.put("info","正在审核");
                    jsonObject.put("url","/activityInfo?activityId=" + activity.getId());
                    newPartake.add(jsonObject);
                }
                for (int i = partake.size()-1; i >= 0; i--){//倒序，最新的在上面
                    Number activityId = partake.get(i);
                    Activity activity = activityDao.findOneById(activityId.longValue()).get(0);
                    if (activity == null)
                        continue;
                    activityList.add(activity);
                }
                newPartake.addAll(ActivityUtil.getActivityList(activityList,from));
                return newPartake;
            }
        }catch (Exception e){
            logger.error("getActivityList|"+e.getMessage());
        }
        return null;
    }
}
