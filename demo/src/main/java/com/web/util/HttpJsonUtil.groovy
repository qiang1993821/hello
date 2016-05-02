package com.web.util

import com.web.domain.User
import groovyx.net.http.HTTPBuilder
import org.apache.commons.lang.StringUtils

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

/**
 * 用于在服务器端发送http请求
 * Created by Administrator on 2016/5/2.
 */
class HttpJsonUtil {
    private static final appid = 'APPID'//公众号的唯一标识
    private static final secret = 'secret'//公众号的appsecret
    private static final grant_type = 'authorization_code'

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
                user.sex
            }
            response.'404' = { resp ->
                logger.error("Not found wecaturl at /user/index")
            }
            http.handler.failure = { resp ->
                logger.error("Unexpected failure: ${resp.statusLine}")
            }
        }
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
