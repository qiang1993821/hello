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

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        logger.error("lllllllllllllllll");
        return "login";
    }
}
