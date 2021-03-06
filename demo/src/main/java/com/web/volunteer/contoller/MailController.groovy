package com.web.volunteer.contoller

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.web.volunteer.service.impl.ActivityServiceImpl
import com.web.volunteer.service.impl.UserServiceImpl
import com.web.volunteer.util.CacheUtil
import com.web.volunteer.util.MailUtil
import groovy.json.JsonBuilder
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by roc on 2016/5/13.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/mail")
class MailController {
    private final Logger logger = LoggerFactory.getLogger(MailController.class);
    @Autowired
    private ActivityServiceImpl activityService;
    @Autowired
    private UserServiceImpl userService

    //发送邮箱验证邮件
    @RequestMapping(value = "/testMail")
    String testMail(@RequestParam(value = "uid") Long uid,
                    @RequestParam(value = "mail") String mail){//记得前端验证邮箱格式
        def map = [:]
        if (uid == null || StringUtils.isBlank(mail)){
            map.put("msg","数据异常，无法发送！")
            map.put("code",0)
            return new JsonBuilder(map).toString()
        }
        def pwd = userService.getPwdByMail(mail)
        if (StringUtils.isNotBlank(pwd)){
            map.put("msg","邮箱已存在！")
            map.put("code",0)
            return new JsonBuilder(map).toString()
        }
        def msg = "志愿者，你好！这是一封志愿平台邮箱验证邮件，若非本人操作请忽略！<a href=\"http://www.ustbvolunteer.com/testMail?uid="+uid+"&mail="+mail+"\">点击验证</a>"
        def title = "志愿者平台邮箱验证"
        if (MailUtil.sendMail(MailUtil.ustbMail,MailUtil.ustbPwd,mail,title,msg)){
            map.put("msg","验证邮件已发送，请尽快登录邮箱进行验证！")
            map.put("code",1)
        }else {
            map.put("msg","验证邮件发送失败！")
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    //发送确认参与邮件
    @RequestMapping(value = "/enSure")
    String enSure(@RequestParam(value = "activityId") Long activityId){
        def map = [:]
        def activity = activityService.findOneById(activityId)
        if (activity==null){
            map.put("msg","数据异常，无法发送！")
            map.put("code",0)
            return new JsonBuilder(map).toString()
        }
        if (StringUtils.isBlank(activity.member)){
            map.put("msg","成员列表为空！")
            map.put("code",0)
            return new JsonBuilder(map).toString()
        }
        def cacheTimes = "enSureTimes"+activityId
        def enSureTimes = CacheUtil.getCache(cacheTimes)
        logger.error(cacheTimes+":"+enSureTimes)
        if (enSureTimes!=null&&((Number)enSureTimes).intValue()==2){
            map.put("msg","您已多次发送，请耐心等待！")
            map.put("code",0)
            return new JsonBuilder(map).toString()
        }else {
            def memberList = (List<JSONObject>)JSON.parseObject(activity.member).get("memberList")
            def code = 0
            for (JSONObject member:memberList){
                def uid = (Number)member.get("uid")
                def user = userService.findOneUser(uid.longValue())
                def msg = "志愿者，你好！您参加的"+activity.name+"将于"+activity.startTime+"开始,请进行活动参加确认。<a href=\"http://www.ustbvolunteer.com/ensureResult?uid="+uid+"&activityId="+activityId+"\">点击此处确认参加</a>"
                def title = "活动提醒|"+activity.name
                if (MailUtil.sendMail(MailUtil.ustbMail, MailUtil.ustbPwd, user.mail, title, msg))
                    code = code + 1
            }
            if (enSureTimes==null){
                CacheUtil.putCache(cacheTimes,0,CacheUtil.MEMCACHED_ONE_DAY);
            }else {
                def times = ((Number)enSureTimes).intValue()
                times++
                CacheUtil.putCache(cacheTimes,times,CacheUtil.MEMCACHED_ONE_DAY);
            }
            if (code==0){
                map.put("msg","提醒邮件群发异常！")
            }else {
                code = 1
            }
            map.put("code",code)
        }
        return new JsonBuilder(map).toString()
    }

    //发送密码提醒邮件
    @RequestMapping(value = "/forget")
    String forget(@RequestParam(value = "mail") String mail){
        def map = [:]
        def code = 0
        def msg = ""
        if (StringUtils.isBlank(mail)){
            map.put("code",0)
            map.put("result","邮箱为空")
            return new JsonBuilder(map).toString()
        }
        if (CacheUtil.getCache("forget"+mail)==null)
            CacheUtil.putCache("forget"+mail,0,CacheUtil.MEMCACHED_ONE_DAY)
        int times = CacheUtil.getCache("forget"+mail)
        def pwd = userService.getPwdByMail(mail)
        if (StringUtils.isBlank(pwd)) {
            msg = "该邮箱尚未注册，直接设定密码登录即可"
        } else if (times<5){
            def content = "您的密码为：" + pwd
            def title = "弓一活动助手密码找回"
            if (MailUtil.sendMail(MailUtil.ustbMail, MailUtil.ustbPwd, mail, title, content)) {
                msg = "邮件已发送，请尽快登录邮箱查看密码！"
                code = 1
            } else {
                msg = "验证邮件发送失败！"
            }
            times++
            CacheUtil.putCache("forget"+mail,times,CacheUtil.MEMCACHED_ONE_DAY)
        }else {
            msg = "已达今日密码找回邮件发送次数上限"
        }
        map.put("code",code)
        map.put("result",msg)
        return new JsonBuilder(map).toString()
    }
}
