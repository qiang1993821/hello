package com.web.contoller;

import com.web.util.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


@Controller
@EnableAutoConfiguration
@RequestMapping("/users")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(TestController.class);
    @RequestMapping(value = "/aaa", method = RequestMethod.GET)
    public String getUser() {
        logger.error("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        return "main";
    }

}
