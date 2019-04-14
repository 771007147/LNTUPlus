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

<h1>WeekNo</h1>
<form action="time/week" method="get">
    <input type="submit" style="width:80px;"/>
</form>

<h1>CET</h1>
<form action="cet/get" method="post">
    <input type="text" name="number" value="1706030227">
    <input type="text" name="password" value="211422199805033813">
    <input type="submit" style="width:80px;"/>
</form>

<h1>CET enroll</h1>
<form action="cet/enroll" method="post">
    <input type="text" name="number" value="1706030227">
    <input type="text" name="password" value="211422199805033813">
    <input type="submit" style="width:80px;"/>
</form>

<h1>CET post</h1>
<form action="cet/post" method="post">
    <input type="text" name="number" value="1706030227">
    <input type="text" name="password" value="211422199805033813">
    <input type="text" name="notifyid" value="31284129">
    <input type="text" name="id" value="31289162">
    <input type="text" name="rz" value="2879190">
    <input type="submit" style="width:80px;"/>
</form>

<h1>GetChooseTable</h1>
<form action="choose/get" method="post">
    <input type="text" name="number" value="1706030227">
    <input type="text" name="password" value="211422199805033813">
    <input type="submit" style="width:80px;"/>
</form>

<h1>ChooseTable</h1>
<form action="choose/choose" method="post">
    <input type="text" name="number" value="1706030227">
    <input type="text" name="password" value="211422199805033813">
    <input type="text" name="select" value="xx_xk_add.jsp?kkplan_xxid=90116">
    <input type="text" name="cNumber" value="H271700004036">
    <input type="submit" style="width:80px;"/>
</form>

<h1>Login</h1>
<form action="login/get" method="post">
    <input type="text" name="number" value="1606110204">
    <input type="text" name="password" value="211402199706042428">
    <input type="submit" style="width:80px;"/>
</form>

<h1>StuInfo</h1>
<form action="stuinfo/get" method="post">
    <input type="text" name="number" value="1606110204">
    <input type="text" name="password" value="211402199706042428">
    <input type="submit" style="width:80px;"/>
</form>

<h1>Score</h1>
<form action="score/get" method="post">
    <input type="text" name="number" value="1606110204">
    <input type="text" name="password" value="211402199706042428">
    <input type="submit" style="width:80px;"/>
</form>

<h1>Exam</h1>
<form action="exam/get" method="post">
    <input type="text" name="number" value="1606110204">
    <input type="text" name="password" value="211402199706042428">
    <input type="submit" style="width:80px;"/>
</form>

<h1>Table</h1>
<form action="table/get" method="post">
    <input type="text" name="number" value="1606110204">
    <input type="text" name="password" value="211402199706042428">
    <input type="submit" style="width:80px;"/>
</form>

<h1>Hello</h1>
<form action="hello/get" method="get">
    <input type="submit" style="width:80px;"/>
</form>

<h1>School Notice</h1>
<form action="notice/get" method="post">
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

<h1>Everyday</h1>
<form action="everyday/get" method="get">
    <input type="submit" style="width:80px;"/>
</form>

<%= request.getServletContext().getRealPath("/")%>
</body>
</html>
