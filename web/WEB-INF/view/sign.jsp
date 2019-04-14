<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.lntuplus.model.SignModel" %>
<%
    List<SignModel> list = (List<SignModel>) request.getAttribute("list");
    if (list == null) {
        list = new ArrayList<>();
    }
    Integer number = (Integer) request.getAttribute("number");
    if (number == null) {
        number = 0;
    }
%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>签到情况查询</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <link rel="shortcut icon" href="/favicon.ico">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <link rel="stylesheet"
          href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
    <link rel="stylesheet"
          href="//g.alicdn.com/msui/sm/0.6.2/css/sm-extend.min.css">

</head>
<body>

<div class="content">
    <form action="get" method="post">
        班级：<input type="text" name="iClass"/> <br> <br>
        日期：<input type="text" name="day"/> <br> <br>
        第：<input type="text" name="no"/>节 <br> <br>
        <input type="submit" value="提交"/>
    </form>

    <table style="width: 100%">

        <tr>
            <td>姓名</td>
            <td>班级</td>
            <td>学号</td>
        </tr>
        <tr>未签到人数：<% out.print(number); %></tr>
        <%
            for (int i = 0; i < list.size(); i++) {
        %>
        <tr>
            <td>
                <%
                    out.print(list.get(i).getName());
                %>
            </td>
            <td>
                <%
                    out.print(list.get(i).getiClass());
                %>
            </td>
            <td>
                <%
                    out.print(list.get(i).getNumber());
                %>
            </td>
        </tr>
        <%
            }
        %>

    </table>

</div>


<script type='text/javascript'
        src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
<script type='text/javascript'
        src='//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js' charset='utf-8'></script>
<script type='text/javascript'
        src='//g.alicdn.com/msui/sm/0.6.2/js/sm-extend.min.js' charset='utf-8'></script>

</body>
</html>