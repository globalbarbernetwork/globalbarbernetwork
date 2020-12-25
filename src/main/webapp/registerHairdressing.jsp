<%-- 
    Document   : registerHairdressing
    Created on : 31 oct. 2020, 19:27:46
    Author     : Grup 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="msgErrorEmail" scope="request" class="java.lang.String"/>
<jsp:useBean id="hairdrsg" scope="request" class="org.globalbarbernetwork.entities.Hairdressing"/>

<!DOCTYPE html>
<html>
    <head>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_head.jspf"%>
        <link rel="stylesheet" href="${contextPath}/css/register.css">
    </head>
    <body>
        <!-- Header -->
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <div class="container-fluid mt-5">
            <div class="card-login">
                <div class="card">
                    <div class="card-header">
                        <h4 class="card-title">Registre Empresa</h4>
                    </div>
                    <div class="card-body p-5" >
                        <div id="container">
                            <div class="signup-form center">
                                <form id="form" style="margin:auto;" action="${contextPath}/ManagementServlet/access/registerHairdressing"  class="form-horizontal" method="post">
                                    <div class="form-row">
                                        <div class="form-group col-6">
                                            <input type="text" class="form-control" name="name" id="name" placeholder="Nom Empresa" title="Omple aquest camp" maxlength="30" value="${hairdrsg.companyName}" required>
                                            <small id="errorInput" class="text-danger"></small>
                                        </div>
                                        <div class="form-group col-6">
                                            <input type="text" class="form-control" name="address" id="address" placeholder="Direcció" title="Omple aquest camp" maxlength="70" value="${hairdrsg.address}" required>
                                            <small id="errorInput" class="text-danger"></small>
                                        </div>
                                    </div>

                                    <div class="form-row">
                                        <div class="form-group col-6">
                                            <input type="email" class="form-control" name="email" id="email" placeholder="Correu Electrònic" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$" title="Omple aquest camp" value="${hairdrsg.email}" required>
                                            <small id="errorInput" class="text-danger">${ msgErrorEmail }</small>
                                        </div>
                                        <div class="form-group col-6">
                                            <input type="text" class="form-control" name="zipCode" id="zipCode" placeholder="Introdueix el codi postal de la teva empresa" title="Omple aquest camp" minlength="5" maxlength="5" value="${hairdrsg.zipCode}" required>
                                            <small id="errorInput" class="text-danger"></small>
                                        </div>
                                    </div>

                                    <div class="form-row">
                                        <div class="form-group col-6">
                                            <input type="text" class="form-control" name="phoneNumber" pattern="^\d{9}$" id="phoneNumber" placeholder="Telèfon" title="Omple aquest camp" value="${hairdrsg.phoneNumber}" required>
                                            <small id="errorInput" class="text-danger"></small>
                                        </div>
                                        <div class="form-group col-6">
                                            <input type="text" class="form-control" name="city" id="city" placeholder="Ciutat" value="${hairdrsg.city}" disabled>
                                            <small id="errorInput" class="text-danger"></small>
                                            <input type="hidden" id="cityHidden" name="cityHidden" value="${hairdrsg.city}"/>
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
                                            <input type="text" class="form-control" name="province" id="province" placeholder="Província" value="${hairdrsg.province}" disabled>
                                            <small id="errorInput" class="text-danger"></small>
                                            <input type="hidden" id="provinceHidden" name="provinceHidden" value="${hairdrsg.province}"/>
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
                                            <input type="text" class="form-control" name="country" id="country" placeholder="País" value="${hairdrsg.country}" disabled>
                                            <small id="errorInput" class="text-danger"></small>
                                            <input type="hidden" id="countryHidden" name="countryHidden" value="${hairdrsg.country}"/>
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
                                            <button type="submit" class="btn btn-success btn-lg btn-block fusubmit" name="btnRegister" id="btnRegister" data-toggle="modal" data-target="#modalConfLocaliz" disabled="">REGISTRE</button>
                                        </div>
                                    </div>

                                    <div class="text-center">Ja tens un compte ? <a href="login.jsp">Iniciar Sessió</a></div>

                                    <div class="modal fade" id="modalConfLocaliz" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="exampleModalLongTitle"><h3>Confirmar Localització</h3></h5>
                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                        <span aria-hidden="true">&times;</span>
                                                    </button>
                                                </div>
                                                <div class="modal-body">  
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                                                    <button type="button" class="btn btn-primary" id="btnConfmodal">Confirmar</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <input type="hidden" id="coordHairdressing" name="coordHairdressing"/>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer text-muted">
                        ${errors}
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <%@include file="/WEB-INF/jspf/footer.jspf"%>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_body.jspf"%>
        <script src="${contextPath}/js/register.js"></script>
        <script src="${contextPath}/js/registerHairdressing.js"></script>
    </body>
</html>
