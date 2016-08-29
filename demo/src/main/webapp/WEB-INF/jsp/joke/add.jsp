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
  <link rel="stylesheet" href="/static/css/login.css">
  <link rel="stylesheet" href="/static/css/example.css">
  <title>新建</title>
</head>
<body class="login">
<center>
  <form action='addAlert' method='post' enctype='multipart/form-data' id="addAlert">
    <input type="file" onchange='PreviewImage(this)' hidden="hidden" id="upload" name="upload"/>
    <div id="imgPreview">
      <img src="${img}" style='width:100px; '/>
    </div>
    <a href="javascript:upload()" class="weui_btn weui_btn_default">点击选择图片</a>
    <br><hr>
    <div align="center">图片和标题为转发至朋友圈后显示的图片和标题，有没有人上当，就看你怎么设置了！</div>
    <hr><br>
    <div class="title"><input type="text" placeholder="请填写标题" id="title" name="title" value="${title}"></div>
    <br><br><br><br>
    <c:choose>
      <c:when test="${alertId >0}">
        <input type="hidden" id="alertId" name="alertId" value="${alertId}">
        <a href="javascript:addAlert()" class="weui_btn weui_btn_primary">确认修改</a>
        <a href="" class="weui_btn weui_btn_default">预览弹窗</a>
        <a href="javascript:addAlert()" class="weui_btn weui_btn_warn">删除弹窗</a>
      </c:when>
      <c:otherwise>
        <a href="javascript:addAlert()" class="weui_btn weui_btn_primary">确认新建</a>
      </c:otherwise>
    </c:choose>
    <div id="uid" hidden="hidden"></div>
  </form>
</center>
<!-- 搜索结束 -->
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
<input type="hidden" id="result" value="${result}">
<script src="/static/js/zepto.min.js"></script>
<script src="/static/js/router.min.js"></script>
<script type="text/javascript">
  $(function() {
    if(!localStorage.jokeId){
      location.href = "login";
    }
    document.getElementById("uid").innerHTML = '<input type="hidden" name="uid" value="'+localStorage.jokeId+'">'
    var result = $("#result").val();
    if(result!=null&&result!=""){
      $(".weui_dialog_title").html("提示");
      $(".weui_dialog_bd").html(result);
      $('#url').attr('href',"javascript:closeDialog(1)");
      if(result=="操作成功")
        $('#url').attr('href',"javascript:closeDialog(2)");
      $(".weui_dialog_alert").removeAttr("hidden");
      $("#btn").removeAttr("hidden");
    }
  });
  //上传
  function upload(){
    return $("#upload").click();
  }
  function addAlert(){
    var title = $("#title").val();
    var img = $("#upload").val();
    if(img=="") {
      $(".weui_dialog_title").html("上传失败");
      $(".weui_dialog_bd").html("图片或标题不能为空！");
      $('#url').attr('href',"javascript:closeDialog(0)");
      $(".weui_dialog_alert").removeAttr("hidden");
      $("#btn").removeAttr("hidden");
    }else
      $("#addAlert").submit();
  }
  function PreviewImage(imgFile) {
    var pattern = /(\.*.jpg$)|(\.*.png$)|(\.*.jpeg$)|(\.*.gif$)|(\.*.bmp$)/;
    if(!pattern.test(imgFile.value)) {
      $(".weui_dialog_title").html("上传失败");
      $(".weui_dialog_bd").html("系统仅支持jpg/jpeg/png/gif/bmp格式的照片！");
      $('#url').attr('href',"javascript:closeDialog(0)");
      $(".weui_dialog_alert").removeAttr("hidden");
      $("#btn").removeAttr("hidden");
    } else if(imgFile.files[0].size>1024000){
      $(".weui_dialog_title").html("上传失败");
      $(".weui_dialog_bd").html("图片不要超过1M！");
      $('#url').attr('href',"javascript:closeDialog(0)");
      $(".weui_dialog_alert").removeAttr("hidden");
      $("#btn").removeAttr("hidden");
    }else{
      var path;
      if(document.all){//IE
        imgFile.select();
        path = document.selection.createRange().text;
        document.getElementById("imgPreview").innerHTML="";
        document.getElementById("imgPreview").style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',sizingMethod='scale',src=\"" + path + "\")";//使用滤镜效果
      } else{//FF
        path = URL.createObjectURL(imgFile.files[0]);
        document.getElementById("imgPreview").innerHTML = "<img width='100px' src='"+path+"'/>";
      }
    }
  }
  //关闭对话框
  function closeDialog(type){
    if(type==1){
      location.href = "index";
    }else if(type==2){
      $("#result").attr("value","");
      location.href = "pageList?alertId="+$("#alertId").val();
    }else{
      $(".weui_dialog_alert").attr("hidden","hidden");
    }
  }
</script>
</body>
</html>





