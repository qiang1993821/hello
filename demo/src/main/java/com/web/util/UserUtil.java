package com.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserUtil {
    private static final Logger logger = LoggerFactory.getLogger(UserUtil.class);
    private static final List<String> academyList = new ArrayList<String>();

    static {
        academyList.add("土木与环境工程学院");
        academyList.add("冶金与生态工程学院");
        academyList.add("材料科学与工程学院");
        academyList.add("机械工程学院");
        academyList.add("自动化学院");
        academyList.add("计算机与通信工程学院");
        academyList.add("数理学院");
        academyList.add("化学与生物工程学院");
        academyList.add("东凌经济管理学院");
        academyList.add("文法学院");
        academyList.add("马克思主义学院");
        academyList.add("外国语学院");
        academyList.add("高等工程师学院");
    }

    public static List<String> getAcademyList(){
        return academyList;
    }

    /**
     * 新增加活动
     * @param joinStr
     * @param activityId
     * @return
     */
    public static String addJoin(String joinStr,long activityId){
        try {
            JSONObject join = null;
            List<Long> joinList = null;
            if (StringUtils.isBlank(joinStr)){
                join = new JSONObject();
                joinList = new ArrayList<Long>();
            }else {
                join = JSON.parseObject(joinStr);
                joinList = (List<Long>) join.get("activityList");
            }
            joinList.add(activityId);
            join.put("activityList", joinList);
            return join.toJSONString();
        }catch (Exception e){
            logger.error("util_addJoin|"+e.getMessage());
            return null;
        }
    }

    /**
     * 添加或删除好友
     * @param friendStr
     * @param status
     * @return
     */
    public static String doFriend(String friendStr,long friendId,String name,boolean status){
        try {
            JSONObject jsonObject = null;
            List<JSONObject> friendList = null;
            if (status){//添加
                if (StringUtils.isBlank(friendStr)){
                    jsonObject = new JSONObject();
                    friendList = new ArrayList<JSONObject>();
                }else {
                    jsonObject = JSON.parseObject(friendStr);
                    friendList = (List<JSONObject>)jsonObject.get("friendList");
                }
                JSONObject friend = new JSONObject();
                friend.put("id",friendId);
                friend.put("name",name);
                friendList.add(friend);
            }else {//删除
                jsonObject = JSON.parseObject(friendStr);
                friendList = (List<JSONObject>)jsonObject.get("friendList");
                for (int i = 0; i < friendList.size(); i++){
                    JSONObject friend = friendList.get(i);
                    if (friend.getLongValue("id")==friendId){
                        friendList.remove(i);
                        break;
                    }
                }
            }
            jsonObject.put("friendList",friendList);
            return jsonObject.toJSONString();
        }catch (Exception e){
            logger.error("util_doFriend"+e.getMessage());
        }
        return friendStr;
    }

    public static List<JSONObject> getFriendList(String friendStr){
        try {
            List<JSONObject> friendList = new ArrayList<JSONObject>();
            List<JSONObject> friends = (List<JSONObject>)JSON.parseObject(friendStr).get("friendList");
            for (int i=friends.size()-1;i>=0;i--){
                JSONObject user = new JSONObject();
                user.put("name", friends.get(i).getString("name"));
                user.put("info","");
                user.put("url","/userInfo?uid="+friends.get(i).getString("id")+"&page=2");
                friendList.add(user);
            }
            return friendList;
        }catch (Exception e){
            logger.error("util_getFriendList"+e.getMessage());
        }
        return null;
    }
}
