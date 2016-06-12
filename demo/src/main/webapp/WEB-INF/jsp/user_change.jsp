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
<title>修改个人信息页面</title>
</head>
<body class="activity">
	<div class="container" id="container">
		<div class="weui_cells weui_cells_form">
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">姓名</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="text" placeholder="请输入姓名" value="${name}" id="name">
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">学院</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<select class="weui_select" id="academy">
						<c:forEach items="${academyList}" var="xueyuan">
							<c:if test="${academy eq xueyuan}">
								<option selected value="${xueyuan}">${xueyuan}</option>
							</c:if>
							<option value="${xueyuan}">${xueyuan}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">班级</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="text" placeholder="请输入班级" value="${className}" id="className">
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">性别</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<select class="weui_select" id="sex">
						<c:if test="${sex==0}">
							<option selected value="0">女</option>
							<option value="1">男</option>
						</c:if>
						<c:if test="${sex==1}">
							<option value="0">女</option>
							<option selected value="1">男</option>
						</c:if>
					</select>
				</div>
			</div>
		</div>

		<div class="weui_cell weui_cell_switch">
			<div class="weui_cell_hd weui_cell_primary">是否对外显示以下信息</div>
			<div class="weui_cell_ft">
				<c:if test="${show==1}">
					<input class="weui_switch" type="checkbox" id="isShow" checked>
				</c:if>
				<c:if test="${show==0}">
					<input class="weui_switch" type="checkbox" id="isShow">
				</c:if>
			</div>
		</div>

		<div class="weui_cells weui_cells_form">
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">微信</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="text" placeholder="请输入微信号" value="${wechat}" id="wechat">
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">手机</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="number" placeholder="请输入手机号" value="${phone}" id="phone">
				</div>
			</div>
			<div class="weui_cell">
				<div class="weui_cell_hd"><label class="weui_label">邮箱</label></div>
				<div class="weui_cell_bd weui_cell_primary">
					<input class="weui_input" type="email" placeholder="请输入邮箱" value="${mail}" id="mail">
				</div>
			</div>
			<div class="bd spacing">
				<a href="javascript:editUser()" class="weui_btn weui_btn_primary">确认修改</a>
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
		var mail = $("#mail").val();
		function editUser(){
			if(!localStorage.gyid){
				location.href = "/login";
			}
			var name = $("#name").val();
			var academy = $("#academy").val();
			var className = $("#className").val();
			var phone = $("#phone").val();
			var wechat = $("#wechat").val();
			var sex = $("#sex").val();
			var show = $("#isShow").attr("checked")?1:0;
			var newMail = $("#mail").val();
			if(newMail && newMail!="" && newMail!=mail){//若修改了邮箱
				$.ajax({
					url: 'mail/testMail?uid='+localStorage.gyid+'&mail='+newMail,
					type: 'POST',
					dataType: 'json',
					error: function () {
						alert("服务器被海王类劫持了,验证邮件发送失败！");
					},
					success: function (data) {
						alert(data.msg);
					}
				});
			}
			if(name){
				$.ajax({
					url: 'user/updateUserInfo',
					type: 'POST',
					data:{
						id:localStorage.gyid,
						name:name,
						academy:academy,
						className:className,
						phone:phone,
						wechat:wechat,
						sex:sex,
						show:show,
						mail:newMail
					},
					dataType: 'json',
					error: function () {
						$(".weui_dialog_title").html("修改失败");
						$(".weui_dialog_bd").html("服务器被海王类劫持了！");
						$('#url').attr('href',"javascript:closeDialog(0)");
						$(".weui_dialog_alert").removeAttr("hidden");
					},
					success: function (data) {
						if(data.code==1){
							$(".weui_dialog_title").html("修改成功");
							$(".weui_dialog_bd").html("");
							$('#url').attr('href',"javascript:closeDialog(2)");
						}else{
							$(".weui_dialog_title").html("修改失败");
							$(".weui_dialog_bd").html(data.msg);
							$('#url').attr('href',"javascript:closeDialog(0)");
						}
						$(".weui_dialog_alert").removeAttr("hidden");
					}
				});
			}else{
				$(".weui_dialog_title").html("修改失败");
				$(".weui_dialog_bd").html("请仔细检查资料是否填写完善！");
				$('#url').attr('href',"javascript:closeDialog(0)");
				$(".weui_dialog_alert").removeAttr("hidden");
			}
		}
		//关闭对话框
		function closeDialog(code){
			if(code==1) {
				localStorage.needRefresh = 1;
				location.href = "/index?uid=" + localStorage.gyid;
			}else if(code==0){
				$(".weui_dialog_alert").attr("hidden","hidden");
			}else if(code==2){
				location.reload();
			}
		}
	</script>
</body>
</html>