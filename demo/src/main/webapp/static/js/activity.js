$(function () {
    //tab切换
    var router = new Router({
        container: '#container',
        enterTimeout: 250,
        leaveTimeout: 250
    });

    var home = {
        url: '/',
        className: 'home',
        render: function () {
            return $('#tpl_home').html();
        },
        bind: function () {
            // searchbar
            $('#container').on('focus', '#search_input', function () {
                var $weuiSearchBar = $('#search_bar');
                $weuiSearchBar.addClass('weui_search_focusing');
            }).on('blur', '#search_input', function () {
                var $weuiSearchBar = $('#search_bar');
                $weuiSearchBar.removeClass('weui_search_focusing');
                if ($(this).val()) {
                    $('#search_text').hide();
                } else {
                    $('#search_text').show();
                }
            }).on('input', '#search_input', function () {
                var $searchShow = $("#search_show");
                if ($(this).val()) {
                    $searchShow.show();
                } else {
                    $searchShow.hide();
                }
            }).on('touchend', '#search_cancel', function () {
                $("#search_show").hide();
                $('#search_input').val('');
            }).on('touchend', '#search_clear', function () {
                $("#search_show").hide();
                $('#search_input').val('');
            });
            // tabbar
            $('#container').on('click', '.weui_navbar_item', function () {
                $(this).addClass('weui_bar_item_on').siblings('.weui_bar_item_on').removeClass('weui_bar_item_on');
            });
        }
    };
   

    router.push(home)
        .setDefault('/')
        .init();


    $('.weui_tabbar a').on('click',function(){ 
        // console.log($(this).index());
        $('.lists>div').hide();
        $('.lists>div').eq($(this).index()).show();
    })


    // .container 设置了 overflow 属性, 导致 Android 手机下输入框获取焦点时, 输入法挡住输入框的 bug
    // 相关 issue: https://github.com/weui/weui/issues/15
    // 解决方法:
    // 0. .container 去掉 overflow 属性, 但此 demo 下会引发别的问题
    // 1. 参考 http://stackoverflow.com/questions/23757345/android-does-not-correctly-scroll-on-input-focus-if-not-body-element
    //    Android 手机下, input 或 textarea 元素聚焦时, 主动滚一把
    if (/Android/gi.test(navigator.userAgent)) {
        window.addEventListener('resize', function () {
            if (document.activeElement.tagName == 'INPUT' || document.activeElement.tagName == 'TEXTAREA') {
                window.setTimeout(function () {
                    document.activeElement.scrollIntoViewIfNeeded();
                }, 0);
            }
        })
    }

    //var activityId = $("#activityId").val();
    //if(activityId && activityId!=""){
    //    //获取活动信息，给各项填入数据，只有活动在审核状态适用，目前预留
    //}
});
//点击报名活动按钮
function joinAC(activityId){
    if(localStorage.gyid){
        $.ajax({
            url: 'activity/join?uid=' + localStorage.gyid + '&activityId=' + activityId,
            type: 'POST',
            dataType: 'json',
            error: function () {
                $(".weui_dialog_title").html("报名失败");
                $(".weui_dialog_bd").html("服务器被海王类劫持了！");
                $('#url').attr('href',"javascript:closeDialog(0)");
                $(".weui_dialog_alert").removeAttr("hidden");
            },
            success: function (data) {
                if(data.code==1){
                    $(".weui_dialog_title").html("报名成功");
                    $(".weui_dialog_bd").html("");
                    $('#url').attr('href',"javascript:closeDialog(1)");
                }else{
                    $(".weui_dialog_title").html("报名失败");
                    $(".weui_dialog_bd").html(data.msg);
                    $('#url').attr('href',"javascript:closeDialog(0)");
                }
                $(".weui_dialog_alert").removeAttr("hidden");
            }
        });
    }else{
        location.href = "/login?activityId="+activityId;
    }
}
//关闭对话框
function closeDialog(code){
    localStorage.needRefresh = 1;
    location.href = "/index?uid="+localStorage.gyid;
}
