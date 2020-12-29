<%-- 
    Document   : index
    Created on : 31 oct. 2020, 18:20:31
    Author     : Grup 3
--%>

<%@page import="java.util.Objects"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="listHairdressingsJSON" scope="request" class="java.lang.String"/>
<jsp:useBean id="reserves" scope="request" class="java.util.HashMap<java.lang.String, java.util.ArrayList>"/>

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

        <div class="container-fluid p-5">
            <div class="row">
                <div class="card col p-0">
                    <div class="card-header">
                        <h4>Historial de reserves</h4>                        
                    </div>
                    <div class="card-body p-5">
                        <div class="row">
                            <div class="col-12">
                                <div class="card">
                                    <div class="card-header">
                                        <h5 class="mb-0"><a data-toggle="collapse" href="#multiCollapseExample1" role="button" aria-expanded="true" aria-controls="multiCollapseExample1">Proxims esdeveniments</a></h5>
                                    </div>                                    
                                    <div class="collapse multi-collapse show" id="multiCollapseExample1">                                        
                                        <div class="card-body">
                                            <c:forEach items="<%= reserves.get("pendingReserves") %>">                                                                                            
                                                <div class="card" style="width: 18rem;">
                                                    <img class="card-img-top" src="${contextPath}/img/test_sm.svg" alt="Card image cap">
                                                    <div class="card-body">
                                                        <h5 class="card-title">Card title</h5>
                                                        <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                                                        <a href="#" class="btn btn-primary">Go somewhere</a>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                            <% if (reserves.get("pendingReserves").isEmpty()) { %>                                                                                        
                                            <div>
                                                <span>No se ha hecho ninguna reserva</span>
                                            </div>
                                            <% }%>
                                        </div>
                                    </div>
                                </div>      
                            </div>
                            <div class="col-12 mt-2">
                                <div class="card">
                                    <div class="card-header">
                                        <h5 class="mb-0"><a data-toggle="collapse" data-target="#multiCollapseExample2" role="button" aria-expanded="false" aria-controls="multiCollapseExample2" style="color: #007bff">Esdeveniments finalitzats</a></h5>
                                    </div>           
                                    <div class="collapse multi-collapse" id="multiCollapseExample2">
                                        <div class="card-body">
                                            <div class="card" style="width: 18rem;">
                                                <img class="card-img-top" src="${contextPath}/img/test_sm.svg" alt="Card image cap">
                                                <div class="card-body">
                                                    <h5 class="card-title">Card title</h5>
                                                    <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                                                    <a href="#" class="btn btn-primary">Go somewhere</a>
                                                </div>
                                            </div>
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
