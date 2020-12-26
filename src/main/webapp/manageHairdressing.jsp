<%-- 
    Document   : manageHairdressing
    Created on : Dec 5, 2020, 8:25:34 PM
    Author     : IOC DAW
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="employees" scope="request" class="java.util.List<org.globalbarbernetwork.entities.Employee>"/>
<jsp:useBean id="services" scope="request" class="java.util.List<org.globalbarbernetwork.entities.Service>"/>
<jsp:useBean id="selectedTab" scope="request" class="java.lang.Boolean"/>

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
            <ul class="nav nav-tabs" id="myTab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link <%= selectedTab ? "active" : ""%>" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="${selectedTab}">Serveis</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <%= !selectedTab ? "active" : ""%>" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="${!selectedTab}">Treballadors</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="schedule-tab" data-toggle="tab" href="#schedule" role="tab" aria-controls="schedule" aria-selected="#">Horari</a>
                </li>
            </ul>
            <div class="tab-content" id="myTabContent">
                <div class="tab-pane fade <%= selectedTab ? "show active" : ""%>" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                    <%@include file="/manageService.jsp"%>
                </div>
                <div class="tab-pane fade <%= !selectedTab ? "show active" : ""%>" id="home" role="tabpanel" aria-labelledby="home-tab">
                    <%@include file="/manageEmployee.jsp"%>
                </div>
                <div class="tab-pane fade" id="schedule" role="tabpanel" aria-labelledby="schedule-tab">
                    <%@include file="/manageSchedule.jsp"%>
                </div>
            </div>
        </div>

        <input id="contextPath" name="contextPath" type="hidden" value="${contextPath}"/>

        <!-- Footer -->
        <%@include file="/WEB-INF/jspf/footer.jspf"%>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_body.jspf"%>

        <script src="${contextPath}/js/initializeDatatables.js"></script>
        <script src="${contextPath}/js/manageEmployee.js"></script>
        <script src="${contextPath}/js/manageService.js"></script>
    </body>
</html>
