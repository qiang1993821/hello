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
<span class="name">活动助手</span>
<div class="username"><input type="text" placeholder="请填写邮箱" id="username"></div>
<div class="pwd"><input type="password" placeholder="请填写密码" id="pwd"></div>
<a href="#" class="weui_btn weui_btn_primary btn" onclick="login()">登录</a>
<a href="#" class="weui_btn weui_btn_warn btn" onclick="login()">忘记密码</a>
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
</body>
</html>