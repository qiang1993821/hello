<!DOCTYPE html >
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta name="wap-font-scale" content="no">
<link rel="stylesheet" href="static/css/weui.min.css"/>
<link rel="stylesheet" href="static/css/reset.css">
<link rel="stylesheet" href="static/css/index.css">
<link rel="stylesheet" href="static/css/example.css">
<title>首页</title>
</head>
<body class="common" ontouchstart>
    <div class="container" id="container"></div>

    <script type="text/html" id="tpl_home">
        <div class="bd">
            <!--<a href="javascript:;" class="weui_btn weui_btn_primary">点击展现searchBar</a>-->
            <div class="weui_search_bar" id="search_bar">
                <form class="weui_search_outer">
                    <div class="weui_search_inner">
                        <i class="weui_icon_search"></i>
                        <input type="search" class="weui_search_input" id="search_input" placeholder="搜索" required/>
                        <a href="javascript:" class="weui_icon_clear" id="search_clear"></a>
                    </div>
                    <label for="search_input" class="weui_search_text" id="search_text">
                        <i class="weui_icon_search"></i>
                        <span>搜索</span>
                    </label>
                </form>
                <a href="javascript:" class="weui_search_cancel" id="search_cancel">取消</a>
            </div>
            <div class="weui_cells weui_cells_access search_show" id="search_show">
                <div class="weui_cell">
                    <div class="weui_cell_bd weui_cell_primary">
                        <p>实时搜索文本</p>
                    </div>
                </div>
            </div>
        </div>
       
        <div class="weui_tab">
            <div class="weui_tab_bd">

            </div>
            <div class="weui_tabbar">
                <a href="javascript:;" class="weui_tabbar_item" id="activity">
                    <div class="weui_tabbar_icon">
                        <img src="static/images/icon_nav_article.png" alt="">
                    </div>
                    <p class="weui_tabbar_label">活动</p>
                </a>
                <a href="javascript:;" class="weui_tabbar_item" id="me">
                    <div class="weui_tabbar_icon" id="current">
                        <img src="static/images/icon_nav_cell.png" alt="">
                    </div>
                    <p class="weui_tabbar_label">我</p>
                </a>
            </div>
        </div>
        
        <div class="lists"> 
            <div class="weui_panel weui_panel_access">
                <div class="weui_panel_bd"  id="result_show">
                    <a class="weui_cell" href="/testMail">
                        <div class="weui_media_box weui_media_text">
                            <h4 class="weui_media_title">测试活动</h4>
                            <p class="weui_media_desc">6工时|正在进行</p>
                        </div>
                    </a>
                    <a class="weui_cell" href="/testMail">
                        <div class="weui_media_box weui_media_text">
                            <h4 class="weui_media_title">测试活动</h4>
                            <p class="weui_media_desc">6工时|正在进行</p>
                        </div>
                    </a>
                    <a class="weui_cell" href="/testMail">
                        <div class="weui_media_box weui_media_text">
                            <h4 class="weui_media_title">测试活动</h4>
                            <p class="weui_media_desc">6工时|正在进行</p>
                        </div>
                    </a>
                    <a class="weui_cell" href="/testMail">
                        <div class="weui_media_box weui_media_text">
                            <h4 class="weui_media_title">测试活动</h4>
                            <p class="weui_media_desc">6工时|正在进行</p>
                        </div>
                    </a>
                    <a class="weui_cell" href="/testMail">
                        <div class="weui_media_box weui_media_text">
                            <h4 class="weui_media_title">测试活动</h4>
                            <p class="weui_media_desc">6工时|正在进行</p>
                        </div>
                    </a>
                </div>
                <div onclick="queryByPage()" class="weui_btn weui_btn_default" id="loadMore">加载更多</div>
            </div>
            <div> 
                <div class="weui_cells weui_cells_access">
                    <div class="hd">
                        <h1 class="page_title">${name}</h1>
                    </div>
                    <a class="weui_cell" href="/launch" id="launch">
                        <div class="weui_cell_bd weui_cell_primary">
                            <p>发起活动</p>
                        </div>
                        <div class="weui_cell_ft">
                        </div>
                    </a>
                    <a class="weui_cell" href="javascript:;">
                        <div class="weui_cell_bd weui_cell_primary">
                            <p>修改个人信息</p>
                        </div>
                        <div class="weui_cell_ft">
                        </div>
                    </a>
                    <a class="weui_cell" href="javascript:;">
                        <div class="weui_cell_bd weui_cell_primary">
                            <p>我的好友</p>
                        </div>
                        <div class="weui_cell_ft">
                        </div>
                    </a>
                    <a class="weui_cell" href="javascript:;">
                        <div class="weui_cell_bd weui_cell_primary">
                            <p>收到的邀请</p>
                        </div>
                        <div class="weui_cell_ft">
                        </div>
                    </a>
                    <a class="weui_cell" href="javascript:;">
                        <div class="weui_cell_bd weui_cell_primary">
                            <p>我发起的</p>
                        </div>
                        <div class="weui_cell_ft">
                        </div>
                    </a>
                    <a class="weui_cell" href="javascript:;">
                        <div class="weui_cell_bd weui_cell_primary">
                            <p>我参与的</p>
                        </div>
                        <div class="weui_cell_ft">
                        </div>
                    </a>
                    <div class="bd spacing">
                        <a href="javascript:logout()" class="weui_btn weui_btn_warn">
                            退出登录
                        </a>
                    </div>
                </div>
            </div>
        </div>
        
    </script>
   
    <!-- 搜索结束 -->
    <script src="static/js/zepto.min.js"></script>
    <script src="static/js/router.min.js"></script>
    <script src="static/js/index.js"></script>
    <script type="text/javascript">
        if(localStorage.needRefresh && localStorage.needRefresh==1) {
            localStorage.needRefresh = 0;
            location.reload();
        }
    </script>
</body>
</html>





