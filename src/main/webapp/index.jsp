<%-- 
    Document   : index
    Created on : 17 oct. 2020, 13:49:30
    Author     : DAW IOC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cat.xtec.ioc.entity.Hairdressing"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="./base.jsp"/>
        <script src="js/index.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src='https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.js'></script>
        <link href='https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.css' rel='stylesheet' />
        <title>Global Barber Network</title>  
        
    </head>
    <body>
        <div id='map' style='width: 400px; height: 300px;'></div>
        
    <ul>
        <li><a href="Login">Iniciar sessión</a></li>
        <li><a href="Register">Registrar</a></li>
    </ul>                
        
    </body>
    <script src="js/index_mapbox.js"></script>
</html>
