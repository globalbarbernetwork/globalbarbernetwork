<%-- 
    Document   : index
    Created on : 31 oct. 2020, 18:20:31
    Author     : Grup 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="listHairdressingsJSON" scope="request" class="java.lang.String"/>

<!DOCTYPE html>
<html>
    <head>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_head.jspf"%>
        <link href='https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.css' rel='stylesheet'>
        <link rel="stylesheet" href="${contextPath}/css/index.css">
    </head>
    <body>
        <!-- Header -->
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        
        <!-- Mapbox -->
        <main>
            <div id='map'></div>
        </main>
        
        <!-- Modal -->
        <div class="modal fade" id="modalReserve" tabindex="-1" role="dialog" aria-labelledby="modalReserveTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalReserveLongTitle">Realitzar reserva</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <c:choose>
                            <c:when test="${user.type == 'client'}">
                                <div id="formModalReserve" class="form-group mb-4">
                                    <div class="custom-control custom-checkbox">
                                        <input type="checkbox" class="custom-control-input" id="chooseHairdresser" onchange="showOrHideHairdressers(this.checked);">
                                        <label class="custom-control-label" for="chooseHairdresser">Vull escollir al meu perruquer de confiança</label>
                                    </div>
                                    <select id="hairdressers" class="browser-default custom-select" style="display:none;">
                                        <option value="-1" selected>Escull un/a perruquer/a</option>
                                    </select>
                                    <select id="services" class="browser-default custom-select">
                                        <option value="-1" selected>Escull un servei</option>
                                    </select>
                                    <div class="datepicker date input-group shadow-sm">
                                        <input type="text" placeholder="Data de la reserva" class="form-control" id="reservationDate">
                                        <div class="input-group-append"><span class="input-group-text"><i class="fas fa-calendar-day"></i></span></div>
                                        <select id="availableHours" class="browser-default custom-select">
                                            <option value="0" selected>Escull una hora</option>
                                            <option value="1">11:00H</option>
                                            <option value="2">13:00H</option>
                                            <option value="3">17:30H</option>
                                        </select>
                                    </div>
                                    <input id="selectedIdHairdressing" name="selectedIdHairdressing" type="hidden"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div id="errorModalReserve">
                                    <p>Necessites iniciar sessió per poder realitzar una reserva.</p>
                                </div>                                
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="modal-footer">
                        <c:choose>
                            <c:when test="${user.type == 'client'}">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel·lar</button>
                                <button type="button" class="btn btn-primary">Reservar</button>
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="btn btn-primary" data-dismiss="modal">Acceptar</button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        
        <input id="contextPath" name="contextPath" type="hidden" value="${contextPath}"/>
        <input id="listHairdressingsJSON" name="listHairdressingsJSON" type="hidden" value='<%=listHairdressingsJSON%>'/>
        <input id="userType" name="userType" type="hidden" value='${user.type}'/>
        
        <!-- Footer -->
        <%@include file="/WEB-INF/jspf/footer.jspf"%>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_body.jspf"%>
        <script src='https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.js'></script>
        <script src="${contextPath}/js/mapbox.js"></script>
        <script src="${contextPath}/js/index.js"></script>
    </body>
</html>
