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
    @Transactional
    public int approve(Pend pend) {
        //在活动表的成员项加入成员，在用户表的参与项加入活动
        try {
            pendDao.delete(pend);
            Activity activity = activityDao.findOneById(pend.getActivityId()).get(0);
            User user = userDao.findOneById(pend.getUid()).get(0);
            if (activity == null || user == null)
                return 0;
            String newMember = ActivityUtil.addMember(activity.getMember(),pend.getUid());
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
            Pageable pageable = new PageRequest(page, 3, sort);
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
            if (nameList.size()>4)//最长为4
                return nameList.subList(0,4);
            return nameList;
        }catch (Exception e){
            logger.error("fuzzyQuery|"+e.getMessage());
            return null;
        }
    }
}
