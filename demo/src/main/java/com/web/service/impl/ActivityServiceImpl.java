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
import com.web.util.*;
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

import java.util.*;

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
    public int addPend(Pend pend,int type) {
        try {
            User user = userDao.findOneById(pend.getUid()).get(0);
            Activity activity = activityDao.findOneById(pend.getActivityId()).get(0);
            pend.setUsername(user.getName());
            pend.setActivityName(activity.getName());
            pend.setType(type);
            pendDao.save(pend);
            if (type==2){
                String msg = "志愿者，你好！发起者邀请你参加"+activity.getName()+"，请登录平台进入“我---收到的邀请”查看详情。";
                String title = "活动邀请|"+activity.getName();
                MailUtil.sendMail(MailUtil.ustbMail, MailUtil.ustbPwd, user.getMail(), title, msg);
            }
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
            Activity activity = activityDao.findOneById(pend.getActivityId()).get(0);
            User user = userDao.findOneById(pend.getUid()).get(0);
            if (activity == null || user == null)
                return 0;
            String newMember = ActivityUtil.addMember(activity.getMember(),pend.getUid(),user.getName());
            String newJoin = UserUtil.addJoin(user.getPartake(),pend.getActivityId());
            if (newMember == null || newJoin == null)
                return 0;
            activity.setMember(newMember);
            user.setPartake(newJoin);
            pendDao.delete(pend);
            activityDao.save(activity);
            userDao.save(user);
            if (pend.getType()!=2) {
                //发邮件通知
                String msg = "志愿者，恭喜！活动发起者已同意你的报名申请，请准时参加" + activity.getStartTime() + "开始的" + activity.getName() + "。";
                String title = activity.getName() + "活动报名结果反馈";
                MailUtil.sendMail(MailUtil.ustbMail, MailUtil.ustbPwd, user.getMail(), title, msg);
            }
            return 1;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public int reject(Pend pend) {
        try {
            Pend newPend = pendDao.findOneById(pend.getId()).get(0);
            newPend.setStatus(-1);
            pendDao.save(newPend);
            //发邮件通知
            Activity activity = activityDao.findOneById(pend.getActivityId()).get(0);
            User user = userDao.findOneById(pend.getUid()).get(0);
            if (pend.getType()!=2) {
                String msg = "志愿者，你好！很遗憾活动发起者拒绝了你对开始于" + activity.getStartTime() + "的" + activity.getName() + "的申请。";
                String title = activity.getName() + "活动报名结果反馈";
                MailUtil.sendMail(MailUtil.ustbMail, MailUtil.ustbPwd, user.getMail(), title, msg);
            }
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
                invite.put("url","/activityInfo?activityId=" + pend.getActivityId() + "&page=1" + "&pendId=" + pend.getId());
                inviteList.add(invite);
            }
            return inviteList;
        }catch (Exception e){
            logger.error("getInviteList|"+e.getMessage());
            return null;
        }
    }

    @Override
    public List<JSONObject> getApproveList(long activityId) {
        try {
            List<Pend> pendList = pendDao.getApproveList(activityId);
            List<JSONObject> approveList = new ArrayList<JSONObject>();
            for (Pend pend:pendList){
                JSONObject approve = new JSONObject();
                approve.put("name",pend.getUsername());
                approve.put("info","");
                approve.put("url","/userInfo?uid=" + pend.getUid() + "&page=5&pendId="+pend.getId()+"&activityId="+pend.getActivityId());
                approveList.add(approve);
            }
            return approveList;
        }catch (Exception e){
            logger.error("getApproveList|"+e.getMessage());
            return null;
        }
    }

    @Override
    public List<JSONObject> partakeList(long activityId) {
        try {
            List<JSONObject> partakeList = new ArrayList<JSONObject>();
            Activity activity = activityDao.findOneById(activityId).get(0);
            if (StringUtils.isNotBlank(activity.getMember())) {
                JSONObject jsonObject = JSON.parseObject(activity.getMember());
                List<JSONObject> otherList = (List<JSONObject>) jsonObject.get("memberList");
                if (otherList != null && otherList.size() > 0) {
                    for (JSONObject member:otherList){
                        member.put("info","");
                        member.put("url","/userInfo?uid="+member.getString("uid")+"&page=7");
                        member.put("name",member.getString("name"));
                        partakeList.add(member);
                    }
                }
            }
            return partakeList;
        }catch (Exception e){
            logger.error("partakeList|"+e.getMessage());
            return null;
        }
    }

    @Override
    public List<JSONObject> friendList(long uid, long activityId) {
        try {
            List<JSONObject> friendList = new ArrayList<JSONObject>();
            Map<String,JSONObject> friendMap = new HashMap<String, JSONObject>();
            User user = userDao.findOneById(uid).get(0);
            if (StringUtils.isNotBlank(user.getFriends())) {
                JSONObject jsonObject = JSON.parseObject(user.getFriends());
                List<JSONObject> otherList = (List<JSONObject>) jsonObject.get("friendList");
                if (otherList != null && otherList.size() > 0) {
                    for (JSONObject friend:otherList){
                        friend.put("info","");
                        friend.put("url","/userInfo?uid="+friend.getString("id")+"&page=4&activityId="+activityId);
                        friend.put("name",friend.getString("name"));
                        friendMap.put(friend.getString("id"),friend);
                    }
                }
                List<Pend> pendList = pendDao.getInviteFriend(activityId);
                for (Pend pend:pendList){
                    JSONObject friend = new JSONObject();
                    friend.put("info","已邀请，待回复");
                    if (pend.getStatus()==-1){
                        friend.put("info","已拒绝");
                    }
                    friend.put("url","/userInfo?uid="+pend.getUid()+"&page=6");
                    friend.put("name",pend.getUsername());
                    friendMap.put(pend.getUid()+"",friend);
                }
                Activity activity = activityDao.findOneById(activityId).get(0);
                if (StringUtils.isNotBlank(activity.getMember())) {
                    List<JSONObject> memberList = (List<JSONObject>) JSON.parseObject(activity.getMember()).get("memberList");
                    if (memberList != null && memberList.size() > 0) {
                        for (JSONObject member:memberList){
                            JSONObject friend = new JSONObject();
                            friend.put("info","已参加");
                            friend.put("url","/userInfo?uid="+member.getString("uid")+"&page=6");
                            friend.put("name",member.getString("name"));
                            friendMap.put(member.getString("uid"),friend);
                        }
                    }
                }
                Iterator iter = friendMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    JSONObject val = (JSONObject)entry.getValue();
                    friendList.add(val);
                }
            }
            return friendList;
        }catch (Exception e){
            logger.error("friendList|"+e.getMessage());
            return null;
        }
    }

    @Override
    public List<JSONObject> ensureList(long activityId) {
        try {
            List<JSONObject> ensureList = new ArrayList<JSONObject>();
            List<JSONObject> isSure = new ArrayList<JSONObject>();
            Activity activity = activityDao.findOneById(activityId).get(0);
            if (StringUtils.isNotBlank(activity.getMember())) {
                JSONObject jsonObject = JSON.parseObject(activity.getMember());
                List<JSONObject> otherList = (List<JSONObject>) jsonObject.get("memberList");
                if (otherList != null && otherList.size() > 0) {
                    for (JSONObject member:otherList){
                        member.put("url","/userInfo?uid="+member.getString("uid")+"&page=7");
                        member.put("name",member.getString("name"));
                        if (CacheUtil.getCache(member.getString("uid")+"ensure"+activityId)==null){
                            member.put("info","");
                            ensureList.add(member);
                        }else {
                            member.put("info","已确认");
                            isSure.add(member);
                        }
                    }
                    ensureList.addAll(isSure);
                }
            }
            return ensureList;
        }catch (Exception e){
            logger.error("ensureList|"+e.getMessage());
        }
        return null;
    }

    @Override
    public List<JSONObject> signList(long activityId) {
        try {
            List<JSONObject> signList = new ArrayList<JSONObject>();
            List<JSONObject> sign1 = new ArrayList<JSONObject>();
            List<JSONObject> sign2 = new ArrayList<JSONObject>();
            Activity activity = activityDao.findOneById(activityId).get(0);
            if (StringUtils.isNotBlank(activity.getMember())) {
                JSONObject jsonObject = JSON.parseObject(activity.getMember());
                List<JSONObject> otherList = (List<JSONObject>) jsonObject.get("memberList");
                if (otherList != null && otherList.size() > 0) {
                    for (JSONObject member:otherList){
                        if (member.getString("status").equals("0")) {
                            member.put("info", "未签到");
                            member.put("url","/userInfo?uid="+member.getString("uid")+"&page=1");
                            sign2.add(member);
                        }else if (member.getString("status").equals("1")) {
                            member.put("info", "对方已签到，点击确认");
                            member.put("url","/userInfo?uid="+member.getString("uid")+"&page=3&activityId="+activityId);
                            signList.add(member);
                        }else if (member.getString("status").equals("2")) {
                            member.put("info", "已确认签到");
                            member.put("url","/userInfo?uid="+member.getString("uid")+"&page=7");
                            sign1.add(member);
                        }
                        member.put("name",member.getString("name"));
                    }
                    signList.addAll(sign1);
                    signList.addAll(sign2);
                }
            }
            return signList;
        }catch (Exception e){
            logger.error("signList|"+e.getMessage());
        }
        return null;
    }

    @Override
    public boolean signIn(long uid, long activityId,int type) {
        try {
            Activity activity = activityDao.findOneById(activityId).get(0);
            if (StringUtils.isNotBlank(activity.getMember())) {
                JSONObject jsonObject = JSON.parseObject(activity.getMember());
                List<JSONObject> memberList = (List<JSONObject>) jsonObject.get("memberList");
                if (memberList != null && memberList.size() > 0) {
                    for (int i=0;i<memberList.size();i++){
                        JSONObject member = memberList.get(i);
                        Number memberId = (Number)member.get("uid");
                        if (uid==memberId.longValue()) {
                            if (type == 0) {//签到
                                member.put("status", "1");
                                CacheUtil.putCache(uid + "sign" + activityId, "1", CacheUtil.MEMCACHED_ONE_DAY);
                            } else if (type == 1) {//确认签到
                                member.put("status", "2");
                            }
                            memberList.remove(i);
                            memberList.add(member);
                            jsonObject.put("memberList",memberList);
                            activity.setMember(jsonObject.toJSONString());
                            activityDao.save(activity);
                            return true;
                        }
                    }
                }
            }
            return false;
        }catch (Exception e){
            logger.error("signIn|"+e.getMessage());
        }
        return false;
    }
}
