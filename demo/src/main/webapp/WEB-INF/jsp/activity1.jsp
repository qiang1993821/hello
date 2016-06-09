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
			<div class="weui_cells weui_cells_access">
				<a class="weui_cell" href="/activityInfo?activityId=${activityId}">
					<div class="weui_cell_bd weui_cell_primary">
						<p>转发活动</p>
					</div>
					<div class="weui_cell_ft">
					</div>
				</a>
			</div>
			<div class="weui_cells weui_cells_access">
				<a class="weui_cell" href="javascript:;">
					<div class="weui_cell_bd weui_cell_primary">
						<p>邀请好友</p>
					</div>
					<div class="weui_cell_ft">
					</div>
				</a>
			</div>
			<div class="weui_cells weui_cells_access">
				<a class="weui_cell" href="javascript:;">
					<div class="weui_cell_bd weui_cell_primary">
						<p>报名申请</p>
					</div>
					<div class="weui_cell_ft">
					</div>
				</a>
			</div>
			<div class="weui_cells weui_cells_access">
				<a class="weui_cell" href="javascript:;">
					<div class="weui_cell_bd weui_cell_primary">
						<p>参与成员</p>
					</div>
					<div class="weui_cell_ft">
					</div>
				</a>
			</div>

			<div class="bd spacing">
				<a href="javascript:;" class="weui_btn weui_btn_warn">删除活动</a>
			</div>

		</div>
	</div>
	<script src="static/js/zepto.min.js"></script>
    <script src="static/js/router.min.js"></script>
</body>
</html>