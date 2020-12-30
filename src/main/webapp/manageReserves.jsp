<%-- 
    Document   : manageReserves
    Created on : 29 Dec 2020, 22:48:27
    Author     : Grup 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="businessHoursJSON" scope="request" class="java.lang.String"/>
<jsp:useBean id="reservesEventsJSON" scope="request" class="java.lang.String"/>

<!DOCTYPE html>
<html>
    <head>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_head.jspf"%>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/fullcalendar@5.5.0/main.css">
        <link rel="stylesheet" href="${contextPath}/css/manageReserves.css">
    </head>
    <body>
        <!-- Header -->
        <%@include file="/WEB-INF/jspf/header.jspf"%>

        <!-- Manage reserves -->
        <div class="container-fluid pt-5 pb-5">
            <div class="card">
                <div class="card-header">
                    <h4 style="display: inline-block">Gestió reserves</h4>
                    <!--<input class="btn btn-success w20 float-right" type="submit" value="Gravar">-->
                </div>
                <div class="card-body">
                    <div id='fullcalendarReserves'></div>
                </div>
            </div>
        </div>
        
        <input type="hidden" id="businessHoursJSON" name="businessHoursJSON" value='<%=businessHoursJSON%>'>
        <input type="hidden" id="reservesEventsJSON" name="reservesEventsJSON" value='<%=reservesEventsJSON%>'>
        
        <!-- Footer -->
        <%@include file="/WEB-INF/jspf/footer.jspf"%>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_body.jspf"%>
        <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.5.0/main.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.5.0/locales-all.js"></script>
        <script src="${contextPath}/js/manageReserves.js"></script>
    </body>
</html>

