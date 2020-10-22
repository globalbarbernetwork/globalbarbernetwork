<%-- 
    Document   : login
    Created on : 22-oct-2020, 22:37:47
    Author     : Adrian
--%>
<%@include file="./base.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="./css/styles.css" >
    </head>
    <body>        

        <div class="card">
            <div class="card-header">
                <%= request.getAttribute("title")%>
            </div>
            <div class="card-body">
                <%= request.getAttribute("form")%>
            </div>
        </div>

    </div>
</body>
</html>
