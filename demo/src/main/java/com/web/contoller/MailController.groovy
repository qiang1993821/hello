package com.web.contoller

import com.web.util.MailUtil
import groovy.json.JsonBuilder
import org.apache.commons.lang.StringUtils
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
    @RequestMapping(value = "/testMail")
    String testMail(@RequestParam(value = "uid") Long uid,
                    @RequestParam(value = "mail") String mail){//记得前端验证邮箱格式
        def map = [:]
        if (uid == null || StringUtils.isBlank(mail)){
            map.put("msg","数据异常，无法发送！")
            map.put("type",0)
        }
        def msg = "志愿者，你好！这是一封志愿平台邮箱验证邮件，若非本人操作请忽略！<a href=\"http://localhost/home/testMail?uid="+uid+"&mail="+mail+"\">点击验证</a>"
        def title = "志愿者平台邮箱验证"
        if (MailUtil.sendMail(MailUtil.ustbMail,MailUtil.ustbPwd,mail,title,msg)){
            map.put("msg","验证邮件发送成功！")
            map.put("type",1)
        }else {
            map.put("msg","验证邮件发送失败！")
            map.put("type",0)
        }
        return new JsonBuilder(map).toString()
    }
}
