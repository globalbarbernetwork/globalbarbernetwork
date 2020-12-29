<%-- 
    Document   : manageHairdressing
    Created on : Dec 5, 2020, 8:25:34 PM
    Author     : IOC DAW
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="employees" scope="request" class="java.util.List<org.globalbarbernetwork.entities.Employee>"/>
<jsp:useBean id="services" scope="request" class="java.util.List<org.globalbarbernetwork.entities.Service>"/>
<jsp:useBean id="selectedTab" scope="request" class="java.util.HashMap"/>
<jsp:useBean id="incrementMin" scope="request" class="java.lang.Integer"/>

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
                    <a class="black-on-hover nav-link ${selectedTab.get("service") ? "active" : ""}" id="service-tab" data-toggle="tab" href="#service" role="tab" aria-controls="service" aria-selected="${selectedTab.get("service")}">Serveis</a>
                </li>
                <li class="nav-item">
                    <a class="black-on-hover nav-link ${selectedTab.get("schedule") ? "active" : ""}" id="schedule-tab" data-toggle="tab" href="#schedule" role="tab" aria-controls="schedule" aria-selected="${selectedTab.get("schedule")}">Horari</a>
                </li>
                <li class="nav-item">
                    <a class="black-on-hover nav-link ${selectedTab.get("holiday") ? "active" : ""}" id="holiday-tab" data-toggle="tab" href="#holiday" role="tab" aria-controls="holiday" aria-selected="${selectedTab.get("holiday")}">Festius</a>
                </li>
                <li class="nav-item">
                    <a class="black-on-hover nav-link ${selectedTab.get("employee") ? "active" : ""}" id="employee-tab" data-toggle="tab" href="#employee" role="tab" aria-controls="employee" aria-selected="${selectedTab.get("employee")}">Treballadors</a>
                </li>
            </ul>
            <div class="tab-content" id="myTabContent">
                <div class="tab-pane fade ${selectedTab.get("service") ? " show active" : ""}" id="service" role="tabpanel" aria-labelledby="service-tab">
                    <%@include file="/manageService.jsp"%>
                </div>
                <div class="tab-pane fade ${selectedTab.get("schedule") ? " show active" : ""}" id="schedule" role="tabpanel" aria-labelledby="schedule-tab">
                    <%@include file="/manageSchedule.jsp"%>
                </div>
                <div class="tab-pane fade ${selectedTab.get("holiday") ? " show active" : ""}" id="holiday" role="tabpanel" aria-labelledby="holiday-tab">
                    <%@include file="/manageHoliday.jsp"%>
                </div>
                <div class="tab-pane fade ${selectedTab.get("employee") ? " show active" : ""}" id="employee" role="tabpanel" aria-labelledby="employee-tab">
                    <%@include file="/manageEmployee.jsp"%>
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
        <script src="${contextPath}/js/manageSchedule.js"></script>
        <script src="${contextPath}/js/manageHolidays.js"></script>
    </body>
</html>
