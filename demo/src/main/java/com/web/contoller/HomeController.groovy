package com.web.contoller

import com.web.service.impl.ActivityServiceImpl
import com.web.service.impl.UserServiceImpl
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
        model.put("uid",uid);
        model.put("mail",mail);
        return "manual"
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

    //成员列表页
    @RequestMapping(value = "/member")
    public String member(Map<String, Object> model,
                        @RequestParam(value = "activityId") Long activityId) {
        def memberList = activityService.getMemberList(activityId)
        model.put("memberList",memberList)
        return "login"
    }

    //用户详情页
    @RequestMapping(value = "/userInfo")
    public String userInfo(Map<String, Object> model,
                         @RequestParam(value = "uid") Long uid) {
        def user = userService.findOneUser(uid)
        model.put("academy",StringUtils.isBlank(user.academy)?"未填写":user.academy)
        model.put("className",StringUtils.isBlank(user.className)?"未填写":user.className)
        model.put("wechat",StringUtils.isBlank(user.wechat)?"未填写":user.wechat)
        model.put("phone",StringUtils.isBlank(user.phone)?"未填写":user.phone)
        model.put("mail",StringUtils.isBlank(user.mail)?"未填写":user.mail)
        model.put("sex",user.sex==0?"女":"男")
        model.put("id",user.id)
        return "userInfo"
    }

    //活动详情页
    @RequestMapping(value = "/activityInfo")
    public String activityInfo(Map<String, Object> model,
                           @RequestParam(value = "activityId") Long activityId) {
        return "activityInfo"
    }
}
