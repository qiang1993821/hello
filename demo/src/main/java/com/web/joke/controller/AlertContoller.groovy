package com.web.joke.controller

import com.web.joke.enity.SingleAlert
import com.web.joke.service.impl.AlertServiceImpl
import groovy.json.JsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
}
