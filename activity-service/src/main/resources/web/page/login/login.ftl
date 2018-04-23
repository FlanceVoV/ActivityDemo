<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
<head>
    <meta charset="UTF-8">
    <title></title>
</head>
<body>

    账号：<input type="text" id="account" name="account" value="e" /><br />
    密码：<input type="password" id="password" name="password" value="123" />
    <button type="button" id="subbut">登录</button>


</body>

<script src="http://192.168.1.103:8081/lib/jquery-1.10.2.min.js"></script>

<script>
    $(function(){
        $("#subbut").click(login());

    })


    function login(){
        var $account = $("#account").val();
        var $password = $("#password").val();
        var sUrl = "/activity/user/login";
        var oData = {"account":$account,"password":$password};
        ajaxFun(sUrl,"post",oData,function(oResultData){
            console.log(oResultData);
            alert("success");
        })
    }

    function ajaxFun(sUrl,sType,oData,fSuccess){
        $.ajax({
            url:sUrl,
            type:sType,
            data:oData,
            success:fSuccess,
            error:function(oMsg){
                console.log(oMsg);
                alert("error!");
            }
        })

    }

</script>

</html>