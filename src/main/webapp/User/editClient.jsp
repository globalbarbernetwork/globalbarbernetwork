<%-- 
    Document   : index
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
    </head>
    <style>
        .avatar {
            vertical-align: middle;
            width: 200px;
            height: 200px;
            border-radius: 50%;
        }
    </style>
    <body>
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <div class="container mt-5">
            <div class="row">
                <div class="col-4 mr-5 mb-5">
                    <div class="container card bg-dark" style="color: white;">
                        <h3 class="mb-5 mt-3 text-center"><%=client.getDisplayName()%></h3>
                        <div class="text-center">
                            <img src="${contextPath}/img/avatar.png" alt="Avatar" class="avatar">
                        </div>
                        <button type="button" class="btn btn-success mt-5 mb-3" style="width: 100%">Cambiar foto</button>
                    </div>
                </div>

                <div class="col">
                    <div class="card bg-dark mb-3" style="color: white;">
                        <div class="card-header" style="border-color: white;">
                            <h3 class="mb-3">Editar Perfil</h3>                    
                        </div>
                        <div class="card-body">
                            <form action="post">                                  
                                <div class="form-group">
                                    <label for="labelEmail">Correu electronic</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">@</div>
                                        </div>
                                        <input type="text" class="form-control" id="email" placeholder="Correu electronic" value="<%=client.getEmail()%>">
                                    </div>
                                </div>                                                              
                                <div class="form-group">
                                    <label for="labelDisplayName">Display name</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-text"></i></div>
                                        </div>                                        
                                        <input name="displayName" type="text" placeholder="Display name" class="form-control" value="<%=client.getDisplayName()%>">                
                                    </div>
                                </div>                    
                                <div class="form-group">
                                    <label for="labelName">Nom</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-text"></i></div>
                                        </div>                                                                                
                                        <input name="name" type="text" placeholder="Nom" class="form-control" value="<%=client.getName()%>">                        
                                    </div>
                                </div>                    
                                <div class="form-group">
                                    <label for="labelSurname">Cognom</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-text"></i></div>
                                        </div>                                                                                                                        
                                        <input name="surname" type="text" placeholder="Cognom" class="form-control" value="<%=client.getSurname()%>">                                    
                                    </div>
                                </div>                    
                                <div class="form-group">
                                    <label for="labelPhoneNumber">Numero de telefon</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-phone-alt"></i></div>
                                        </div>                                                                                                                                                                
                                        <input name="phoneNumber" type="text" placeholder="Numero de telefon" class="form-control" value="<%=client.getPhoneNumber()%>">
                                    </div>
                                </div>                    
                                <button type="button" class="btn btn-success mt-4" style="width: 100%">Actualitza</button>
                            </form>
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
