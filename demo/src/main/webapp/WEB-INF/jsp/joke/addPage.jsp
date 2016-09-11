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
  <link rel="stylesheet" href="/static/css/example.css">
  <title>列表</title>
</head>
<body class="activity">
<div class="container" id="container">
  <input type="hidden" id="alertId" name="alertId" value="${alertId}">
  <input type="hidden" id="pageId" name="pageId" value="${pageId}">
  <div class="weui_cells weui_cells_form">
    <div class="weui_cell">
      <h1 class="page_title" style="font-size:20px;color:#000000;">插入当前
        <select style="font-size:20px;color:#000000;" id="currentPage">
          <option selected value="${pageNum+1}">最新</option>
          <c:if test="${pageNum>0}">
            <c:forEach begin="0" end="${pageNum-1}" step="1" var="num">
              <c:choose>
                <c:when test="${(pageNum-num)==currentPage}">
                  <option value="${pageNum-num}" selected>第${pageNum-num}</option>
                </c:when>
                <c:otherwise>
                  <option value="${pageNum-num}">第${pageNum-num}</option>
                </c:otherwise>
              </c:choose>
            </c:forEach>
          </c:if>
        </select>页
      </h1>
    </div>
    <div class="weui_cell weui_cell_switch">
      <div class="weui_cell_hd weui_cell_primary">是否需要回答</div>
      <div class="weui_cell_ft">
        <c:choose>
          <c:when test="${prompt==1}">
            <input class="weui_switch" type="checkbox" id="isShow" checked onchange="javascript:isShow()">
          </c:when>
          <c:otherwise>
            <input class="weui_switch" type="checkbox" id="isShow" onchange="javascript:isShow()">
          </c:otherwise>
        </c:choose>
      </div>
    </div>
    <div class="weui_cell">
      <div class="weui_cell_hd"><label class="weui_label">显示内容</label></div>
      <div class="weui_cell_bd weui_cell_primary">
        <input class="weui_input" type="text" placeholder="请输入显示内容" value="${content}" id="content">
      </div>
    </div>
    <c:choose>
      <c:when test="${prompt==1}">
        <div id="hasAnswer">
      </c:when>
      <c:otherwise>
        <div id="hasAnswer" hidden="hidden">
      </c:otherwise>
    </c:choose>
          <div class="weui_cell">
            <div class="weui_cell_hd"><label class="weui_label">答案</label></div>
            <div class="weui_cell_bd weui_cell_primary">
              <input class="weui_input" type="text" placeholder="请输入答案" value="${answer}" id="answer">
            </div>
          </div>
          <div class="weui_cell">
            <div class="weui_cell_hd"><label class="weui_label">答错提示语</label></div>
            <div class="weui_cell_bd weui_cell_primary">
              <input class="weui_input" type="text" placeholder="请输入提示语" value="${wrong}" id="wrong">
            </div>
          </div>
        </div>
  </div>
    <br><br><br><br>
    <div id="btn">
    <c:choose>
      <c:when test="${pageId >0}">
        <a href="javascript:savePage()" class="weui_btn weui_btn_primary">确认修改</a>
        <a href="javascript:deletePage()" class="weui_btn weui_btn_warn">删除弹窗</a>
      </c:when>
      <c:otherwise>
        <a href="javascript:savePage()" class="weui_btn weui_btn_primary" id="addBtn">确认新建</a>
      </c:otherwise>
    </c:choose>
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
<script src="/static/js/zepto.min.js"></script>
<script src="/static/js/router.min.js"></script>
  <script type="text/javascript">
    $(function() {
      if (!localStorage.jokeId) {
        location.href = "login";
      }
    });
    //是否显示答案相关输入框
    function isShow(){
      if($("#isShow").attr("checked")){
        $("#hasAnswer").removeAttr("hidden");
      }else{
        $("#hasAnswer").attr("hidden","hidden");
      }
    }
    //新建修改弹窗
    function savePage(){
      $("#btn").attr("hidden","hidden");
      var pageNum = $("#pageNum").val();
      var content = $("#content").val();
      var prompt = $("#isShow").attr("checked")?1:0;
      var answer = $("#answer").val();
      var wrong = $("#wrong").val();
      var page = $("#currentPage").val();
      var alertId = $("#alertId").val();
      var url = "alert/savePage?page="+page+"&alertId="+alertId+"&content="+content+"&prompt="+prompt;
      var pageId = $("#pageId").val();
      var msg = null;
      if(alertId==null||alertId=="") {
        msg = "未获取到弹窗！！";
      }else if(content==null||content=="")
        msg = "显示内容不能为空！";
      else if(prompt==1&&(answer==null||answer==""||wrong==null||wrong==""))
        msg = "答案或答错提示不能为空！";
      if(pageId!=null&&pageId>0)
        url += "&id="+pageId;
      if(prompt==1)
        url += "&answer="+answer+"&wrong="+wrong;
      if(msg == null){
        $.ajax({
          url: url,
          type: 'POST',
          dataType: 'json',
          error: function () {
            $(".weui_dialog_title").html("操作失败");
            $(".weui_dialog_bd").html("服务器被海王类劫持了！");
            $('#url').attr('href',"javascript:closeDialog(1)");
            $(".weui_dialog_alert").removeAttr("hidden");
            $("#btn").removeAttr("hidden");
          },
          success: function (data) {
            if(data.code==1){
              $(".weui_dialog_title").html("操作成功");
              $(".weui_dialog_bd").html("");
              $('#url').attr('href',"javascript:closeDialog(2)");
              localStorage.jokeId = data.result;
            }else{
              $(".weui_dialog_title").html("提示");
              $(".weui_dialog_bd").html(data.result);
              $('#url').attr('href',"javascript:closeDialog(1)");
            }
            $("#btn").removeAttr("hidden");
            $(".weui_dialog_alert").removeAttr("hidden");
          }
        });
      }else{
        $(".weui_dialog_title").html("操作失败");
        $(".weui_dialog_bd").html(msg);
        $('#url').attr('href',"javascript:closeDialog(1)");
        $(".weui_dialog_alert").removeAttr("hidden");
        $("#btn").removeAttr("hidden");
      }
    }
    //关闭对话框
    function closeDialog(type){
      if(type==1){
        $(".weui_dialog_alert").attr("hidden","hidden");
      }else if(type==2){
        location.href = "pageList?alertId="+$("#alertId").val();
      }
    }
  </script>
</body>
</html>