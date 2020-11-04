<%-- 
    Document   : index
    Created on : 31 oct. 2020, 18:20:31
    Author     : Grup 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <jsp:include page="./templates/base.jsp"/>
    <head>        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src='https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.js'></script>
        <link href='https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.css' rel='stylesheet'>
        <link rel="stylesheet" href="./css/index.css">        
        <script src="js/mapbox.js"></script>
    </head>
    <body>
        <%
            String listHairdressingsJSON = (String) request.getAttribute("listHairdressingsJSON");
        %>
        <jsp:include page="./templates/header.jsp"/>
        <main>
            <div id='map'></div>
        </main>       
        <jsp:include page="./templates/footer.jsp"/>
        <input id="listHairdressingsJSON" name="listHairdressingsJSON" type="hidden" value='<%=listHairdressingsJSON%>'/>
    </body>
</html>
