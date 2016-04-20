package com.web.contoller

import com.web.domain.User
import com.web.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Administrator on 2016/4/14.
 */
@RestController
class Test {
    @Autowired
    private UserService userService
    @RequestMapping("/hello")
    String hello(){
        "hello world, bye bye kobe."
    }
    @RequestMapping("/sql")
    String sql(){
        def result = ""
        def user = new User()
        user.setName("索隆")
        user.setJob("大副")
        result += userService.save()
        return result
    }
}
