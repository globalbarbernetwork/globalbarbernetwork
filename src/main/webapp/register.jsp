<%-- 
    Document   : register
    Created on : 31 oct. 2020, 19:26:53
    Author     : Grup 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="msgErrorEmail" scope="request" class="java.lang.String"/>
<jsp:useBean id="client" scope="request" class="org.globalbarbernetwork.entities.Client"/>

<!DOCTYPE html>
<html>
    <head>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_head.jspf"%>
        <script src="${contextPath}/js/register.js"></script>
        <link rel="stylesheet" href="${contextPath}/css/register.css">
    </head>
    <body>
        <!-- Header -->
        <%@include file="/WEB-INF/jspf/header.jspf"%>

        <div id="container">
            <div class="signup-form center">
                <form id="form" style="margin:auto;" action="${contextPath}/ManagementServlet/access/register"  class="form-horizontal col-lg-4 col-md-4 col-sm-4 col-xs-12" method="post">
                    <h2>Registre</h2>
                    <div class="form-group">
                        <input type="text" class="form-control" name="name" id="name" placeholder="Nom" title="Omple aquest camp" maxlength="30" value="${client.name}" required>      	
                        <small id="errorInput" class="text-danger"></small>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" name="surname" id="surname" placeholder="Cognom" title="Omple aquest camp" maxlength="40" value="${client.surname}" required>
                        <small id="errorInput" class="text-danger"></small>
                    </div>
                    <div class="form-group">
                        <input type="email" class="form-control <%= !msgErrorEmail.isEmpty() ? "is-invalid" : " " %>" name="email" id="email" placeholder="Correu Electrónic" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$" title="Omple aquest camp" value="${client.email}" required>
                        <small id="errorInput" class="text-danger">${ msgErrorEmail }</small>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" name="phoneNumber" pattern="^\d{9}$" id="phoneNumber" placeholder="Telèfon Móbil" title="Omple aquest camp" value="${client.phoneNumber}" required>
                        <small id="errorInput" class="text-danger"></small>
                    </div>
                    <div class="form-group">
                        <div class="input-group mb-3 show_hide_password">
                            <input type="password" class="form-control" id="password" name="password" placeholder="Contrasenya" title="Omple aquest camp" minlength="6" required>
                            <div class="input-group-append">
                                <span class="input-group-text" id="password" title="Visualitza la contrasenya">
                                    <i class="fa fa-eye-slash" aria-hidden="true"></i>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group mb-3 show_hide_password">
                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Confirmar Contrasenya" title="Omple aquest camp" minlength="6" required>
                            <div class="input-group-append">
                                <span class="input-group-text" id="confirmPassword">
                                    <i class="fa fa-eye-slash" aria-hidden="true"></i>
                                </span>
                            </div>
                        </div>
                        <small id="passwordHelp" class="text-danger" hidden>Les contrasenyes no coincideixen</small>
                    </div>
                    <div class="form-group">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="terms" title="Omple aquest camp" required>
                            <label class="form-check-label" for="terms">
                                Acceptar <a href="#">termes del servei</a> i <a href="#">política de privacitat</a>
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-success btn-lg btn-block fusubmit" name="btnRegister" id="btnRegister" disabled="">REGISTRE</button>
                    </div>
                    <div class="text-center">Ja tens un compte ? <a href="login.jsp">Iniciar Sessió</a></div>
                </form>
            </div>
        </div>
        
        <!-- Footer -->
        <%@include file="/WEB-INF/jspf/footer.jspf"%>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_body.jspf"%>
    </body>
</html>
