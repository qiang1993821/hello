package com.web.contoller

import com.web.domain.User
import com.web.service.impl.UserServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Administrator on 2016/4/14.
 */
@RestController
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
        user.setName("山治")
        user.setJob("厨师")
        result += userService.save(user)
        return result
    }
}
