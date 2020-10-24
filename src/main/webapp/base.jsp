<%-- 
    Document   : base
    Created on : Oct 17, 2020, 7:01:16 PM
    Author     : IOC DAW
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <link rel="stylesheet" href="./css/styles.css" >
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

        <footer class="page-footer font-small bg-dark">
            <div class="footer-copyright text-center py-3">
                © 2020 Copyright: Global Barber Network Corp.
            </div>
        </footer>

        <!-- Optional JavaScript -->
        <!-- jQuery first, then Popper.js, then Bootstrap JS -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    </body>

    <footer>
        <!-- Aquí irá el footer -->
    </footer>
</html>
