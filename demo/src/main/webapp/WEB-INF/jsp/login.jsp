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
<link rel="stylesheet" href="static/css/login.css">
<script src="static/js/zepto.min.js"></script>
<title>登录</title>
</head>
<body class="login">
	<div class="userPhoto"><img src="static/images/head.jpg" alt=""></div>
	<span class="name">志愿者平台</span>
	<div class="username"><input type="text" placeholder="请填写名字" id="username"></div>
	<a href="#" class="weui_btn weui_btn_primary btn" onclick="login()">登录</a>
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
	<input type="hidden" id="activityId" value="${activityId}"/>
	<script type="text/javascript">
		$(function() {
			var activityId = $("#activityId").val();
			if(localStorage.gyid){
				if(activityId==0) {
					location.href = "/index";
				} else{
					location.href = "/activity?activityId="+activityId;
				}
			}
		});
		function login(){
			var activityId = $("#activityId").val();
			var name = $("#username").val();
			if(name==null||name==""){
				$(".weui_dialog_title").html("登录失败");
				$(".weui_dialog_bd").html("用户名不能为空！");
			}else{
				$.ajax({
					url: 'user/login?username=' + name,
					type: 'POST',
					dataType: 'json',
					error: function () {
						$(".weui_dialog_title").html("登录失败");
						$(".weui_dialog_bd").html("网络传输错误！");
					},
					success: function (data) {
						if(data.code==1){
							$(".weui_dialog_title").html("登录成功");
							$(".weui_dialog_bd").html("");
							$('#url').attr('href',activityId==0?"/index":"/activity?activityId="+activityId);
							localStorage.gyid = data.result;
						}else{
							$(".weui_dialog_title").html("登录失败");
							$(".weui_dialog_bd").html(data.result);
							$('#url').attr('href',activityId==0?"/login":"/login?activityId="+activityId);
						}
					}
				});
			}
			$(".weui_dialog_alert").removeAttr("hidden");
		}
	</script>
</body>
</html>