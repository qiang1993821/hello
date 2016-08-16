package com.web.joke.controller

import com.web.joke.service.impl.JokeUserServiceImpl
import com.web.joke.util.MailUtil
import com.web.joke.util.UserUtil
import com.web.volunteer.util.CacheUtil
import groovy.json.JsonBuilder
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

/**
 * Created by Administrator on 2016/4/26.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/joke/user")
class JokeUserController {
    private final Logger logger = LoggerFactory.getLogger(JokeUserController.class);
    @Autowired
    private JokeUserServiceImpl userService


    /**
     * 登录接口
     * @param username
     * @return
     */
    @RequestMapping(value = "/login")
    String login(@RequestParam(value = "username") String username,
                 @RequestParam(value = "pwd") String pwd){
        def map = [:]
        if (StringUtils.isBlank(username) || StringUtils.isBlank(pwd)){
            map.put("result","微信登录异常，请重新登录！")
            map.put("code",0)
        }else {
            def user = userService.getUserByMail(username)
            def type = 0
            def result
            def random = UserUtil.getRondomNum()//随机数，增大安全性，否则不登陆邮箱知道注册激活链接也能注册
            if (user == null){
                if (CacheUtil.getCache("jregTimes"+username)==null)
                    CacheUtil.putCache("jregTimes"+username,0,CacheUtil.MEMCACHED_ONE_DAY)
                int times = CacheUtil.getCache("jregTimes"+username)
                if (times<5) {
                    CacheUtil.putCache("j" + username, pwd, CacheUtil.MEMCACHED_ONE_DAY * 3)
                    CacheUtil.putCache("jrandom-" + username, random, CacheUtil.MEMCACHED_ONE_DAY * 3)
                    def msg = "少年，开启你的伟大航路吧，<a href=\"http://www.ustbvolunteer.com/joke/reg?mail=" + username + "&random=" + random + "\">点击完成注册</a>，若非本人操作请忽略！（此邮件三日内有效）"
                    def title = "恶作剧弹窗注册激活"
                    if (MailUtil.sendMail(MailUtil.ustbMail, MailUtil.ustbPwd, username, title, msg)) {
                        result = "此邮箱尚未注册，已发送注册邮件，请尽快登录完成注册！"
                    } else {
                        result = "此邮箱尚未注册，注册邮件发送失败！"
                    }
                    times++
                    CacheUtil.putCache("jregTimes"+username,times,CacheUtil.MEMCACHED_ONE_DAY)
                }else {
                    result = "此邮箱尚未注册，注册邮件发送次数已达今日上限！"
                }
            }else {
                if (user.pwd == pwd){
                    type = 1
                    result = user.id
                }else {
                    result = "密码错误！"
                }
            }
            map.put("code",type)
            map.put("result",result)
        }
        return new JsonBuilder(map).toString()
    }
}
