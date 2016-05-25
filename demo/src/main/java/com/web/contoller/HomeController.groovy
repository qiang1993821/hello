package com.web.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@EnableAutoConfiguration
@RequestMapping("/home")
public class HomeController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @RequestMapping(value = "/testMail")
    public String getUser(Map<String, Object> model,
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
}
