package com.web.contoller

import com.web.domain.User
import com.web.service.impl.UserServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

/**
 * Created by Administrator on 2016/4/14.
 */
@Controller
@EnableAutoConfiguration
class Test {
    private final Logger logger = LoggerFactory.getLogger(Test.class);
    @Autowired
    private UserServiceImpl userService
    @RequestMapping("/hello")
    String hello(){
        "hello world, bye bye kobe."
    }
    @RequestMapping("/sql")
    String sql(){
        def result = ""
        def user = new User()
        user.name = "索隆"
        user.token = "zoro"
        result += userService.save(user)
        return result
    }
    @RequestMapping("/jsp")
    public String welcome() {
        logger.error("kkkkkkkkkkkkkkkkkkkkkkkkk")
        return "main"
    }

    @RequestMapping("/main")
    public ModelAndView welcome1() {
        logger.error("111111111111111")
        return new ModelAndView("/main")
    }

}
