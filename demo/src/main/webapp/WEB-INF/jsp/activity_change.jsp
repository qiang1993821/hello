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
<title>弓一志愿平台</title>
</head>
<body class="activityChange">
<input type="hidden" id="activityId" value="${activityId}"/>
	<div class="container" id="container"></div>
	<script type="text/html" id="tpl_home">
		<div class="weui_cells_title">活动名称</div>
		<div class="weui_cell">
            <div class="weui_cell_hd"><label class="weui_label"></label></div>
            <div class="weui_cell_bd weui_cell_primary">
                <input class="weui_input" type="text" placeholder="请输入活动名称" id="activityName">
            </div>
        </div>
        <div class="weui_cell">
            <div class="weui_cell_hd"><label for="" class="weui_label">开始时间</label></div>
            <div class="weui_cell_bd weui_cell_primary">
                <input class="weui_input" type="datetime-local" value="" placeholder="" id="startTime">
            </div>
        </div>
        <div class="weui_cell">
            <div class="weui_cell_hd"><label for="" class="weui_label">结束时间</label></div>
            <div class="weui_cell_bd weui_cell_primary">
                <input class="weui_input" type="datetime-local" value="" placeholder="" id="endTime">
            </div>
        </div>
        <div class="weui_cell">
            <div class="weui_cell_hd"><label class="weui_label">工时</label></div>
            <div class="weui_cell_bd weui_cell_primary">
                <input class="weui_input" type="text" placeholder="" id="hour">
            </div>
        </div>
        <div class="weui_cells_title">活动详情</div>
        <div class="weui_cells weui_cells_form">
        <div class="weui_cell">
            <div class="weui_cell_bd weui_cell_primary">
                <textarea class="weui_textarea" placeholder="请输入活动详情" rows="3" id="details"></textarea>
                <div class="weui_textarea_counter"><span>0</span>/200</div>
            </div>
        </div>
        <div class="bd spacing"> 
        	<a href="javascript:launch()" class="weui_btn weui_btn_primary">发起活动</a>
        </div>
    </div>
	</script>
<div class="weui_dialog_alert" hidden="hidden">
    <div class="weui_mask"></div>
    <div class="weui_dialog">
        <div class="weui_dialog_hd"><strong class="weui_dialog_title">弹窗标题</strong></div>
        <div class="weui_dialog_bd">弹窗内容，告知当前页面信息等</div>
        <div class="weui_dialog_ft">
            <a href="#" class="weui_btn_dialog primary" id="url">确定</a>
        </div>
    </div>
</div>
	<script src="static/js/zepto.min.js"></script>
    <script src="static/js/router.min.js"></script>
    <script src="static/js/activity.js"></script>
</body>
</html>