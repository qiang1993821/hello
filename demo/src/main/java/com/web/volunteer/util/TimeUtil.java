package com.web.volunteer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/26.
 */
public class TimeUtil {
    private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);
    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    /**
     * 修改活动状态
     * @param startTime
     * @param endTime
     * @return
     */
    public static int changeStatus(String startTime,String endTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date current = df.parse(df.format(new Date()));
            Date start = df.parse(startTime);
            Date end = df.parse(endTime);
            long toEnd = end.getTime()-current.getTime();
            long toStart = start.getTime()-current.getTime();
            if (toEnd<=0){
                return 4;
            }else if (toStart<=0){
                return 3;
            }else if ((toStart / (1000 * 60 * 60 * 24))==0){
                return 2;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return 1;
    }

}
