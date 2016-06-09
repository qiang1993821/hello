package com.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.web.domain.Activity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ActivityUtil {
    private static final Logger logger = LoggerFactory.getLogger(ActivityUtil.class);

    /**
     * 给活动新加成员
     * @param memberStr
     * @param uid
     * @return
     */
    public static String addMember(String memberStr,long uid,String name){
        try {
            JSONObject member = null;
            List<JSONObject> memberList = null;
            if (StringUtils.isBlank(memberStr)){
                member = new JSONObject();
                memberList = new ArrayList<JSONObject>();
            }else {
                member = JSON.parseObject(memberStr);
                memberList = (List<JSONObject>) member.get("memberList");
            }
            JSONObject user = new JSONObject();
            user.put("uid", uid);
            user.put("name",name);
            user.put("status", 0);
            memberList.add(user);
            member.put("memberList", memberList);
            return member.toJSONString();
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 将活动的部分需要展示的属性封装成JSON对象
     * @param activity
     * @return
     */
    public static JSONObject activityToJSON(Activity activity){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", activity.getName());
        jsonObject.put("hour", activity.getHour());
        jsonObject.put("id", activity.getId());
        String status = "";
        switch (activity.getStatus()){
            case 0:status = "审核中";break;
            case 1:status = "招募中";break;
            case 2:status = "即将开始";break;
            case 3:status = "正在进行";break;
            case 4:status = "已结束";break;
        }
        jsonObject.put("status",status);
        return jsonObject;
    }

    public static List<JSONObject> getMemberList(List<JSONObject> list){
        List<JSONObject> newList = new ArrayList<JSONObject>();
        for (JSONObject member:list){
            member.put("info","");
            member.put("url","/userInfo?uid="+member.getString("uid")+"&page=0");
            member.put("name",member.getString("name"));
            newList.add(member);
        }
        return newList;
    }

    public static List<JSONObject> getActivityList(List<Activity> list,int from){
        List<JSONObject> newList = new ArrayList<JSONObject>();
        if (from==0) {
            for (Activity activity : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("info", ActivityUtil.statusToString(activity.getStatus()));
                jsonObject.put("url", "/activityInfo?activityId=" + activity.getId() + "&page=0");
                jsonObject.put("name", activity.getName());
                newList.add(jsonObject);
            }
        }else if (from==1){
            for (Activity activity : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("info", ActivityUtil.statusToString(activity.getStatus()));
                int page = 2;
                if (activity.getStatus()==4)
                    page = 5;
                else if (activity.getStatus()==3)
                    page = 3;
                jsonObject.put("url", "/activityInfo?activityId=" + activity.getId() + "&page=" + page);
                jsonObject.put("name", activity.getName());
                newList.add(jsonObject);
            }
        }else if (from==2){
            for (Activity activity : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("info", ActivityUtil.statusToString(activity.getStatus()));
                String url = "/activityInfo?activityId=" + activity.getId();
                switch (activity.getStatus()){
                    case 0:url = "/activity0?activityId=" + activity.getId();break;
                    case 1:url = "/activity1?activityId=" + activity.getId();break;
                    case 2:url = "/activity2?activityId=" + activity.getId();break;
                    case 3:url = "/activity3?activityId=" + activity.getId();break;
                    case 4:url = "/activityInfo?activityId=" + activity.getId() + "&page=4";break;
                }
                jsonObject.put("url", url);
                jsonObject.put("name", activity.getName());
                newList.add(jsonObject);
            }
        }
        return newList;
    }

    public static String statusToString(int status){

        switch (status){
            case 0:return "审核中";
            case 1:return "招募中";
            case 2:return "即将开始";
            case 3:return "正在进行";
            case 4:return "已结束";
            default:return "";
        }
    }

}
