package com.web.joke.controller

import com.web.joke.enity.Alert
import com.web.joke.enity.JokeUser
import com.web.joke.enity.SingleAlert
import com.web.joke.service.impl.AlertServiceImpl
import com.web.joke.service.impl.JokeUserServiceImpl
import com.web.joke.util.PhontoUtil
import com.web.joke.util.TimeUtil
import com.web.volunteer.util.CacheUtil
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
@EnableAutoConfiguration
@RequestMapping("/joke")
public class PageController {
    private final Logger logger = LoggerFactory.getLogger(PageController.class);
    @Autowired
    private JokeUserServiceImpl userService
    @Autowired
    private AlertServiceImpl alertService

    //注册
    @RequestMapping(value = "/reg")
    String reg(Map<String, Object> model,
               @RequestParam(value = "mail") String mail,
               @RequestParam(value = "random") int random){
        try {
            def pwd = CacheUtil.getCache("j"+mail)
            def r = CacheUtil.getCache("jrandom-"+mail)
            def user = userService.getUserByMail(mail)
            if (StringUtils.isNotBlank(pwd) && user==null && r == random) {
                user = new JokeUser()
                user.mail = mail
                user.pwd = pwd
                user.regTime = TimeUtil.getNowTime()
                def type = userService.save(user)
                if (type == 1) {
                    model.put("result", "注册成功！");
                    logger.error("joke|LOGIN|uid:" + user.id + ",name:" + mail + ",time:" + TimeUtil.getNowTime())
                } else {
                    model.put("result", "创建用户异常");
                }
            }else {
                model.put("result", "邮件已过期！");
            }
        }catch (Exception e){
            model.put("result","注册失败，请重试，若已尝试多次，请返回平台再次发送注册邮件！");
            logger.error("reg|"+e.message)
        }
        return "result"
    }

    //登录页
    @RequestMapping(value = "/login")
    public String login(Map<String, Object> model) {
        return "joke/login"
    }

    //主页
    @RequestMapping(value = "/index")
    public String index(Map<String, Object> model) {
        return "joke/index"
    }

    //最终弹窗
    @RequestMapping(value = "/alert")
    public String alert(Map<String, Object> model,
                        @RequestParam(value = "alertId") Long alertId) {
        //处理
        def list = new ArrayList<SingleAlert>()
        def a1 = new SingleAlert()
        a1.content = "窗前明月光"
        a1.prompt = 0
        list.add(a1)
        def a2 = new SingleAlert()
        a2.content = "疑是地上霜"
        a2.prompt = 0
        list.add(a2)
        def a3 = new SingleAlert()
        a3.content = "下一句："
        a3.prompt = 1
        a3.answer = "举头望明月"
        a3.wrong = "这都不会？"
        list.add(a3)
        def a4 = new SingleAlert()
        a4.content = "低头思故乡"
        a4.prompt = 0
        list.add(a4)
        model.put("alertList",list)
        return "joke/alert"
    }

    //新增弹窗
    @RequestMapping(value = "/add")
    public String add(Map<String, Object> model) {
        return "joke/add"
    }

    //修改弹窗
    @RequestMapping(value = "/edit")
    public String edit(Map<String, Object> model,
                      @RequestParam(value = "alertId") Long alertId) {
        model.put("alertId",alertId)
        //根据id取title，修改时间之类的
        return "joke/add"
    }

    //主页
    @RequestMapping(value = "/addAlert")
    public String addAlert(Map<String, Object> model,
                           @RequestParam(value = "alertId", required = false) Long alertId,
                           @RequestParam(value = "uid") Long uid,
                           @RequestParam(value = "title") String title,
                           @RequestParam(value = "upload") MultipartFile file) {
        if (file==null){
            model.put("result","图片文件为空！")
            return "joke/add"
        }else if(file.getSize()>3000000){//照片大小不超过3M
            model.put("result","图片过大，上传失败！")
            return "joke/add"
        }
        def alert = new Alert()
        if (alertId==null || alertId==0){//新建
            alert.addTime = TimeUtil.getNowTime()
            alert.uid = uid
            alert.title = title
            int type = alertService.save(alert)
            if (type==0){
                model.put("result","新建弹窗失败！")
                return "joke/add"
            }
            alertId = alert.id
        }else {
            alert = alertService.getOneById(alertId)
            if (alert==null){
                model.put("result","弹窗不存在！")
                return "joke/add"
            }
            alert.title = title
            alert.editTime = TimeUtil.getNowTime()
            int type = alertService.save(alert)
            if (type==0){
                model.put("result","修改弹窗失败！")
                return "joke/add"
            }
        }
        if (PhontoUtil.savePhonto(file,alertId)){
            model.put("alertId",alertId)
            model.put("result","操作成功")//和前端js判断相关，别随意改
            return "joke/add"
        }else {
            model.put("result","图片保存失败！")
            return "joke/add"
        }
    }

}
