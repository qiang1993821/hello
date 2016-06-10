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
    <title>下载反馈表</title>
</head>
<body>
<div align="center">
    <div class="weui_cells_title">输入活动id下载反馈表格</div>
    <div class="weui_cell">
        <div class="weui_cell_hd">活动id：<label class="weui_label"></label></div>
        <div class="weui_cell_bd weui_cell_primary">
            <input class="weui_input" type="number" placeholder="请输入活动id号" id="activityId">
        </div>
    </div>
    <br>
    <br>
    <div class="bd spacing">
        <a href="javascript:download()" class="weui_btn weui_btn_primary">下载</a>
    </div>
</div>
<script src="static/js/zepto.min.js"></script>
<script src="static/js/router.min.js"></script>
<script type="text/javascript">
    function download(){
        var activityId = $("#activityId").val();
        if(activityId==null){
            alert("请输入活动id")
        }else{
            location.href = 'activity/download?activityId=' + activityId;
//            $.ajax({
//                url: 'activity/download?activityId=' + activityId,
//                type: 'POST',
//                dataType: 'json',
//                error: function () {
//                    alert("下载失败！服务器被海王类劫持了！")
//                },
//                success: function (data) {
//                    if(data.code==0){
//                        alert(data.msg);
//                    }
//                }
//            });
        }
    }
</script>
</body>
</html>
