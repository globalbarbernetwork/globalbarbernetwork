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
    </head>
    <body>
        <!-- Header -->
        <%@include file="/WEB-INF/jspf/header.jspf"%>

        <!-- Login -->
        <div class="col-4">
            <div class="card">
                <div class="card-header">
                    <h5 class="card-title">Inicia sessio</h5>
                </div>
                <div class="card-body">
                    <form method="post" action="${contextPath}/ManagementServlet/access/login">
                        <div class="form-group">
                            <input type='email' required class='form-control' name="email" id='email' placeholder='Correu electronic'/>
                        </div>
                        <div>
                            <input type='password' required class='form-control' name="password" id='password' placeholder='Contrasenya'/>
                        </div>
                        <div class="form-group">
                            <button type='submit' class='btn btn-primary'>Inicia</button>
                        </div>
                        <input type="hidden" id='errors_field' value="${errors}"/>
                    </form>                     
                </div>
                <div class="card-footer text-muted">
                    ${errors}
                </div>
            </div>
        </div>

        <!-- Footer -->
        <%@include file="/WEB-INF/jspf/footer.jspf"%>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_body.jspf"%>
    </body>
</html>
