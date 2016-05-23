<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  通过传过来的code，用weixin的api获取access_token，存入cookie
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>你好你好</title>
</head>
<body>
<c:forEach items="${list}" var="var" varStatus="vs">
<div>${var}</div><br>
</c:forEach>
绿蚁新醅酒，春泥小火炉
</body>
</html>
