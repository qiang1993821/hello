package com.web.joke.controller

import com.web.joke.enity.SingleAlert
import com.web.joke.service.impl.AlertServiceImpl
import com.web.joke.util.PhontoUtil
import groovy.json.JsonBuilder
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletResponse

/**
 * Created by roc on 2016/8/29.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/joke/alert")
class AlertContoller {
    private final Logger logger = LoggerFactory.getLogger(AlertContoller.class);
    @Autowired
    private AlertServiceImpl alertService

    //新增或修改一个弹窗
    @RequestMapping(value = "/savePage")
    String savePage(SingleAlert singleAlert,
                  @RequestParam(value = "page") Integer page,
                  @RequestParam(value = "alertId") Long alertId){
        def map = [:]
        try{
            def alert = alertService.getOneById(alertId)
            if (alert==null){
                map.put("code",0)
                map.put("result","获取不到弹窗ID")
                return new JsonBuilder(map).toString()
            }
            if (alertService.savePage(singleAlert)==1){
                //修改页面顺序
                if (alertService.saveSequence(alert,page,singleAlert.id)) {
                    map.put("code", 1)
                    logger.error("joke|savePage|id:" + singleAlert.id)
                }else {
                    map.put("code",2)
                    map.put("result","弹窗顺序修改失败")
                }
            }else {
                map.put("code",3)
                map.put("result","保存数据失败")
            }

        }catch (Exception e){
            map.put("code", -1)
            logger.error("joke|savePage|Exception:" + e.message)
        }
        return new JsonBuilder(map).toString()
    }

    //删除整个弹窗
    @RequestMapping(value = "/delAlert")
    String delAlert(@RequestParam(value = "alertId") Long alertId){
        def map = [:]
        try{
            if (alertId==null || alertId==0){
                map.put("code",0)
                map.put("result","获取不到弹窗ID")
                return new JsonBuilder(map).toString()
            }
            //删除弹窗
            int code = alertService.delAlert(alertId)
            if (code != 1)
                map.put("result","删除失败")
            map.put("code", code)
        }catch (Exception e){
            map.put("code", -1)
            map.put("result","删除异常")
            logger.error("joke|delPage|Exception:" + e.message)
        }
        return new JsonBuilder(map).toString()
    }

    //删除一个弹窗
    @RequestMapping(value = "/delPage")
    String delPage(@RequestParam(value = "pageId") Long pageId,
                   @RequestParam(value = "alertId") Long alertId){
        def map = [:]
        try{
            if (pageId==null || alertId==null || pageId==0 || alertId==0){
                map.put("code",0)
                map.put("result","获取不到弹窗ID")
                return new JsonBuilder(map).toString()
            }
            //删除弹窗
            int code = alertService.delPage(pageId,alertId)
            if (code != 1)
                map.put("result","删除失败")
            map.put("code", code)
        }catch (Exception e){
            map.put("code", -1)
            map.put("result","删除异常")
            logger.error("joke|delPage|Exception:" + e.message)
        }
        return new JsonBuilder(map).toString()
    }

    //获取图片
    @RequestMapping(value = "/getImg")
    String getImg(@RequestParam(value = "alertId") Long alertId,
                  HttpServletResponse response){
        FileInputStream inputStream = new FileInputStream(PhontoUtil.IMG_URL+alertId+".jpg");
        int length = inputStream.available();
        byte[] data = new byte[length];
        response.setContentLength(length);
        response.setContentType("jpg");
        inputStream.read(data);
        OutputStream toClient = response.getOutputStream();
        toClient.write(data);
        toClient.flush();
        IOUtils.closeQuietly(toClient);
        IOUtils.closeQuietly(inputStream);
    }
}
