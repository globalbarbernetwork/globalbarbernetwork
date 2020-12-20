<%-- 
    Document   : manageHairdressing
    Created on : Dec 5, 2020, 8:25:34 PM
    Author     : IOC DAW
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="employees" scope="request" class="java.util.List<org.globalbarbernetwork.entities.Employee>"/>
<!DOCTYPE html>
<html>
    <head>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_head.jspf"%>
        <link rel="stylesheet" href="${contextPath}/css/manageHairdressing.css">
    </head>
    <body>
        <!-- Header -->
        <%@include file="/WEB-INF/jspf/header.jspf"%>

        <div id="container">
            <ul class="nav nav-tabs nav-justified md-tabs indigo" id="myTab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" id="employee-tab" data-toggle="tab" href="#employee" role="tab" aria-controls="employee" aria-selected="true">Treballadors</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="service-tab" data-toggle="tab" href="#service" role="tab" aria-controls="service" aria-selected="false">Serveis</a>
                </li>
            </ul>
            <div class="tab-content card pt-5" id="myTabContentJust">
                <div class="tab-pane fade show active" id="employee" role="tabpanel" aria-labelledby="employee-tab-just">
                    <%@include file="/manageEmployee.jsp"%>
                </div>
                <div class="tab-pane fade" id="service" role="tabpanel" aria-labelledby="service-tab-just">

                </div>
            </div>
        </div>

        <input id="contextPath" name="contextPath" type="hidden" value="${contextPath}"/>
                
        <!-- Footer -->
        <%@include file="/WEB-INF/jspf/footer.jspf"%>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_body.jspf"%>
        <script src="${contextPath}/js/manageEmployee.js"></script>
    </body>
</html>
