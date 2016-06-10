package com.web.contoller

import com.web.domain.Activity
import com.web.domain.Pend
import com.web.service.impl.ActivityServiceImpl
import com.web.service.impl.UserServiceImpl
import com.web.util.CacheUtil
import groovy.json.JsonBuilder
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
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
    @Autowired
    private UserServiceImpl userService

    /**
     * 发起活动
     * @param activity
     * @return
     */
    @RequestMapping(value = "/launch",method = RequestMethod.POST)
    String launch(Activity activity){
        def map = [:]
        if (activity.sponsor==0 || StringUtils.isBlank(activity.name) || StringUtils.isBlank(activity.startTime) || StringUtils.isBlank(activity.endTime)){
            map.put("msg","信息有误，发起活动失败！")
            map.put("code",0)
        }else if (!userService.isFullInfo(activity.sponsor)){//先判断发起人信息是否完整
            map.put("msg","请完善个人后再发起（姓名、邮箱、手机）！")
            map.put("code",0)
        }else{
            def type = activityService.launch(activity)
            map.put("code",type)
            if (type == 1)
                map.put("msg","活动发起成功！")
            else
                map.put("msg","活动发起异常！")
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
            map.put("code",0)
        }else if (!userService.isFullInfo(pend.uid)){//先判断报名人信息是否完整
            map.put("msg","请完善个人后再报名（姓名、邮箱、手机）！")
            map.put("code",0)
        }else if (activityService.hasJoined(pend.uid,pend.activityId)){//先判断报名人是否已经参与
            map.put("msg","您曾报名|受邀|参与|发起过该活动！")
            map.put("code",0)
        }else {
            def user = userService.findOneUser(pend.uid)
            pend.username = user.name
            activityService.addPend(pend,1)
            map.put("msg","报名成功！")
            map.put("code",1)
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 邀请好友
     * @param pend
     * @param request
     * @return
     */
    @RequestMapping(value = "/invite")
    String invite(Pend pend,HttpServletRequest request){
        def map = [:]
        if (StringUtils.isBlank(request.getParameter("uid")) || StringUtils.isBlank(request.getParameter("activityId"))){
            map.put("msg","信息错误，邀请朋友失败！")
            map.put("code",0)
        }else if (!userService.isFullInfo(pend.uid)){//先判断报名人信息是否完整
            map.put("msg","该好友资料不完善，无法邀请！")
            map.put("code",0)
        }else if (activityService.hasJoined(pend.uid,pend.activityId)){//先判断报名人是否已经参与
            map.put("msg","该好友曾报名|受邀|参与|发起过该活动！")
            map.put("code",0)
        }else {
            activityService.addPend(pend,2)
            map.put("msg","邀请成功！")
            map.put("code",1)
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 审批
     * @param pend
     * @param request
     * @return
     */
    @RequestMapping(value = "/approveUser")
    String approveUser(Pend pend,HttpServletRequest request,@RequestParam(value = "result",defaultValue = "0") Integer result){
        def map = [:]
        def type
        if (StringUtils.isBlank(request.getParameter("id")) || StringUtils.isBlank(request.getParameter("uid"))
                || StringUtils.isBlank(request.getParameter("activityId"))){
            map.put("msg","信息错误，审批失败！")
            map.put("type",0)
        }else {
            if (result == null || result == 0) {//拒绝
                type = activityService.reject(pend)
            }else {//批准
                type = activityService.approve(pend)
            }
            map.put("msg","")
            if (type != 1){
                map.put("msg","审批过程发生异常！")
            }
            map.put("code",type)
        }
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/searchAC")
    String searchAC(@RequestParam(value = "page",defaultValue = "0") Integer page){
        def map = [:]
        def activityList = activityService.queryByPage(page)
        map.put("activityList",activityList)
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/queryByName")
    String queryByName(@RequestParam(value = "name") String name){
        def map = [:]
        def activityList = activityService.queryByName(name)
        map.put("activityList",activityList)
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/fuzzyQueryAC")
    String fuzzyQueryAC(@RequestParam(value = "name") String name){
        def map = [:]
        def nameList = activityService.fuzzyQuery(name)
        map.put("nameList",nameList)
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/signIn")
    String signIn(@RequestParam(value = "activityId") Long activityId,
                  @RequestParam(value = "uid") Long uid,
                  @RequestParam(value = "type") Integer type){
        def map = [:]
        if (type==0&&CacheUtil.getCache(uid+"sign"+activityId)!=null){
            map.put("msg","您已签到过，不必重复操作！");
            map.put("code",0);
            return new JsonBuilder(map).toString()
        }else {
            if (activityService.signIn(uid,activityId,type)){
                map.put("msg","");
                map.put("code",1);
            }else {
                map.put("msg","签到发生异常，请退出重试！");
                map.put("code",0);
            }
        }
        return new JsonBuilder(map).toString()
    }
}
