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
import com.web.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Activity findOneById(long id) {
        return activityDao.findOneById(id).get(0);
    }

    @Override
    @Transactional
    public int join(Pend pend) {
        try {
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
            String newJoin = UserUtil.addJoin(user.getJoin(),pend.getActivityId());
            if (newMember == null || newJoin == null)
                return 0;
            activity.setMember(newMember);
            user.setJoin(newJoin);
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
}
