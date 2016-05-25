package com.web.contoller

import com.web.service.impl.ActivityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@EnableAutoConfiguration
@RequestMapping("/home")
public class HomeController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private ActivityServiceImpl activityService;

    @RequestMapping(value = "/testMail")
    public String testMail(Map<String, Object> model,
                          @RequestParam(value = "uid") Long uid,
                          @RequestParam(value = "mail") String mail) {
        model.put("uid",uid);
        model.put("mail",mail);
        return "manual";
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
        return "login";
    }

    //主页
    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    //成员列表页
    @RequestMapping(value = "/member")
    public String member(Map<String, Object> model,
                        @RequestParam(value = "activityId") Long activityId) {
        def memberList = activityService.getMemberList(activityId)
        model.put("memberList",memberList)
        return "login";
    }
}
