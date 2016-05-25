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
    public static String addMember(String memberStr,long uid){
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
}
