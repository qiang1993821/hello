package com.web.contoller

import com.web.domain.User
import com.web.service.impl.UserServiceImpl
import com.web.util.CacheUtil
import com.web.util.HttpJsonUtil
import com.web.util.TimeUtil
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import groovy.json.JsonBuilder
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Administrator on 2016/4/26.
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/user")
class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserServiceImpl userService
    /**
     * 作为微信授权接口的回调url，会返回几个参数，code用来换取access_token
     * 返回后前端存上cookie
     * @param token
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    ModelAndView index(@RequestParam(value = "code",required = false) String code,
                       @RequestParam(value = "state") String state){
        def modelAndView
        if (StringUtils.isBlank(code)){
            modelAndView = new ModelAndView("/error")
            modelAndView.addObject("erroInfo", "微信登录异常，请重新登录！")
        }else {
            def user = HttpJsonUtil.getUserByCode(code)
            if (user == null){
                modelAndView = new ModelAndView("/error")
                modelAndView.addObject("erroInfo", "微信获取信息异常，请重新登录！")
            }else {

            }
            modelAndView = new ModelAndView("/main")
            modelAndView.addObject("code", code)
        }
        return modelAndView
    }

    /**
     * 前端到main界面后，获取完token（和微信用户信息）访问login，注册或更新用户登录信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    String login(@RequestParam(value = "token") String token){
        // 包装数据并返回
        def map = [:]
        def uid = userService.findIdByToken(token)
        if (uid == null){
            def user = new User()
            user.token = token
            user.regTime = TimeUtil.getNowTime()
            userService.save(user)
            uid = userService.findIdByToken(token)
            map.put("msg","参与或发起志愿活动前，请前往完善个人信息！")
            map.put("type",1)
        }else {
            map.put("msg", "欢迎你，志愿者！")
            map.put("type",0)
        }
        CacheUtil.putCache(token,uid,CacheUtil.MEMCACHED_ONE_DAY*3)
        return new JsonBuilder(map).toString()
    }
}
