package com.web.contoller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Administrator on 2016/4/14.
 */
@RestController
class Test {
    @RequestMapping("/hello")
    String hello(){
        "hello world, bye bye kobe."
    }
}
