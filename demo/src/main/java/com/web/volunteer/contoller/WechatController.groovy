package com.web.volunteer.contoller

import com.web.volunteer.util.CacheUtil
import com.web.volunteer.util.HttpJsonUtil
import net.sf.json.JSONObject
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest

/**
 * Created by Administrator on 2016/7/5.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/wechat")
class WechatController {
    private final Logger logger = LoggerFactory.getLogger(WechatController.class)
    @RequestMapping(value = "/check")
    String doWechat(HttpServletRequest request){
        logger.error("111111111111111111")
        Map<String, String> msg = new HashMap<String, String>()
        try {
            ServletInputStream inputStream = request.getInputStream()
            String xml = IOUtils.toString(inputStream)
            xml = new String(xml.getBytes("GBK"), "GBK")
            logger.error(xml)
            Document document = DocumentHelper.parseText(xml)
            Element list = document.getRootElement()
            List<Element> elements = list.elements()
            for (Element element : elements) {
                msg.put(element.getName().trim(), element.getStringValue())
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        def event = msg.get("Event");
        def content = "活动管理平台：http://www.ustbvolunteer.com/login\n" +
                "弹窗恶作剧：http://www.ustbvolunteer.com/joke/login\n" +
                "本平台自带超简易的回复系统，回复内容是上一位的发言，如果聊上了，纯属巧合。~\n" +
                "如果你有有意思的点子，无聊的点子，腹黑的点子，可以联系roc_strong@163.com~"
        if (StringUtils.isNotBlank(event)){
            logger.error("WECHAT_EVENT|event:"+event)
            if ("subscribe".equalsIgnoreCase(event)){
                if (StringUtils.isNotBlank(msg.get("FromUserName")))
                    CacheUtil.putCache(msg.get("FromUserName"),"1",CacheUtil.MEMCACHED_ONE_DAY)
                return HttpJsonUtil.reply(msg,"十分感谢关注弓一\n"+content)
            }
        }
        //每天发一次推送
        if (StringUtils.isNotBlank(msg.get("FromUserName"))&&CacheUtil.getCache(msg.get("FromUserName"))==null){//每天推一次欢迎内容
            CacheUtil.putCache(msg.get("FromUserName"),"1",CacheUtil.MEMCACHED_ONE_DAY)
            logger.error("WECHAT_INDEX|FromUserName:"+msg.get("FromUserName"))
            return HttpJsonUtil.reply(msg,content)
        }
        Random random = new Random()
        int r = random.nextInt(10)//十个缓存，避免一直和自己对话
        if ("text".equalsIgnoreCase(msg.get("MsgType"))){
            if (CacheUtil.getCache("lastMsg"+r)!=null)
                content = CacheUtil.getCache("lastMsg"+r)
            CacheUtil.putCache("lastMsg"+r,msg.get("Content"),CacheUtil.MEMCACHED_ONE_MONTH)
        }
        return HttpJsonUtil.reply(msg,content)
    }

    /**
     * 不认证玩不了，获取AccessToken的接口是好的
     * @return
     */
    @RequestMapping(value = "/creatMenu")
    String creatMenu(){
        def menu = new JSONObject()
        def btnList = new ArrayList<JSONObject>()
        def btn1 = new JSONObject()
        def btn2 = new JSONObject()
        def btn3 = new JSONObject()
        btn1.put("type","view")
        btn1.put("name","志愿平台")
        btn1.put("url","http://www.ustbvolunteer.com/login")
        btnList.add(btn1)
        btn2.put("type","view")
        btn2.put("name","恶作剧")
        btn2.put("url","http://www.ustbvolunteer.com/login")
        btnList.add(btn2)
        btn3.put("type","click")
        btn3.put("name","联系作者")
        btn3.put("key","iwantu")
        btnList.add(btn3)
        menu.put("button",btnList)
        logger.error(HttpJsonUtil.creatMenu(menu,HttpJsonUtil.getAccessToken()))
        return ""
    }
}
