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
import groovy.json.JsonBuilder
import org.springframework.web.bind.annotation.RestController

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Administrator on 2016/4/26.
 */
@RestController
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
//    @RequestMapping(value = "/index", method = RequestMethod.GET)
//    ModelAndView index(@RequestParam(value = "code",required = false) String code,
//                       @RequestParam(value = "state") String state){
//        def modelAndView
//        if (StringUtils.isBlank(code)){
//            modelAndView = new ModelAndView("/error")
//            modelAndView.addObject("erroInfo", "微信登录异常，请重新登录！")
//        }else {
//            def user = HttpJsonUtil.getUserByCode(code)
//            if (user == null){
//                modelAndView = new ModelAndView("/error")
//                modelAndView.addObject("erroInfo", "微信获取信息异常，请重新登录！")
//            }else {
//                modelAndView = new ModelAndView("/main")
//                modelAndView.addObject("code", code)
//                def uid = userService.findIdByOpenid(user.openid)
//                if (uid == null){
//                    userService.save(user)
//                    uid = userService.findIdByOpenid(user.openid)
//                    modelAndView.addObject("uid", uid)
//                }
//                CacheUtil.putCache(user.openid,uid,CacheUtil.MEMCACHED_ONE_DAY*3)
//            }
//        }
//        return modelAndView
//    }

    /**
     * 判断是否填写了个人信息，以手机号和邮箱为准，在用户参加或发起活动前调用
     * @return
     */
    @RequestMapping(value = "/isFullInfo")
    String isFullInfo(@RequestParam(value = "uid") Long uid){//这接口去了吧，把逻辑直接加到发起或参加活动那里
        if (uid == null)
            return null
        // 包装数据并返回
        def map = [:]
        def phone = userService.getPhoneById(uid)
        if (StringUtils.isBlank(phone)){
            map.put("msg","参与或发起志愿活动前，请前往完善个人信息！")
            map.put("type",0)
        }else {
            map.put("msg", "")
            map.put("type",1)
            CacheUtil.putCache("phone-"+uid,phone,CacheUtil.MEMCACHED_ONE_DAY*3)
        }
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/login")
    String login(@RequestParam(value = "username") String username){
        def map = [:]
        if (StringUtils.isBlank(username)){
            map.put("result","微信登录异常，请重新登录！")
            map.put("type",0)
        }else {
            def uid = userService.findIdByName(username)
            def type
            def result
            if (uid == null){
                def user = new User()
                user.name = username
                user.regTime = TimeUtil.getNowTime()
                type = userService.save(user)
                if (type == 1) {
                    uid = userService.findIdByName(username)
                    result = uid//存cookie用
                    CacheUtil.putCache(username,uid,CacheUtil.MEMCACHED_ONE_DAY*3)
                }else {
                    result = "创建用户异常"
                }
            }
            map.put("type",type)
            map.put("result",result)
        }
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/updateUserInfo")
    String updateUserInfo(User oldUser,HttpServletRequest request){
        def map = [:]
        try {
            def user = userService.findOneUser(oldUser.id)
            user.name = StringUtils.isBlank(oldUser.name)?user.name:oldUser.name
            user.academy = StringUtils.isBlank(oldUser.academy)?user.academy:oldUser.academy
            user.className = StringUtils.isBlank(oldUser.className)?user.className:oldUser.className
            user.phone = StringUtils.isBlank(oldUser.phone)?user.phone:oldUser.phone
            user.wechat = StringUtils.isBlank(oldUser.wechat)?user.wechat:oldUser.wechat
            user.show = StringUtils.isBlank(request.getParameter("show"))?user.show:oldUser.show
            user.sex = StringUtils.isBlank(request.getParameter("sex"))?user.sex:oldUser.sex
            userService.save(user)
            map.put("msg","更新个人信息成功！")
            map.put("type",1)
        }catch (Exception e){
            logger.error(e.message)
            map.put("msg","更新个人信息失败！")
            map.put("type",0)
        }
        return new JsonBuilder(map).toString()
    }
}
