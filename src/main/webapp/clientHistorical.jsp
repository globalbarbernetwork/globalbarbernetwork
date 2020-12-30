<%-- 
    Document   : index
    Created on : 31 oct. 2020, 18:20:31
    Author     : Grup 3
--%>

<%@page import="org.globalbarbernetwork.entities.Employee"%>
<%@page import="java.util.Date"%>
<%@page import="org.globalbarbernetwork.entities.Service"%>
<%@page import="org.globalbarbernetwork.entities.Reserve"%>
<%@page import="org.globalbarbernetwork.entities.Hairdressing"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Objects"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<jsp:useBean id="listHairdressingsJSON" scope="request" class="java.lang.String"/>
<jsp:useBean id="historical" scope="request" class="java.util.HashMap"/>

<!DOCTYPE html>
<html>
    <head>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_head.jspf"%>
        <link href='https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.css' rel='stylesheet'>
        <link rel="stylesheet" href="${contextPath}/css/historicalClient.css">
    </head>
    <body>
        <!-- Header -->
        <%@include file="/WEB-INF/jspf/header.jspf"%>

        <div class="container-fluid p-5">
            <div class="row">
                <div class="card col p-0">
                    <div class="card-header historic-header">
                        <h4>Historial de reserves</h4>                        
                    </div>
                    <div class="card-body p-5">
                        <div class="row">
                            <div class="col-12">
                                <div class="card">
                                    <div class="card-header header-proxims-esdeveniments">
                                        <h5 class="mb-0"><a data-toggle="collapse" href="#multiCollapseExample1" role="button" aria-expanded="true" aria-controls="multiCollapseExample1">Pròxims esdeveniments</a></h5>
                                    </div>                                    
                                    <div class="collapse multi-collapse show" id="multiCollapseExample1">                                        
                                        <div class="card-body <%= (((ArrayList) historical.get("pendingReserves")).isEmpty() ? "" : "overflow-auto") %>">
                                            <% Integer loop = 0;%>
                                            <c:forEach var="tmpReserve" items="<%= historical.get("pendingReserves")%>" varStatus="loop">                                                  
                                                <c:set var="hairdressing" value="<%= ((Hairdressing) ((HashMap) ((ArrayList) historical.get("pendingReserves")).get(loop)).get("hairdressing")).getDisplayName()%>"></c:set>
                                                <c:set var="service" value="<%= ((Service) ((HashMap) ((ArrayList) historical.get("pendingReserves")).get(loop)).get("service")).getName()%>"></c:set>                                             
                                                <c:set var="employee" value="<%= ((Employee) ((HashMap) ((ArrayList) historical.get("pendingReserves")).get(loop)).get("employee"))%>"></c:set>
                                                <c:set var="servicePrice" value="<%= ((Service) ((HashMap) ((ArrayList) historical.get("pendingReserves")).get(loop)).get("service")).obtainPriceFormatted()%>"></c:set>
                                                <c:set var="timeFinal" value="<%= ((Reserve) ((HashMap) ((ArrayList) historical.get("pendingReserves")).get(loop)).get("reserve")).obtainTimeFinalLocalDate()%>"></c:set>
                                                <c:set var="timeInit" value="<%= ((Reserve) ((HashMap) ((ArrayList) historical.get("pendingReserves")).get(loop)).get("reserve")).obtainTimeInitLocalDate()%>"></c:set>
                                                <c:set var="reserve" value="<%= ((Reserve) ((HashMap) ((ArrayList) historical.get("pendingReserves")).get(loop)).get("reserve"))%>"></c:set>
                                                <javatime:format value="${timeInit}" pattern="HH:mm" var="iniParseDate"/>
                                                <javatime:format value="${timeFinal}" pattern="HH:mm" var="finalParseDate"/>
                                                <c:choose>
                                                    <c:when test="${reserve != null && reserve.state == 'P'}">
                                                        <div class="card d-inline-block ml-2 card-border-pending mb-5" style="width: 18rem;">
                                                        </c:when>                                                         
                                                        <c:when test="${reserve != null && reserve.state == 'A'}">
                                                            <div class="card d-inline-block ml-2 card-border-anuled mb-5" style="width: 18rem;">
                                                            </c:when>                                                         
                                                        </c:choose>                                                                  
                                                        <img class="card-img-top" src="${contextPath}/img/global_logo.png" alt="Card image cap">                                                                                  
                                                        <div class="card-body">
                                                            <h5 class="card-title">${hairdressing}</h5>
                                                            <p class="card-text">${service} - <span class='font-weight-bold'>${servicePrice}&euro;</span> </p>
                                                            <p class="card-text">${employee.name} ${employee.surname}</p>
                                                            <span class="card-text float-right">${timeInit.dayOfMonth}&sol;${timeInit.monthValue}&sol;${timeInit.year}</span>
                                                            <span class="card-text">${iniParseDate} - ${finalParseDate}</span>
                                                        </div>             
                                                        <c:choose>
                                                            <c:when test="${reserve != null && reserve.state == 'P'}">
                                                                <div class="card-footer text-center pending-status">
                                                                    <span>Pendent</span>
                                                                </div>
                                                            </c:when>                                                         
                                                            <c:when test="${reserve != null && reserve.state == 'A'}">
                                                                <div class="card-footer text-center anuled-status">
                                                                    <span>Anulat</span>
                                                                </div>
                                                            </c:when>                                                         
                                                        </c:choose>                                                               
                                                    </div>
                                                    <% loop++; %>
                                                </c:forEach>
                                                <%  if (((ArrayList) historical.get("pendingReserves")).isEmpty()) { %>                                               
                                                <div>
                                                    <span>Encara no hi ha cap esdeveniment pendent</span>
                                                </div>
                                                <% }%>
                                            </div>
                                        </div>
                                    </div>      
                                </div>
                                <div class="col-12 mt-2">
                                    <div class="card">
                                        <div class="card-header header-finalitzats-esdeveniments">
                                            <h5 class="mb-0"><a data-toggle="collapse" data-target="#multiCollapseExample2" role="button" aria-expanded="false" aria-controls="multiCollapseExample2" style="color: #007bff">Esdeveniments anteriors</a></h5>
                                        </div>           
                                        <div class="collapse multi-collapse" id="multiCollapseExample2">
                                            <div class='card-body <%= (((ArrayList) historical.get("completedReserves")).isEmpty() ? "" : "overflow-auto") %>'>
                                                <% Integer loop2 = 0;%>
                                                <c:forEach var="tmpReserve" items="<%= historical.get("completedReserves")%>" varStatus="loop">      
                                                    <c:set var="hairdressing" value="<%= ((Hairdressing) ((HashMap) ((ArrayList) historical.get("completedReserves")).get(loop2)).get("hairdressing")).getDisplayName()%>"></c:set>
                                                    <c:set var="service" value="<%= ((Service) ((HashMap) ((ArrayList) historical.get("completedReserves")).get(loop2)).get("service")).getName()%>"></c:set>                                                
                                                    <c:set var="employee" value="<%= ((Employee) ((HashMap) ((ArrayList) historical.get("completedReserves")).get(loop2)).get("employee"))%>"></c:set>        
                                                    <c:set var="servicePrice" value="<%= ((Service) ((HashMap) ((ArrayList) historical.get("completedReserves")).get(loop2)).get("service")).obtainPriceFormatted()%>"></c:set>                                                
                                                    <c:set var="timeFinal" value="<%= ((Reserve) ((HashMap) ((ArrayList) historical.get("completedReserves")).get(loop2)).get("reserve")).obtainTimeFinalLocalDate()%>"></c:set>  
                                                    <c:set var="timeInit" value="<%= ((Reserve) ((HashMap) ((ArrayList) historical.get("completedReserves")).get(loop2)).get("reserve")).obtainTimeInitLocalDate()%>"></c:set>
                                                    <c:set var="reserve" value="<%= ((Reserve) ((HashMap) ((ArrayList) historical.get("completedReserves")).get(loop2)).get("reserve"))%>"></c:set>
                                                    <javatime:format value="${timeInit}" pattern="HH:mm" var="iniParseDate2"/>
                                                    <javatime:format value="${timeFinal}" pattern="HH:mm" var="finalParseDate2"/>
                                                    <c:choose>
                                                        <c:when test="${reserve != null && reserve.state == 'C'}">
                                                            <div class="card d-inline-block ml-2 card-border-closed" style="width: 18rem;">
                                                            </c:when>                                                         
                                                            <c:when test="${reserve != null && reserve.state == 'A'}">
                                                                <div class="card d-inline-block ml-2 card-border-anuled" style="width: 18rem;">
                                                                </c:when>                                                         
                                                            </c:choose>
                                                            <img class="card-img-top" src="${contextPath}/img/global_logo.png" alt="Card image cap">
                                                            <div class="card-body">
                                                                <h5 class="card-title">${hairdressing}</h5>
                                                                <p class="card-text">${service} - <span class='font-weight-bold'>${servicePrice}&euro;</span></p>                                                        
                                                                <p class="card-text">${employee.name} ${employee.surname}</p>
                                                                <span class="card-text float-right">${timeInit.dayOfMonth}&sol;${timeInit.monthValue}&sol;${timeInit.year}</span>
                                                                <span class="card-text">${iniParseDate2} - ${finalParseDate2}</span>
                                                            </div>
                                                            <c:choose>
                                                                <c:when test="${reserve != null && reserve.state == 'C'}">
                                                                    <div class="card-footer text-center closed-status">
                                                                        <span>Completat</span>
                                                                    </div>
                                                                </c:when>                                                         
                                                                <c:when test="${reserve != null && reserve.state == 'A'}">
                                                                    <div class="card-footer text-center anuled-status">
                                                                        <span>Anulat</span>
                                                                    </div>
                                                                </c:when>                                                         
                                                            </c:choose>          
                                                        </div>
                                                        <% loop2++; %>
                                                    </c:forEach>
                                                    <%  if (((ArrayList) historical.get("completedReserves")).isEmpty()) { %>                                               
                                                    <div>
                                                        <span>Encara no hi ha cap esdeveniment finalitzat</span>
                                                    </div>
                                                    <% }%>
                                                </div>
                                            </div>      
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Footer -->
                <%@include file="/WEB-INF/jspf/footer.jspf"%>
                <!-- Scripts comunes -->
                <%@include file="/WEB-INF/jspf/base_body.jspf"%>
                </body>
                </html>
