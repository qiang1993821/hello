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
        def alert = alertService.getOneById(alertId)
        def alertList = alertService.getAllPage(alert.alertList)
        model.put("alertList",alertList)
        model.put("imgUrl","http://www.ustbvolunteer.com/joke/alert/getImg?alertId=" + alertId)
        model.put("title",alert.title)
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
        def alert = alertService.getOneById(alertId)
        if (alert != null) {
            model.put("img", "alert/getImg?alertId=" + alertId)
            model.put("title", alert.title)
            model.put("addTime", alert.addTime)
            model.put("editTime", StringUtils.isBlank(alert.editTime)?alert.addTime:alert.editTime)
        }
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
        def isEdit = false
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
            isEdit = true
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
            }else {
                model.put("result","修改弹窗成功！")
            }
        }
        if (isEdit && file.size==0){
            return "joke/add"
        }else {
            if (PhontoUtil.savePhonto(file, alertId)) {
                model.put("alertId", alertId)
                model.put("result", "操作成功")//和前端js判断相关，别随意改
                return "joke/add"
            } else {
                model.put("result", "图片保存失败！")
                return "joke/add"
            }
        }
    }

    //弹窗内页的列表
    @RequestMapping(value = "/pageList")
    public String pageList(Map<String, Object> model,
                       @RequestParam(value = "alertId") Long alertId) {
        model.put("alertId",alertId)
        model.put("infoList",alertService.getPageList(alertId))
        return "joke/pageList"
    }

    //新增弹窗内页
    @RequestMapping(value = "/addPage")
    public String addPage(Map<String, Object> model,
                           @RequestParam(value = "alertId") Long alertId) {
        model.put("alertId",alertId)
        model.put("pageNum",alertService.getPageNum(alertId))
        return "joke/addPage"
    }

    //修改弹窗内页
    @RequestMapping(value = "/editPage")
    public String editPage(Map<String, Object> model,
                          @RequestParam(value = "alertId") Long alertId,
                          @RequestParam(value = "pageId") Long pageId,
                          @RequestParam(value = "currentPage") Integer currentPage) {
        model.put("alertId",alertId)
        model.put("pageId",pageId)
        model.put("pageNum",alertService.getPageNum(alertId))
        model.put("currentPage",currentPage)
        def page = alertService.getOnePage(pageId)
        model.put("prompt",page.prompt)
        model.put("content",page.content)
        model.put("answer",page.answer)
        model.put("wrong",page.wrong)
        return "joke/addPage"
    }

    //我的弹窗
    @RequestMapping(value = "/myAlert")
    public String myAlert(Map<String, Object> model,
                           @RequestParam(value = "uid") Long uid) {
        def alertList = alertService.getMyAlertList(uid)
        model.put("alertList",alertList)
        return "joke/myAlert"
    }
}
