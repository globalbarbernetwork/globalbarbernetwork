<%-- 
    Document   : header
    Created on : 04-nov-2020, 22:36:28
    Author     : Adrian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<c:set var="contextPath" scope="request" value="${pageContext.request.contextPath}"/>

<header>
    <nav class="navbar navbar-dark bg-dark">
        <a class="navbar-brand" href="${contextPath}/ManagementServlet">
            <img src="${contextPath}/img/barber-pole.svg" width="30" height="30" class="d-inline-block align-top" alt="logoWeb">
            Global Barber Network
        </a> 

        <form class="form-inline">                    
            <c:set var = "user" scope = "session" value="<%= session.getAttribute("user")%>"/>                                    
            <c:choose>
                <c:when test="${user}">
                    <img src="${contextPath}/img/user.svg" width="30" height="30" class="d-inline-block align-top" alt="iconUser">
                </c:when>                   
                <c:otherwise>
                    <a href="${contextPath}/login.jsp">
                        <button class="btn btn-sm btn-outline-secondary" type="button">Iniciar sessión</button>
                    </a>
                    <a href="${contextPath}/ManagementServlet/access/register">
                        <button class="btn btn-sm btn-outline-secondary" type="button">Registrar-se</button>
                    </a>                        
                    <a href="${contextPath}/ManagementServlet/access/registerHairdressing">
                        <button class="btn btn-sm btn-outline-secondary" type="button">Registrar-se com empresa</button>
                    </a>                        
                </c:otherwise>
            </c:choose>                
        </form>
    </nav>
</header>  