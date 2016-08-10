package com.web.volunteer.contoller

import com.web.volunteer.domain.User
import com.web.volunteer.util.CacheUtil
import com.web.volunteer.service.impl.UserServiceImpl
import com.web.volunteer.util.MailUtil
import com.web.volunteer.util.TimeUtil
import com.web.volunteer.util.UserUtil
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import groovy.json.JsonBuilder
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

import javax.servlet.http.HttpServletRequest

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

    /**
     * 登录接口，临时的，只认用户名，没有的就创建
     * @param username
     * @return
     */
    @RequestMapping(value = "/login")
    String login(@RequestParam(value = "username") String username,
                 @RequestParam(value = "pwd") String pwd){
        def map = [:]
        if (StringUtils.isBlank(username)){
            map.put("result","微信登录异常，请重新登录！")
            map.put("code",0)
        }else {
            def user = userService.getUserByMail(username)
            def type = 1
            def result
            def random = UserUtil.getRondomNum()//随机数，增大安全性，否则不登陆邮箱知道注册激活链接也能注册
            if (user == null){
                type = 0
                CacheUtil.putCache(username,pwd,CacheUtil.MEMCACHED_ONE_DAY*3)
                CacheUtil.putCache("random-"+username,random,CacheUtil.MEMCACHED_ONE_DAY*3)
                def msg = "欢迎注册弓一活动平台，<a href=\"http://www.ustbvolunteer.com/reg?mail="+username+"&random="+random+"\">点击完成注册</a>，若非本人操作请忽略！（此邮件三日内有效）"
                def title = "弓一活动平台注册激活"
                if (MailUtil.sendMail(MailUtil.ustbMail,MailUtil.ustbPwd,username,title,msg)){
                    result = "此邮箱不存在，已发送注册邮件，请尽快登录完成注册！"
                }else {
                    result = "此邮箱不存在，注册邮件发送失败！"
                }
            }else {
                if (user.pwd == pwd){
                    result = user.id
                }else {
                    type = 0
                    result = "密码错误！"
                }
            }
            map.put("code",type)
            map.put("result",result)
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 修改个人信息
     * @param oldUser
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    String updateUserInfo(User newUser,HttpServletRequest request){
        def map = [:]
        try {
            def user = userService.findOneUser(newUser.id)
            def oldUser = user
            user.name = StringUtils.isBlank(newUser.name)?user.name:newUser.name
            user.academy = StringUtils.isBlank(newUser.academy)?user.academy:newUser.academy
            user.className = StringUtils.isBlank(newUser.className)?user.className:newUser.className
            user.phone = StringUtils.isBlank(newUser.phone)?user.phone:newUser.phone
            user.wechat = StringUtils.isBlank(newUser.wechat)?user.wechat:newUser.wechat
            user.show = StringUtils.isBlank(request.getParameter("show"))?user.show:newUser.show
            user.sex = StringUtils.isBlank(request.getParameter("sex"))?user.sex:newUser.sex
            if (oldUser.name.equals(user.name)&&oldUser.academy.equals(user.academy)&&oldUser.className.equals(user.className)
            &&oldUser.phone.equals(user.phone)&&oldUser.wechat.equals(user.wechat)&&oldUser.mail.equals(newUser.mail)
            &&oldUser.show==user.show&&oldUser.sex==user.sex){
                map.put("msg", "信息没有改动")
                map.put("code",0)
                return new JsonBuilder(map).toString()
            }
            userService.save(user)
            map.put("msg", "")
            map.put("code",1)
        }catch (Exception e){
            logger.error(e.message)
            map.put("msg","更新个人信息失败！")
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    /**
     * 查询活跃事件，同时检测所有活动相关状态，若到时改变状态
     * @param uid
     * @return
     */
    @RequestMapping(value = "/current")
    String current(@RequestParam(value = "uid") Long uid){
        def map = [:]
        if (uid == null){
            map.put("code",0)
        }else {
            map.put("code",userService.current(uid))
        }
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/addFriend")
    String addFriend(@RequestParam(value = "uid") Long uid,@RequestParam(value = "friendId") Long friendId){
        def map = [:]
        if (userService.isFriend(uid,friendId)){
            map.put("msg","ta已经是您的好友，不必再次添加！")
            map.put("code",0)
            return new JsonBuilder(map).toString()
        }
        if (userService.doFriend(uid,friendId,true)){
            map.put("msg","添加成功！")
            map.put("code",1)
        }else {
            map.put("msg","添加发生异常！")
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/delFriend")
    String delFriend(@RequestParam(value = "uid") Long uid,@RequestParam(value = "friendId") Long friendId){
        def map = [:]
        if (userService.doFriend(uid,friendId,false)){
            map.put("msg","删除成功！")
            map.put("code",1)
        }else {
            map.put("msg","删除发生异常！")
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }
}
