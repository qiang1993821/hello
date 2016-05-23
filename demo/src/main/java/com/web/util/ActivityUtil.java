package com.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ActivityUtil {
    private static final Logger logger = LoggerFactory.getLogger(ActivityUtil.class);

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
}
