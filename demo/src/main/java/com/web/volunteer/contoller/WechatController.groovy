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
        def content = "\ue231<a href=\"http://www.ustbvolunteer.com/login\">点击进入活动管理平台</a>\n" +
                "\ue231<a href=\"http://www.ustbvolunteer.com/joke/login\">点击进入弹窗恶作剧</a>\n" +
                "自带超简易的回复系统，回复内容是上一位的发言,支持图片语音，如果聊上了，纯属巧合。~\n" +
                "主页君是一个技术粗糙喜欢胡思乱想的无聊人士，会不定时的推出些无聊的小功能，欢迎邮件roc_strong@163.com，\n" +
                "吐槽，提意见，出点子，技术改进均可~"
        if (StringUtils.isNotBlank(event)){
            logger.error("WECHAT_EVENT|event:"+event)
            if ("subscribe".equalsIgnoreCase(event)){
                if (StringUtils.isNotBlank(msg.get("FromUserName")))
                    CacheUtil.putCache(msg.get("FromUserName"),"1",CacheUtil.MEMCACHED_ONE_DAY)
                return HttpJsonUtil.reply(msg,"t十分感谢关注弓一\n"+content)
            }
        }
        content = "t"+content
        //每天发一次推送
        if (StringUtils.isNotBlank(msg.get("FromUserName"))&&CacheUtil.getCache(msg.get("FromUserName"))==null){//每天推一次欢迎内容
            CacheUtil.putCache(msg.get("FromUserName"),"1",CacheUtil.MEMCACHED_ONE_DAY)
            logger.error("WECHAT_INDEX|FromUserName:"+msg.get("FromUserName"))
            return HttpJsonUtil.reply(msg,content)
        }
        //获取缓存回复，将来信加入缓存，t开头代表文本，v声音，i图片
        Random random = new Random()
        int r = random.nextInt(10)//十个缓存，避免一直和自己对话
        if (CacheUtil.getCache("lastMsg"+r)!=null)
            content = CacheUtil.getCache("lastMsg"+r)
        if ("text".equalsIgnoreCase(msg.get("MsgType"))){
            CacheUtil.putCache("lastMsg"+r,"t"+msg.get("Content"),CacheUtil.MEMCACHED_ONE_MONTH)
        }else if ("voice".equalsIgnoreCase(msg.get("MsgType"))){
            CacheUtil.putCache("lastMsg"+r,"v"+msg.get("MediaId"),CacheUtil.MEMCACHED_ONE_MONTH)
        }else if ("image".equalsIgnoreCase(msg.get("MsgType"))){
            CacheUtil.putCache("lastMsg"+r,"i"+msg.get("MediaId"),CacheUtil.MEMCACHED_ONE_MONTH)
        }
        logger.error("reply:"+content+"|||||||||this:"+CacheUtil.getCache("lastMsg"+r))
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
