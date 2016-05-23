package com.web.contoller

import com.web.domain.Activity
import com.web.domain.Pend
import com.web.service.impl.ActivityServiceImpl
import groovy.json.JsonBuilder
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

/**
 * Created by qiangyipeng on 2016/5/22.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/activity")
class ActivityController {
    private final Logger logger = LoggerFactory.getLogger(ActivityController.class);
    @Autowired
    private ActivityServiceImpl activityService;

    /**
     * 发起活动
     * @param activity
     * @return
     */
    @RequestMapping(value = "/launch")
    String launch(Activity activity){
        def map = [:]
        if (activity.sponsor==0 || StringUtils.isBlank(activity.name)){
            map.put("msg","信息有误，发起活动失败！")
            map.put("type",0)
        }else {
            activityService.save(activity)
            map.put("msg","活动发起成功！")
            map.put("type",1)
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 修改活动内容
     * @param activity
     * @return
     */
    @RequestMapping(value = "/saveActivity")
    String saveActivity(Activity newActivity,HttpServletRequest request){
        def map = [:]
        try {
            def activity = activityService.findOneById(newActivity.id)
            activity.name = StringUtils.isBlank(newActivity.name)?activity.name:newActivity.name
            activity.hour = StringUtils.isBlank(request.getParameter("hour"))?activity.hour:newActivity.hour
            activity.startTime = StringUtils.isBlank(newActivity.startTime)?activity.startTime:newActivity.startTime
            activity.endTime = StringUtils.isBlank(newActivity.endTime)?activity.endTime:newActivity.endTime
            activity.details = StringUtils.isBlank(newActivity.details)?activity.details:newActivity.details
            activityService.save(activity)
            map.put("msg","更新活动信息成功！")
            map.put("type",1)
        }catch (Exception e){
            logger.error(e.message)
            map.put("msg","更新活动信息失败！")
            map.put("type",0)
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 报名活动
     * @param pend
     * @param request
     * @return
     */
    @RequestMapping(value = "/join")
    String join(Pend pend,HttpServletRequest request){
        def map = [:]
        if (StringUtils.isBlank(request.getParameter("uid")) || StringUtils.isBlank(request.getParameter("activityId"))){
            map.put("msg","信息错误，加入活动失败！")
            map.put("type",0)
        }else {
            activityService.join(pend)
            map.put("msg","报名成功！")
            map.put("type",1)
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 审批活动报名
     * @param pend
     * @param request
     * @return
     */
    @RequestMapping(value = "/approveUser")
    String approveUser(Pend pend,HttpServletRequest request){
        def map = [:]
        def status = request.getParameter("status")
        def type
        if (StringUtils.isBlank(request.getParameter("id")) || StringUtils.isBlank(request.getParameter("uid"))
                || StringUtils.isBlank(request.getParameter("activityId")) || StringUtils.isBlank(status)){
            map.put("msg","信息错误，审批失败！")
            map.put("type",0)
        }else {
            if (status == null || status == 0) {//拒绝
                type = activityService.reject(pend)
            }else {//批准
                type = activityService.approve(pend)
            }
            if (type == 1){
                map.put("msg","审批成功！")
            }else {
                map.put("msg","审批异常！")
            }
            map.put("type",type)
        }
        return new JsonBuilder(map).toString()
    }
}