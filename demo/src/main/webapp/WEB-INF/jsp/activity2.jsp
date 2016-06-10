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
<title>活动</title>
</head>
<body class="activity">
	<div class="container" id="container">
		<div class="hd">
			<h1 class="page_title">${name}</h1>
		</div>
		<div class="bd">

			<div class="weui_cells">
				<div class="weui_cell">
					<div class="weui_cell_bd weui_cell_primary">
						<p>开始时间</p>
					</div>
					<div class="weui_cell_ft">${startTime}</div>
				</div>
			</div>
			<div class="weui_cells">
				<div class="weui_cell">
					<div class="weui_cell_bd weui_cell_primary">
						<p>结束时间</p>
					</div>
					<div class="weui_cell_ft">${endTime}</div>
				</div>
			</div>

			<div class="weui_cells weui_cells_access">
				<a class="weui_cell" href="/ensureList?activityId=${activityId}">
					<div class="weui_cell_bd weui_cell_primary">
						<p>成员反馈</p>
					</div>
					<div class="weui_cell_ft">
					</div>
				</a>
			</div>

			<div class="bd spacing">
				<a href="javascript:sendMail(${activityId})" class="weui_btn weui_btn_primary">群发提醒邮件</a>
			</div>

		</div>
	</div>
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
	<script type="text/javascript">
		$(function () {
			if(!localStorage.gyid){
				location.href = "/login";
			}
		});
		//关闭对话框
		function closeDialog(code){
			if(code==1) {
				localStorage.needRefresh = 1;
				location.href = "/index?uid=" + localStorage.gyid;
			}else if(code==0){
				$(".weui_dialog_alert").attr("hidden","hidden");
			}
		}
		function sendMail(activityId){
			$.ajax({
				url: 'mail/enSure?activityId=' + activityId,
				type: 'POST',
				dataType: 'json',
				error: function () {
					$(".weui_dialog_title").html("发送失败");
					$(".weui_dialog_bd").html("服务器被海王类劫持了！");
					$('#url').attr('href',"javascript:closeDialog(0)");
					$(".weui_dialog_alert").removeAttr("hidden");
				},
				success: function (data) {
					if(data.code==1){
						$(".weui_dialog_title").html("发送成功");
						$(".weui_dialog_bd").html("");
						$('#url').attr('href',"javascript:closeDialog(0)");
					}else{
						$(".weui_dialog_title").html("发送失败");
						$(".weui_dialog_bd").html(data.msg);
						$('#url').attr('href',"javascript:closeDialog(0)");
					}
					$(".weui_dialog_alert").removeAttr("hidden");
				}
			});
		}
	</script>
</body>
</html>