<%-- 
    Document   : registerHairdressing
    Created on : 31 oct. 2020, 19:27:46
    Author     : Grup 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
                <form id="form" style="margin:auto;" action="${contextPath}/ManagementServlet/access/registerHairdressing"  class="form-horizontal col-md-6" method="post">
                    <h2>Registre Empresa</h2>

                    <div class="form-row">
                        <div class="form-group col-6">
                            <input type="text" class="form-control" name="name" id="name" placeholder="Nom Empresa" title="Omple aquest camp" maxlength="30" required>      	
                            <small id="errorInput" class="text-danger"></small>
                        </div>
                        <div class="form-group col-6">
                            <input type="text" class="form-control" name="address" id="address" placeholder="Direcció" title="Omple aquest camp" required>      	
                            <small id="errorInput" class="text-danger"></small>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group col-6">
                            <input type="email" class="form-control" name="email" id="email" placeholder="Correu Electrònic" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$" title="Omple aquest camp" required>
                            <small id="errorInput" class="text-danger"></small>
                        </div>
                        <div class="form-group col-6">
                            <input type="text" class="form-control" name="country" id="country" placeholder="País" title="Omple aquest camp" required>      	
                            <small id="errorInput" class="text-danger"></small>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group col-6">
                            <input type="text" class="form-control" name="phoneNumber" pattern="^\d{9}$" id="phoneNumber" placeholder="Telèfon" title="Omple aquest camp" required>
                            <small id="errorInput" class="text-danger"></small>
                        </div>
                        <div class="form-group col-6">
                            <input type="text" class="form-control" name="province" id="province" placeholder="Província" title="Omple aquest camp" required>      	
                            <small id="errorInput" class="text-danger"></small>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group col-6">
                            <div class="input-group show_hide_password">
                                <input type="password" class="form-control" id="password" name="password" placeholder="Contrasenya" title="Omple aquest camp" minlength="6" required>
                                <div class="input-group-append">
                                    <span class="input-group-text" id="password" title="Visualitza la contrasenya">
                                        <a href="#" id="eye"><i class="fa fa-eye-slash" aria-hidden="true"></i></a>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-6">
                            <input type="text" class="form-control" name="city" id="city" placeholder="Ciutat" title="Omple aquest camp" required>      	
                            <small id="errorInput" class="text-danger"></small>
                        </div>
                    </div>


                    <div class="form-row">
                        <div class="form-group col-6">
                            <div class="input-group show_hide_password">
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Confirmar Contrasenya" title="Omple aquest camp" minlength="6" required>
                                <div class="input-group-append">
                                    <span class="input-group-text" id="confirmPassword">
                                        <a href="#" id="eye"><i class="fa fa-eye-slash" aria-hidden="true"></i></a>
                                    </span>
                                </div>
                            </div>
                            <small id="passwordHelp" class="text-danger" hidden></small>
                        </div>
                        <div class="form-group col-6">
                            <input type="text" class="form-control" name="zipCode" id="zipCode" placeholder="Codi Postal" title="Omple aquest camp" required>      	
                            <small id="errorInput" class="text-danger"></small>
                        </div>
                    </div>

                    <div class="form-row justify-content-center">
                        <div class="form-group col-lg-6">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="terms" title="Omple aquest camp" required>
                                <label class="form-check-label" for="terms">
                                    Acceptar <a href="#">termes del servei</a> i <a href="#">polí­tica de privacitat</a>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-row justify-content-center">
                        <div class="form-group col-lg-6">
                            <button type="submit" class="btn btn-success btn-lg btn-block fusubmit" name="btnRegister" id="btnRegister" disabled="">REGISTRE</button>
                        </div>
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
