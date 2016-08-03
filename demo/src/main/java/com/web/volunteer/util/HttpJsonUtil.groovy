package com.web.volunteer.util

import com.web.volunteer.domain.User
import groovyx.net.http.HTTPBuilder
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import net.sf.json.JSONObject

import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

/**
 * 用于在服务器端发送http请求
 * Created by Administrator on 2016/5/2.
 */
class HttpJsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpJsonUtil.class);
    private static final appid = 'wxf230b60790974618'//公众号的唯一标识
    private static final secret = 'b921022f0639f260177f130eddd0096b'//公众号的appsecret
    private static final grant_type = 'client_credential'

    /**
     * 根据code换取token
     * @param code
     * @return
     */
    public static User getUserByCode(String code){
       //因appid和secret安全级别较高，微信要求在服务器端访问用code换取token的接口
        def http = new HTTPBuilder( 'https://api.weixin.qq.com' )
        def token = ""
        def openid = ""
        http.request( GET, JSON ) {
                uri.path = '/sns/oauth2/access_token'
                uri.query = [ appid:'APPID',secret:'SECRET',code:'CODE',grant_type:'authorization_code' ]
                response.success = { resp, json ->
                    token = json.access_token
                    openid = json.openid
                }
                response.'404' = { resp ->
                logger.error("Not found wecaturl at /user/index")
            }
            http.handler.failure = { resp ->
                logger.error("Unexpected failure: ${resp.statusLine}")
            }
        }
        if (StringUtils.isBlank(token)||StringUtils.isBlank(openid)){
            return null
        }else {
            return getUserInfo(token,openid)
        }
    }

    public static User getUserInfo(String token,String openid){
        def http = new HTTPBuilder( 'https://api.weixin.qq.com' )
        def user = new User()
        http.request( GET, JSON ) {
            uri.path = '/sns/userinfo'
            uri.query = [ access_token:token,openid:openid,lang:'zh_CN' ]
            response.success = { resp, json ->
                user.name = json.nickname
                user.sex = json.sex
                user.openid = json.openid
            }
            response.'404' = { resp ->
                logger.error("Not found wecaturl at /user/index")
            }
            http.handler.failure = { resp ->
                logger.error("Unexpected failure: ${resp.statusLine}")
            }
        }
        if (StringUtils.isBlank(user.openid))
            return null
        else
            return user
    }

    public static String getAccessToken(){
        def http = new HTTPBuilder( 'https://api.weixin.qq.com' )
        def token = ""
        http.request( GET, JSON ) {
            uri.path = '/cgi-bin/token'
            uri.query = [ appid:HttpJsonUtil.appid,secret:HttpJsonUtil.secret,grant_type:'client_credential' ]
            response.success = { resp, json ->
                token = json.access_token
                logger.error("getAccessToken:"+token)
            }
            response.'404' = { resp ->
                logger.error("Not found wecaturl at /user/index")
            }
            http.handler.failure = { resp ->
                logger.error("Unexpected failure: ${resp.statusLine}")
            }
        }
        return token
    }

    public static String creatMenu(JSONObject menu,String accessToken){
        URL url = new URL("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "plain/text; charset=UTF-8");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);

        connection.connect();

        OutputStreamWriter post = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        post.append(menu.toString());
        post.flush();
        post.close();

        // //读取响应 {"errcode":0,"errmsg":"ok"}
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        StringBuffer responseMsg = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            responseMsg.append(lines);
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        return responseMsg
    }

    public static Map<String, String> getXmlMap(HttpServletRequest request){//报错，参数类型的，没细查
        Map<String, String> msg = new HashMap<String, String>()
        try {
            ServletInputStream inputStream = request.getInputStream()
            String xml = IOUtils.toString(inputStream)
            xml = new String(xml.getBytes("GBK"), "GBK")
            logger.error(xml)
            xml = "<xml><ToUserName><![CDATA[gh_b74a0f7f68a9]]></ToUserName>" +
                    "<FromUserName><![CDATA[oOrD2wdsfgKFOrlRM7IjsbTb8fLQ]]></FromUserName>" +
                    "<CreateTime>1468920105</CreateTime>" +
                    "<MsgType><![CDATA[text]]></MsgType>" +
                    "<Content><![CDATA[1]]></Content>" +
                    "<MsgId>6308963811822122875</MsgId>" +
                    "</xml>"
            Document document = DocumentHelper.parseText(xml)
            Element list = document.getRootElement()
            List<Element> elements = list.elements()
            for (Element element : elements) {
                msg.put(element.getName().trim(), element.getStringValue())
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        return msg
    }

    public static String reply(Map<String, String> msg,String content){
        StringBuffer str = new StringBuffer()
        str.append("<xml>")
        str.append("<ToUserName><![CDATA[" + msg.get("FromUserName") + "]]></ToUserName>")
        str.append("<FromUserName><![CDATA[" + msg.get("ToUserName") + "]]></FromUserName>")
        str.append("<CreateTime>" + msg.get("CreateTime") + "</CreateTime>")
        str.append("<MsgType><![CDATA[text]]></MsgType>")
        str.append("<Content><![CDATA[" + content + "]]></Content>")
        str.append("</xml>")
        logger.error(str.toString())
        return str.toString()
    }
//    private static final token = 'zoro'//测试用
//    public static String testHttp(){
//        def msg
//        def http = new HTTPBuilder( 'http://localhost' )
//        http.request( GET, JSON ) {
//            uri.path = '/user/login'
//            uri.query = [ token:token ]
//
//            response.success = { resp, json ->
//                msg = json.msg
//                println msg
//                json.each{
//                    println it
//                }
//            }
//            response.'404' = { resp ->
//                logger.error("Not found wecaturl at /user/index")
//            }
//            http.handler.failure = { resp ->
//                logger.error("Unexpected failure: ${resp.statusLine}")
//            }
//        }
//        return msg
//    }
}
