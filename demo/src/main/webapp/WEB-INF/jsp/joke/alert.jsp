<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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
  <title>${title}</title>
  <script type="text/javascript">
    var _hmt = _hmt || [];
    (function() {
      var hm = document.createElement("script");
      hm.src = "//hm.baidu.com/hm.js?97f7f6d84c6771a74c86e1837c2e9751";
      var s = document.getElementsByTagName("script")[0];
      s.parentNode.insertBefore(hm, s);
    })();
  </script>

</head>
<body>
<div style='margin:0 auto;width:0px;height:0px;overflow:hidden;'>
  <img src="${imgUrl}" width='700'>
</div>
<img src="/static/images/joke_bg.jpg" style="width:100%;">
<br><br>
<h1 style="font-weight: bold;font-size: 19px;color: #FF0000;">长按上图识别图中二维码</h1><br>
<h1 style="font-weight: bold;font-size: 19px;color: #FFCC00;">或搜索公众号“弓一”</h1><br>
<h1 style="font-weight: bold;font-size: 19px;color: #00FF00;">或搜索公众号“roc_gongyi”</h1>
<c:forEach items="${alertList}" var="var">
  <c:choose>
    <c:when test="${var.prompt eq 0}">
      <script type="text/javascript">
        alert("${var.content}");
      </script>
    </c:when>
    <c:otherwise>
      <script type="text/javascript">
        var value = prompt("${var.content}", '');
        while(value != "${var.answer}") {
          if(value == null) {
            alert('太天真了，怎么可能取消~');
            value = prompt("${var.content}", '');
          }else{
            alert("${var.wrong}");
            value = prompt("${var.content}", '');
          }
        }
      </script>
    </c:otherwise>
  </c:choose>
</c:forEach>
</body>
</html>