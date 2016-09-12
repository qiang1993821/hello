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
  <link rel="stylesheet" href="/static/css/weui.min.css"/>
  <link rel="stylesheet" href="/static/css/reset.css">
  <link rel="stylesheet" href="/static/css/index.css">
  <link rel="stylesheet" href="/static/css/example.css">
  <script src="/static/js/zepto.min.js"></script>
  <title>我的弹窗</title>
</head>
<body>
<div class="hd">
  <h1 class="page_title" style="font-size:20px;color:#000000;">点击编辑弹窗</h1>
</div>
<hr>
<c:choose>
  <c:when test="${not empty alertList}">
    <c:forEach items="${alertList}" var="alert">
      <a href="${alert.url}" class="weui_media_box weui_media_appmsg">
        <div class="weui_media_hd">
          <img class="weui_media_appmsg_thumb" src="${alert.imgUrl}" alt="图片无法显示">
        </div>
        <div class="weui_media_bd">
          <h4 class="weui_media_title">${alert.title}</h4>
        </div>
      </a>
    </c:forEach>
  </c:when>
  <c:otherwise>
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
  <script type="text/javascript">
    $(".weui_dialog_title").html("提示");
    $(".weui_dialog_bd").html("您还未建立弹窗");
    $('#url').attr('href',"javascript:closeDialog()");
    $(".weui_dialog_alert").removeAttr("hidden");
    function closeDialog(){
        location.href = "index";
    }
  </script>
  </c:otherwise>
</c:choose>
<hr>
</body>
</html>
