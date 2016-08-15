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
  <title>你好你好</title>
</head>
<body>
<c:choose>
  <c:when test="${aaa eq 1}">
    <div id="aaa">123</div>
  </c:when>
  <c:otherwise>
    <div id="aaa">456</div>
  </c:otherwise>
</c:choose>
绿蚁新醅酒，春泥小火炉
<script type="text/javascript">
  $("#aaa").html("www");
</script>
</body>
</html>
