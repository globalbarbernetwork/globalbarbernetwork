<%-- 
    Document   : login
    Created on : 31 oct. 2020, 19:26:00
    Author     : Grup 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_head.jspf"%>
        <link rel="stylesheet" href="${contextPath}/css/login.css">
    </head>
    <body>
        <!-- Header -->
        <%@include file="/WEB-INF/jspf/header.jspf"%>

        <!-- Login -->
        <div class="container-fluid mt-5 ">
            <div class="col-md-4 card-login">
                <div class="card text-center">
                    <div class="card-header">
                        <h4 class="card-title">Inicia sessió</h4>
                    </div>
                    <div class="card-body p-5" >
                        <form method="post" action="${contextPath}/ManagementServlet/access/login">
                            <div class="form-group">
                                <input type='email' required class='form-control' name="email" id='email' placeholder='Correu electronic'/>
                            </div>
                            <div>
                                <input type='password' required class='form-control' name="password" id='password' placeholder='Contrasenya'/>
                            </div>
                            <div class="form-group mt-3">
                                <button type='submit' class='btn btn-primary w-100'>Inicia</button>
                            </div>
                            <input type="hidden" id='errors_field' class="" value='${errors}'/>
                        </form>                     
                    </div>
                    <div class="card-footer text-muted">                        
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <%@include file="/WEB-INF/jspf/footer.jspf"%>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_body.jspf"%>
        <script src="${contextPath}/js/login.js"></script>

    </body>
</html>
