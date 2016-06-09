package com.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.web.dao.ActivityDao;
import com.web.dao.PendDao;
import com.web.dao.UserDao;
import com.web.domain.Activity;
import com.web.domain.Pend;
import com.web.domain.User;
import com.web.service.ActivityService;
import com.web.util.ActivityUtil;
import com.web.util.TimeUtil;
import com.web.util.UserUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@SpringBootApplication
@Service
public class ActivityServiceImpl implements ActivityService {
    private final Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private PendDao pendDao;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public int save(Activity activity) {
        try {
            activityDao.save(activity);
            return 1;
        }catch (Exception e){
            logger.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public int launch(Activity activity) {
        try {
            JSONObject launch = null;
            List<Long> activityList = null;
            activity.setStatus(1);//暂时直接进入招募阶段
            activityDao.save(activity);
            User user = userDao.findOneById(activity.getSponsor()).get(0);
            if (StringUtils.isBlank(user.getLaunch())){//之前没发布过活动
                launch = new JSONObject();
                activityList = new ArrayList<Long>();
            }else {
                launch = JSON.parseObject(user.getLaunch());
                activityList = (List<Long>)launch.get("activityList");
            }
            activityList.add(activity.getId());
            launch.put("activityList",activityList);
            user.setLaunch(launch.toJSONString());
            userDao.save(user);
            return 1;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public Activity findOneById(long id) {
        return activityDao.findOneById(id).get(0);
    }

    @Override
    public int join(Pend pend) {
        try {
            pend.setType(1);
            pendDao.save(pend);
            return 1;
        }catch (Exception e){
            logger.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public int approve(Pend pend) {
        //在活动表的成员项加入成员，在用户表的参与项加入活动
        try {
            pendDao.delete(pend);
            Activity activity = activityDao.findOneById(pend.getActivityId()).get(0);
            User user = userDao.findOneById(pend.getUid()).get(0);
            if (activity == null || user == null)
                return 0;
            String newMember = ActivityUtil.addMember(activity.getMember(),pend.getUid(),pend.getUsername());
            String newJoin = UserUtil.addJoin(user.getPartake(),pend.getActivityId());
            if (newMember == null || newJoin == null)
                return 0;
            activity.setMember(newMember);
            user.setPartake(newJoin);
            activityDao.save(activity);
            userDao.save(user);
            return 1;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    @Transactional
    public int reject(Pend pend) {
        try {
            Pend newPend = pendDao.findOneById(pend.getUid()).get(0);
            newPend.setStatus(-1);
            pendDao.save(newPend);
            return 1;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public List<JSONObject> queryByPage(int page) {
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            Pageable pageable = new PageRequest(page, 5, sort);
            Page<Activity> activityPage = activityDao.findAll(pageable);
            List<Activity> activityList = activityPage.getContent();
            List<JSONObject> jsonList = new ArrayList<JSONObject>();
            for (Activity activity:activityList){
                jsonList.add(ActivityUtil.activityToJSON(activity));
            }
            return jsonList;
        }catch (Exception e){
            logger.error("queryByPage|"+e.getMessage());
            return null;
        }
    }

    @Override
    public List<JSONObject> queryByName(String name) {
        try {
            List<Activity> activityList = activityDao.queryByName(name);
            List<JSONObject> jsonList = new ArrayList<JSONObject>();
            for (Activity activity:activityList){
                jsonList.add(ActivityUtil.activityToJSON(activity));
            }
            return jsonList;
        }catch (Exception e){
            logger.error("queryByName|"+e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> fuzzyQuery(String name) {
        try {
            List<String> nameList =  activityDao.queryLikeName("%"+name+"%");
            if (nameList.size()>10)//最长为10
                return nameList.subList(0,10);
            return nameList;
        }catch (Exception e){
            logger.error("fuzzyQuery|"+e.getMessage());
            return null;
        }
    }

    @Override
    public boolean hasJoined(long uid, long activityId) {
        try {
            if (pendDao.hasJoined(activityId,uid).size()>0)//判断是否报名|受邀过
                return true;
            User user = userDao.findOneById(uid).get(0);
            if (StringUtils.isNotBlank(user.getPartake())) {//判断是否参与过
                List<Number> partake = (List<Number>) JSON.parseObject(user.getPartake()).get("activityList");
                if (partake!=null && partake.size()>0) {//防止空指针异常，下同
                    for (Number number : partake) {
                        if (number.longValue() == activityId)
                            return true;
                    }
                }
            }
            if (StringUtils.isNotBlank(user.getLaunch())) {//判断是否发起过
                List<Number> launch = (List<Number>) JSON.parseObject(user.getLaunch()).get("activityList");
                if (launch!=null && launch.size()>0) {
                    for (Number number : launch) {
                        if (number.longValue() == activityId)
                            return true;
                    }
                }
            }
        }catch (Exception e){
            logger.error("hasJoined|"+e.getMessage());
        }
        return false;
    }

    @Override
    public List<JSONObject> getMemberList(long activityId) {
        try {
            List<JSONObject> memberList = new ArrayList<JSONObject>();
            Activity activity = activityDao.findOneById(activityId).get(0);
            User user = userDao.findOneById(activity.getSponsor()).get(0);
            JSONObject sponsor = new JSONObject();
            sponsor.put("name",user.getName());
            sponsor.put("url","/userInfo?uid="+user.getId()+"&page=1");
            sponsor.put("info","发起者");
            memberList.add(sponsor);
            if (StringUtils.isNotBlank(activity.getMember())) {
                JSONObject jsonObject = JSON.parseObject(activity.getMember());
                List<JSONObject> otherList = (List<JSONObject>) jsonObject.get("memberList");
                if (otherList != null && otherList.size() > 0) {
                    memberList.addAll((ActivityUtil.getMemberList(otherList)));
                }
            }
            return memberList;
        }catch (Exception e){
            logger.error("getMemberList|"+e.getMessage());
            return null;
        }
    }

    @Override
    public JSONObject getActivityInfo(long activityId) {
        try {
            Activity activity = activityDao.findOneById(activityId).get(0);
            JSONObject jsonObject = ActivityUtil.activityToJSON(activity);
            jsonObject.put("startTime",activity.getStartTime());
            jsonObject.put("endTime",activity.getEndTime());
            jsonObject.put("details",activity.getDetails());
            boolean join = false;
            if (activity.getStatus()==1 || activity.getStatus()==2)
                join = true;
            jsonObject.put("join", join);
            return jsonObject;
        }catch (Exception e){
            logger.error("getActivityInfo|"+e.getMessage());
            return null;
        }
    }

    @Override
    public List<JSONObject> getInviteList(long uid) {
        try {
            List<Pend> pendList = pendDao.getInvite(uid);
            List<JSONObject> inviteList = new ArrayList<JSONObject>();
            for (Pend pend:pendList){
                JSONObject invite = new JSONObject();
                invite.put("name",pend.getActivityName());
                invite.put("info","");
                invite.put("url","/activityInfo?activityId=" + pend.getActivityId() + "&page=1");
                inviteList.add(invite);
            }
            return inviteList;
        }catch (Exception e){
            logger.error("getInviteList|"+e.getMessage());
            return null;
        }
    }
}
