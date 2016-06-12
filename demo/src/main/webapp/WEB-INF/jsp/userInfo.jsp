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
<title>查看用户详情页</title>
</head>
<body class="activity">
	<div class="container" id="container">
		<div class="hd">
			<h1 class="page_title">${name}</h1>
		</div>
		<div class="weui_cells weui_cells_form">

			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">学院</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="text" disabled="disabled" value="${academy}">
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">班级</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="text" disabled="disabled" value="${className}">
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">性别</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="text" disabled="disabled" value="${sex}">
				</div>
			</div>
		</div>


		<div class="weui_cells weui_cells_form">
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">微信</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="text" disabled="disabled" value="${wechat}">
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">手机</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="text" disabled="disabled" value="${phone}">
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">邮箱</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="text" disabled="disabled" value="${mail}">
				</div>
			</div>

		</div>
		<div class="weui_cells weui_cells_access">
			<a class="weui_cell" href="/launch_user?uid=${uid}">
				<div class="weui_cell_bd weui_cell_primary">
					<p>TA发起的</p>
				</div>
				<div class="weui_cell_ft"></div>
			</a>

			<a class="weui_cell" href="/partake_user?uid=${uid}">
				<div class="weui_cell_bd weui_cell_primary">
					<p>TA参与的</p>
				</div>
				<div class="weui_cell_ft"></div>
			</a>

		</div>
		<c:if test="${page==0 || page==1}">
			<div class="bd spacing" id="addFriend">
				<a href="javascript:addFriend(${uid})" class="weui_btn weui_btn_primary">添加好友</a>
			</div>
		</c:if>
		<c:if test="${page==2}">
		<div class="bd spacing" id="delFriend">
			<a href="javascript:delFriend(${uid})" class="weui_btn weui_btn_warn">删除好友</a>
		</div>
		</c:if>
		<c:if test="${page==3}">
			<div class="bd spacing">
				<a href="javascript:ensureSign(${uid})" class="weui_btn weui_btn_primary">确认签到</a>
			</div>
			<br>
			<div class="bd spacing">
				<a href="javascript:closeDialog(1)" class="weui_btn weui_btn_primary">返回</a>
			</div>
		</c:if>
		<c:if test="${page==4}">
			<div class="bd spacing" id="invite">
				<a href="javascript:invite(${uid})" class="weui_btn weui_btn_primary">邀请</a>
			</div>
		</c:if>
		<c:if test="${page==5}">
			<div class="bd spacing" id="approve">
				<input type="hidden" id="uid" value="${uid}"/>
				<input type="hidden" id="pendId" value="${pendId}"/>
				<a href="javascript:approve(1)" class="weui_btn weui_btn_primary">接受</a>
				<a href="javascript:approve(0)" class="weui_btn weui_btn_warn">拒绝</a>
			</div>
		</c:if>
		<c:if test="${page==7}">
			<div class="bd spacing">
				<a href="javascript:kicked(${uid})" class="weui_btn weui_btn_warn">踢出成员</a>
			</div>
		</c:if>
		<input type="hidden" id="activityId" value="${activityId}"/>
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
			}else if(code==2){
				var activityId = $("#activityId").val();
				location.href = "/activity3?activityId=" + activityId;
			}
		}
		//添加好友
		function addFriend(uid){
			if(localStorage.gyid==uid){
				$(".weui_dialog_title").html("添加失败");
				$(".weui_dialog_bd").html("不能添加自己为好友！");
				$('#url').attr('href',"javascript:closeDialog(0)");
				$(".weui_dialog_alert").removeAttr("hidden");
			}else{
				$("#addFriend").attr("hidden","hidden");
				$.ajax({
					url: 'user/addFriend?uid=' + localStorage.gyid + '&friendId=' + uid,
					type: 'POST',
					dataType: 'json',
					error: function () {
						$(".weui_dialog_title").html("添加失败");
						$(".weui_dialog_bd").html("服务器被海王类劫持了！");
						$('#url').attr('href',"javascript:closeDialog(0)");
						$(".weui_dialog_alert").removeAttr("hidden");
						$("#addFriend").removeAttr("hidden");
					},
					success: function (data) {
						if(data.code==1){
							$(".weui_dialog_title").html("添加成功");
							$(".weui_dialog_bd").html("");
							$('#url').attr('href',"javascript:closeDialog(0)");
						}else{
							$(".weui_dialog_title").html("添加失败");
							$(".weui_dialog_bd").html(data.msg);
							$('#url').attr('href',"javascript:closeDialog(0)");
							$("#addFriend").removeAttr("hidden");
						}
						$(".weui_dialog_alert").removeAttr("hidden");
					}
				});
			}
		}
		//删除好友
		function delFriend(uid){
			$("#delFriend").attr("hidden","hidden");
			$.ajax({
				url: 'user/delFriend?uid=' + localStorage.gyid + '&friendId=' + uid,
				type: 'POST',
				dataType: 'json',
				error: function () {
					$(".weui_dialog_title").html("删除失败");
					$(".weui_dialog_bd").html("服务器被海王类劫持了！");
					$('#url').attr('href',"javascript:closeDialog(0)");
					$(".weui_dialog_alert").removeAttr("hidden");
					$("#delFriend").removeAttr("hidden");
				},
				success: function (data) {
					if(data.code==1){
						$(".weui_dialog_title").html("删除成功");
						$(".weui_dialog_bd").html("");
						$('#url').attr('href',"javascript:closeDialog(1)");
					}else{
						$(".weui_dialog_title").html("删除失败");
						$(".weui_dialog_bd").html(data.msg);
						$('#url').attr('href',"javascript:closeDialog(0)");
						$("#delFriend").removeAttr("hidden");
					}
					$(".weui_dialog_alert").removeAttr("hidden");
				}
			});
		}
		//审批报名
		function approve(code){
			$("#approve").attr("hidden","hidden");
			var uid = $("#uid").val();
			var pendId = $("#pendId").val();
			var activityId = $("#activityId").val();
			var url;
			if(code==1){
				url = "activity/approveUser?id="+pendId+"&uid="+uid+"&activityId="+activityId+"&result=1";
			}else{
				url = "activity/approveUser?id="+pendId+"&uid="+uid+"&activityId="+activityId+"&result=0";
			}
			$.ajax({
				url: url,
				type: 'POST',
				dataType: 'json',
				error: function () {
					$(".weui_dialog_title").html("审批失败");
					$(".weui_dialog_bd").html("服务器被海王类劫持了！");
					$('#url').attr('href',"javascript:closeDialog(0)");
					$(".weui_dialog_alert").removeAttr("hidden");
					$("#approve").removeAttr("hidden");
				},
				success: function (data) {
					if(data.code==1){
						$(".weui_dialog_title").html("审批成功");
						$(".weui_dialog_bd").html("");
						$('#url').attr('href',"javascript:closeDialog(1)");
					}else{
						$(".weui_dialog_title").html("审批失败");
						$(".weui_dialog_bd").html(data.msg);
						$('#url').attr('href',"javascript:closeDialog(0)");
						$("#approve").removeAttr("hidden");
					}
					$(".weui_dialog_alert").removeAttr("hidden");
				}
			});
		}
		//邀请好友
		function invite(uid){
			$("#invite").attr("hidden","hidden");
			var activityId = $("#activityId").val();
			$.ajax({
				url: 'activity/invite?uid='+uid+'&activityId='+activityId,
				type: 'POST',
				dataType: 'json',
				error: function () {
					$(".weui_dialog_title").html("邀请失败");
					$(".weui_dialog_bd").html("服务器被海王类劫持了！");
					$('#url').attr('href',"javascript:closeDialog(0)");
					$(".weui_dialog_alert").removeAttr("hidden");
					$("#invite").removeAttr("hidden");
				},
				success: function (data) {
					if(data.code==1){
						$(".weui_dialog_title").html("邀请成功");
						$(".weui_dialog_bd").html("");
						$('#url').attr('href',"javascript:closeDialog(1)");
					}else{
						$(".weui_dialog_title").html("邀请失败");
						$(".weui_dialog_bd").html(data.msg);
						$('#url').attr('href',"javascript:closeDialog(0)");
						$("#invite").removeAttr("hidden");
					}
					$(".weui_dialog_alert").removeAttr("hidden");
				}
			});
		}
		//确认签到
		function ensureSign(uid){
			var activityId = $("#activityId").val();
			$.ajax({
				url: 'activity/signIn?uid='+uid+'&activityId='+activityId+"&type=1",
				type: 'POST',
				dataType: 'json',
				error: function () {
					$(".weui_dialog_title").html("确认失败");
					$(".weui_dialog_bd").html("服务器被海王类劫持了！");
					$('#url').attr('href',"javascript:closeDialog(0)");
					$(".weui_dialog_alert").removeAttr("hidden");
				},
				success: function (data) {
					if(data.code==1){
						$(".weui_dialog_title").html("确认成功");
						$(".weui_dialog_bd").html("");
						$('#url').attr('href',"javascript:closeDialog(2)");
					}else{
						$(".weui_dialog_title").html("确认失败");
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