<%-- 
    Document   : editClient
    Created on : 31 oct. 2020, 18:20:31
    Author     : Grup 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="client" scope="request" class="org.globalbarbernetwork.entities.Client"/>

<!DOCTYPE html>
<html>
    <head>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_head.jspf"%>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script src="${contextPath}/js/editClient.js"></script>                
        <link rel="stylesheet" href="${contextPath}/css/editProfile.css">
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <div class="container-fluid mt-5">
            <div class="row">     
                <div class="col-md-1"></div>
                <div class="col-md-3 mb-3">
                    <div class="container card bg-dark">
                        <h3 class="mb-5 mt-3 text-center"><%=client.getDisplayName() != null ? client.getDisplayName() : "Display Name"%></h3>
                        <div class="text-center">
                            <img src="${contextPath}/img/avatar.png" alt="Avatar" class="avatar avatar-hover">
                        </div>                        
                        <input type="file" hidden id="actual-btn" class="mt-5 mb-3 w-100"/>
                        <label id="file" for="actual-btn" class="text-center btn btn-success">Canviar imatge</label>                                                                   
                    </div>
                </div>
                <div class="col-md-7">
                    <div class="card bg-dark mb-3 pt-2">
                        <div class="card-header">
                            <h3 class="mb-3">Editar Perfil</h3> 
                            <button id="changePassword" class="btn btn-info float-right">Canviar contrasenya</button>
                        </div>
                        <div class="card-body">
                            <form action="${contextPath}/ManagementServlet/menuOption/editProfile/client" method="post">                                  
                                <div class="form-group row">
                                    <div class="col-md-6">
                                        <label for="labelEmail">Correu electronic</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-envelope"></i></div>
                                            </div>
                                            <input id="email" type="text" class="form-control" name="email" placeholder="Correu electronic" value='<%=client.getEmail() != null ? client.getEmail() : ""%>' readonly>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="labelDisplayName">Display name</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-text"></i></div>
                                            </div>                                        
                                            <input name="displayName" type="text" placeholder="Display name" class="form-control" value='<%=client.getDisplayName() != null ? client.getDisplayName() : ""%>'>                
                                        </div>                    
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <div class="col-md-6">
                                        <label for="labelName">Nom</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-text"></i></div>
                                            </div>                                                                                
                                            <input name="name" type="text" placeholder="Nom" class="form-control" value='<%=client.getName() != null ? client.getName() : ""%>'>                        
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="labelSurname">Cognom</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-text"></i></div>
                                            </div>                                                                                                                        
                                            <input name="surname" type="text" placeholder="Cognom" class="form-control" value='<%=client.getSurname() != null ? client.getSurname() : ""%>'>                                    
                                        </div>
                                    </div>
                                </div>                    
                                <div class="form-group row">
                                    <div class="col-md-6">
                                        <label for="labelPhoneNumber">Numero de telefon</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-phone-alt"><span class="ml-2">+34</span></i></div>                                                
                                            </div>                                                                                                                                                                                                            
                                            <input id="phone" name="phoneNumber" type="tel" placeholder="Numero de telefon" class="form-control" value="<%=client.getPhoneNumber() != null ? client.getPhoneNumber() : ""%>">
                                        </div>
                                    </div>       
                                </div>                    
                                <div class="form-group row pl-2 pr-2">
                                    <button class="btn btn-success mt-4 w-100">Actualitzar</button>
                                </div>   
                                <input id="type" name="type" type="hidden" value="${client.type}"/>                                
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-1"></div>
            </div>           
        </div>                                            

        <!-- Footer -->
        <%@include file="/WEB-INF/jspf/footer.jspf"%>
        <!-- Scripts comunes -->
        <%@include file="/WEB-INF/jspf/base_body.jspf"%>

        <script>
            <c:set var="edited" value="<%= request.getAttribute("edited")%>"/>
            <c:out value="${row.file_name}"/>
            <c:if test="${edited != null}">
            Swal.fire({
                icon: 'success',
                title: "S'han guardat els canvis",
                showConfirmButton: false,
                timer: 1500
            })
            </c:if>
        </script>

    </body>
</html>
