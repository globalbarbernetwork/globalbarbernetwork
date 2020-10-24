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
        <style>
            body { margin: 0; padding: 0; }
            #map { position: absolute; top: 50px; bottom: 50px; width: 100%; }
            .mapboxgl-popup {
                max-width: 400px;
                font: 12px/20px 'Helvetica Neue', Arial, Helvetica, sans-serif;
            }
        </style>
    </head>
    <body>              
        <main>
            <div id='map'></div>
        </main>       
    </body>
    <script src="js/index_mapbox.js"></script>
</html>
