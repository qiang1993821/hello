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
<title>参与成员页</title>
</head>
<body class="activity">
	<div class="container" id="container">
		<div class="bd">
			<div class="weui_cells weui_cells_access">
				<c:forEach items="${infoList}" var="info">
					<a class="weui_cell" href="${info.url}">
						<div class="weui_cell_bd weui_cell_primary">
							<p>${info.name}</p>
						</div>
						<div class="weui_cell_ft">${info.info}</div>
					</a>
				</c:forEach>
				<c:if test="${infoList==null || infoList.size()==0}">
					<a class="weui_cell" href="#">
						<div class="weui_cell_bd weui_cell_primary">
							<p>暂无相关数据</p>
						</div>
						<div class="weui_cell_ft"></div>
					</a>
				</c:if>
			</div>
		</div>
	</div>
	<script src="static/js/zepto.min.js"></script>
    <script src="static/js/router.min.js"></script>
</body>
</html>