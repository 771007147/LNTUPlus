<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 2019/2/1
  Time: 下午10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>

<h1>Hello</h1>
<form action="hello/get" method="get">
    <input type="submit" style="width:80px;"/>
</form>

<h1>School Notice</h1>
<form action="schoolnotice/get" method="post">
    <input type="text" name="hashcode" value="629674271">
    <input type="submit" name="schoolnotice" style="width:80px;"/>
</form>

<h1>Sign</h1>
<form action="sign/sign" method="post">
    <input type="text" name="name" value="高瑞联">
    <input type="text" name="number" value="1506010403">
    <input type="text" name="iClass" value="计算15-4">
    <input type="submit" style="width:80px;"/>
</form>

<h1>ClassRoom</h1>
<form action="classroom/get" method="post">
    <input type="text" name="campus" value="0">
    <input type="text" name="buildingname" value="eyl">
    <input type="text" name="weeks" value="16">
    <input type="submit" style="width:80px;"/>
</form>

<h1>Article</h1>
<form action="article/get" method="get">
    <input type="submit" style="width:80px;"/>
</form>

<%= request.getServletContext().getRealPath("/")%>
</body>
</html>
