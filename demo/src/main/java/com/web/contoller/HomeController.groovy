package com.web.contoller

import com.web.service.impl.ActivityServiceImpl
import com.web.service.impl.UserServiceImpl
import com.web.util.UserUtil
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@EnableAutoConfiguration
@RequestMapping("/")
public class HomeController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private ActivityServiceImpl activityService;
    @Autowired
    private UserServiceImpl userService

    @RequestMapping(value = "/testMail")
    public String testMail(Map<String, Object> model,
                          @RequestParam(value = "uid") Long uid,
                          @RequestParam(value = "mail") String mail) {
        try {
            def user = userService.findOneUser(uid)
            user.mail = mail
            userService.save(user)
            model.put("result","验证成功！");
        }catch (Exception e){
            model.put("result","验证失败，请重试，若已尝试多次，请返回平台再次发送验证邮件！");
        }
        return "result"
    }

    //登录页
    @RequestMapping(value = "/login")
    public String login(Map<String, Object> model,
                        @RequestParam(value = "activityId", required = false) Long activityId) {
        if (activityId!=null) {
            model.put("activityId", activityId);//在jsp写个hidden
        }else {
            model.put("activityId", 0);
        }
        return "login"
    }

    //主页
    @RequestMapping(value = "/index")
    public String index(Map<String, Object> model,
                        @RequestParam(value = "uid") Long uid) {
        def user = userService.findOneUser(uid);
        model.put("name",user.name);
        model.put("uid",uid);
        return "index"
    }

    //发起的
    @RequestMapping(value = "/launch_user")
    public String userLaunch(Map<String, Object> model,
                        @RequestParam(value = "uid") Long uid,
                        @RequestParam(value = "from",required = false) Integer from) {
        if (from==null)
            from = 0
        def launchList = userService.getActivityList(uid,"launch",from)
        model.put("infoList",launchList)
        return "infoList"
    }

    //参与的
    @RequestMapping(value = "/partake_user")
    public String userPartake(Map<String, Object> model,
                             @RequestParam(value = "uid") Long uid,
                             @RequestParam(value = "from",required = false) Integer from) {
        if (from==null)
            from = 0
        def launchList = userService.getActivityList(uid,"partake",from)
        model.put("infoList",launchList)
        return "infoList"
    }

    //成员列表页
    @RequestMapping(value = "/member")
    public String member(Map<String, Object> model,
                         @RequestParam(value = "activityId") Long activityId) {
        def memberList = activityService.getMemberList(activityId)
        model.put("infoList",memberList)
        return "infoList"
    }

    //好友列表页
    @RequestMapping(value = "/friendList")
    public String friendList(Map<String, Object> model,
                         @RequestParam(value = "uid") Long uid) {
        def user = userService.findOneUser(uid)
        def friendList = UserUtil.getFriendList(user.friends)
        model.put("infoList",friendList)
        return "infoList"
    }

    //收到邀请的活动列表页
    @RequestMapping(value = "/inviteList")
    public String inviteList(Map<String, Object> model,
                         @RequestParam(value = "uid") Long uid) {
        def inviteList = activityService.getInviteList(uid)
        model.put("infoList",inviteList)
        return "infoList"
    }

    //报名申请的用户列表页
    @RequestMapping(value = "/approveList")
    public String approveList(Map<String, Object> model,
                             @RequestParam(value = "activityId") Long activityId) {
        def approveList = activityService.getApproveList(activityId)
        model.put("infoList",approveList)
        return "infoList"
    }

    //我发起的活动里看成员列表页
    @RequestMapping(value = "/partakeList")
    public String partakeList(Map<String, Object> model,
                              @RequestParam(value = "activityId") Long activityId) {
        def approveList = activityService.partakeList(activityId)
        model.put("infoList",approveList)
        return "infoList"
    }

    //招募中活动邀请朋友列表页
    @RequestMapping(value = "/inviteFriend")
    public String inviteFriend(Map<String, Object> model,
                              @RequestParam(value = "activityId") Long activityId,
                              @RequestParam(value = "uid") Long uid) {
        def friendList = activityService.friendList(uid,activityId)
        model.put("infoList",friendList)
        return "infoList"
    }

    //用户详情页
    @RequestMapping(value = "/userInfo")
    public String userInfo(Map<String, Object> model,
                         @RequestParam(value = "uid") Long uid,
                         @RequestParam(value = "page") Integer page,
                         @RequestParam(value = "pendId", required = false) Long pendId,
                         @RequestParam(value = "activityId", required = false) Long activityId) {
        def user = userService.findOneUser(uid)
        model.put("name",user.name)
        model.put("academy",StringUtils.isBlank(user.academy)?"未填写":user.academy)
        model.put("className",StringUtils.isBlank(user.className)?"未填写":user.className)
        model.put("sex",user.sex==0?"女":"男")
        model.put("page",page)
        model.put("uid",user.id)
        if (page==null || page%2==0){
            model.put("wechat",StringUtils.isBlank(user.wechat)?"未填写":(user.show==0?"保密":user.wechat))
            model.put("phone",StringUtils.isBlank(user.phone)?"未填写":(user.show==0?"保密":user.phone))
            model.put("mail",StringUtils.isBlank(user.mail)?"未填写":(user.show==0?"保密":user.mail))
        }else {//从我发起的查看成员，确认签到，审批报名三个情景可见信息
            model.put("wechat",StringUtils.isBlank(user.wechat)?"未填写":user.wechat)
            model.put("phone",StringUtils.isBlank(user.phone)?"未填写":user.phone)
            model.put("mail",StringUtils.isBlank(user.mail)?"未填写":user.mail)
        }
        if (pendId!=null)
            model.put("pendId", pendId)
        if (activityId!=null)
            model.put("activityId", activityId)
        return "userInfo"
    }

    //活动详情页
    @RequestMapping(value = "/activityInfo")
    public String activityInfo(Map<String, Object> model,
                           @RequestParam(value = "activityId") Long activityId,
                           @RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "pendId", required = false) Long pendId) {
        def activity = activityService.getActivityInfo(activityId)
        if (activity==null) {
            model.put("result","该活动不存在！")
            return "result"
        }
        model.put("activity",activity)
        if (page==null)
            page = 0
        if (pendId!=null)
            model.put("pendId", pendId)
        model.put("page",page)
        return "activityInfo"
    }

    //我发起的正在审核活动页
    @RequestMapping(value = "/activity0")
    public String activity0(Map<String, Object> model,
                         @RequestParam(value = "activityId") Long activityId) {
        def activity = activityService.findOneById(activityId)
        if (activity==null) {
            model.put("result","该活动不存在！")
            return "result"
        }
        model.put("activityId",activityId)
        model.put("name",activity.name)
        model.put("startTime",activity.startTime)
        model.put("endTime",activity.endTime)
        model.put("hour",activity.hour)
        model.put("details",activity.details)
        return "activity0"
    }

    //我发起的招募中活动页
    @RequestMapping(value = "/activity1")
    public String activity1(Map<String, Object> model,
                            @RequestParam(value = "activityId") Long activityId) {
        def activity = activityService.findOneById(activityId)
        if (activity==null) {
            model.put("result","该活动不存在！")
            return "result"
        }
        model.put("activityId",activityId)
        model.put("name",activity.name)
        model.put("sponsor",activity.sponsor)
        return "activity1"
    }

    //我发起的即将开始活动页
    @RequestMapping(value = "/activity2")
    public String activity2(Map<String, Object> model,
                            @RequestParam(value = "activityId") Long activityId) {
        def activity = activityService.findOneById(activityId)
        if (activity==null) {
            model.put("result","该活动不存在！")
            return "result"
        }
        model.put("activityId",activityId)
        model.put("name",activity.name)
        model.put("startTime",activity.startTime)
        model.put("endTime",activity.endTime)
        return "activity2"
    }

    //我发起的正在进行活动页
    @RequestMapping(value = "/activity3")
    public String activity3(Map<String, Object> model,
                             @RequestParam(value = "activityId") Long activityId) {
        //获取签到情况列表
        model.put("infoList",null)
        return "infoList"
    }

    //发起活动页
    @RequestMapping(value = "/launch")
    public String launch(Map<String, Object> model) {
        model.put("activityId","")
        return "activity_change"
    }

    //修改个人信息
    @RequestMapping(value = "/editUser")
    public String editUser(Map<String, Object> model,
                           @RequestParam(value = "uid") Long uid) {
        def user = userService.findOneUser(uid)
        model.put("name",user.name)
        model.put("academy",user.academy)
        model.put("academyList",UserUtil.getAcademyList())
        model.put("className",user.className)
        model.put("sex",user.sex)
        model.put("show",user.show)
        model.put("wechat",user.wechat)
        model.put("phone",user.phone)
        model.put("mail",user.mail)
        return "user_change"
    }

}
