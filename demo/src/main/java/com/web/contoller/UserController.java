package com.web.contoller;

import com.web.util.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;


@RestController
@EnableAutoConfiguration
@RequestMapping("/users")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${my.secret}")
    String num;
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String getUser(@PathVariable String name) {
        CacheUtil.putCache("a","hello",CacheUtil.MEMCACHED_ONE_DAY);
        logger.error("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        return name+num+(String)CacheUtil.getCache("a");
    }

}
