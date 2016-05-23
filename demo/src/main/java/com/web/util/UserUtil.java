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
            logger.error(e.getMessage());
            return null;
        }
    }
}
