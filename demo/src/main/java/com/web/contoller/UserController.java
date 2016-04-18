package com.web.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;


@RestController
@EnableAutoConfiguration
@RequestMapping("/users")
public class UserController {
    protected final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${my.secret}")
    String num;
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String getUser(@PathVariable String name) {
        logger.error("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        return name+num;
    }

}
