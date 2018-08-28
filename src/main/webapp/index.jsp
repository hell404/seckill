<%@ page import="java.util.Date" %>
<html>
<body>
<h2>Hello World!</h2>
<%
    request.setCharacterEncoding("utf-8");

    //String user = URLEncoder.encode(request.getParameter("user"),"utf-8");
    String date = new Date().toLocaleString();
    String user = request.getParameter("user");
    Cookie cookie = new Cookie("mrCookie","");
    //cookie.setValue(date.trim());
    cookie.setMaxAge(60*60*24*30);
    response.addCookie(cookie);


%>
</body>
</html>
