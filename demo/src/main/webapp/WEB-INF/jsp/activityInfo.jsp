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
<title>招募|${activity.name}</title>
</head>
<body class="activity">
<div style='margin:0 auto;width:0px;height:0px;overflow:hidden;'>
	<img src="static/images/mem.png" width='700'>
</div>
	<div class="container" id="container"></div>
	<script type="text/html" id="tpl_home">
		<div class="hd">
		    <h1 class="page_title">${activity.name}</h1>
		</div>
		<div class="bd">
		    <div class="weui_cells">
		        <div class="weui_cell">
		            <div class="weui_cell_bd weui_cell_primary">
		                <p>活动状态</p>
		            </div>
		            <div class="weui_cell_ft">${activity.status}</div>
		        </div>
		    </div>
		    <div class="weui_cells">
		        <div class="weui_cell">
		            <div class="weui_cell_bd weui_cell_primary">
		                <p>开始时间</p>
		            </div>
		            <div class="weui_cell_ft">${activity.startTime}</div>
		        </div>
		    </div>
		    <div class="weui_cells">
		        <div class="weui_cell">
		            <div class="weui_cell_bd weui_cell_primary">
		                <p>结束时间</p>
		            </div>
		            <div class="weui_cell_ft">${activity.endTime}</div>
		        </div>
		    </div>
		    
		    <div class="weui_cells weui_cells_access">
		        <a class="weui_cell" href="/member?activityId=${activity.id}">
		            <div class="weui_cell_bd weui_cell_primary">
		                <p>参与成员</p>
		            </div>
		            <div class="weui_cell_ft">
		            </div>
		        </a>
		    </div>
			<c:if test="${page<5}">
				<article class="weui_article">
					<section>
						<c:if test="${page<4}">
							<h3>活动详情</h3>
							<p>${activity.details}</p>
						</c:if>
						<c:if test="${page==4}">
							<h3>下载表格</h3>
							<p>请凭借活动id号${activity.id}，前往www.ustbvolunteer.com/download下载活动统计表</p>
						</c:if>
					</section>
				</article>
			</c:if>
			<c:if test="${page==5}">
				<div class="weui_cells_title">信息反馈</div>
				<div class="weui_cells weui_cells_form">
					<div class="weui_cell">
						<div class="weui_cell_bd weui_cell_primary">
							<textarea class="weui_textarea" placeholder="请输入反馈信息" rows="3" id="feedback"></textarea>
							<div class="weui_textarea_counter"><span>0</span>/200</div>
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${activity.join && page == 0}">
				<div class="bd spacing" id="joinBtn">
					<a href="javascript:joinAC(${activity.id})" class="weui_btn weui_btn_primary">报名</a>
				</div>
			</c:if>
			<c:if test="${page == 1}">
				<div class="bd spacing">
					<input type="hidden" id="pendId" value="${pendId}"/>
					<a href="javascript:approve(1)" class="weui_btn weui_btn_primary">接受</a>
					<a href="javascript:approve(0)" class="weui_btn weui_btn_warn">拒绝</a>
				</div>
			</c:if>
			<c:if test="${page == 2}">
				<div class="bd spacing">
					<a href="javascript:quitAC(${activity.id})" class="weui_btn weui_btn_warn">退出活动</a>
				</div>
			</c:if>
			<c:if test="${page == 3}">
				<div class="bd spacing">
					<a href="javascript:signIn(${activity.id})" class="weui_btn weui_btn_primary">签到</a>
				</div>
			</c:if>
			<c:if test="${page == 5}">
				<div class="bd spacing">
					<a href="javascript:feedback(${activity.id})" class="weui_btn weui_btn_primary">提交反馈</a>
				</div>
			</c:if>
			<input type="hidden" id="activityId" value="${activity.id}"/>
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