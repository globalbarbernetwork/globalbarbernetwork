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
        <header>
            <nav class="navbar navbar-dark bg-dark">
                <a class="navbar-brand" href="#">
                    <img src="img/barber-pole.svg" width="30" height="30" class="d-inline-block align-top" alt="iconWeb">
                    Global Barber Network
                </a>
                <form class="form-inline">
                    <a href="Login">
                        <button class="btn btn-sm btn-outline-secondary" type="button">Iniciar sessión</button>
                    </a>
                    &nbsp;
                    <a href="Register">
                        <button class="btn btn-sm btn-outline-secondary" type="button">Registrar-se</button>
                    </a>
                </form>
            </nav>
        </header>

        
        <main>
            <div id='map'></div>
        </main>
        

        
        <footer class="page-footer font-small bg-dark">
            <div class="footer-copyright text-center py-3">
                © 2020 Copyright: Global Barber Network Corp.
            </div>
        </footer>
    </body>
    <script src="js/index_mapbox.js"></script>
</html>
