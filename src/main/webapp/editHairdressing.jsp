<%-- 
    Document   : editClient
    Created on : 31 oct. 2020, 18:20:31
    Author     : Grup 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="hairdressing" scope="request" class="org.globalbarbernetwork.entities.Hairdressing"/>

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
                        <h3 class="mb-5 mt-3 text-center"><%=hairdressing.getDisplayName() != null ? hairdressing.getDisplayName() : "Display name"%></h3>
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
                            <form action="${contextPath}/ManagementServlet/menuOption/editProfile/hairdressing/editHairdressing.jsp" method="post">                                  
                                <div class="form-group row">
                                    <div class="col-md-6">
                                        <label for="labelEmail">Correu electronic</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-envelope"></i></div>
                                            </div>
                                            <input id="email" type="text" class="form-control" name="email" placeholder="Correu electronic" value='<%=hairdressing.getEmail() != null ? hairdressing.getEmail() : ""%>' readonly>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="labelSurname">Pais</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-map-marker-alt"></i></div>
                                            </div>                                                                                                                        
                                            <input name="country" type="text" placeholder="Pais" class="form-control" value='<%=hairdressing.getCountry() != null ? hairdressing.getCountry() : ""%>' disabled>                                    
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <div class="col-md-6">
                                        <label for="labelName">Nom de la companyia</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-text"></i></div>
                                            </div>                                                                                
                                            <input name="companyName" type="text" placeholder="Nom de la companyia" class="form-control" value="<%=hairdressing.getCompanyName()%>">                        
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="labelName">Ciutat</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-map-marker-alt"></i></div>
                                            </div>                                                                                
                                            <input name="city" type="text" placeholder="Ciutat" class="form-control" value='<%=hairdressing.getCity() != null ? hairdressing.getCity() : ""%>' disabled>                        
                                        </div>
                                    </div>
                                </div>                    
                                <div class="form-group row">
                                    <div class="col-md-6">
                                        <label for="labelDisplayName">Display name</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-text"></i></div>
                                            </div>                                        
                                            <input name="displayName" type="text" placeholder="Display name" class="form-control" value='<%=hairdressing.getDisplayName() != null ? hairdressing.getDisplayName() : ""%>'>                
                                        </div>                    
                                    </div>                                    
                                    <div class="col-md-6">
                                        <label for="labelName">Provincia</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-map-marker-alt"></i></div>
                                            </div>                                                                                
                                            <input name="province" type="text" placeholder="Provincia" class="form-control" value='<%=hairdressing.getProvince() != null ? hairdressing.getProvince() : ""%>' disabled>                        
                                        </div>
                                    </div>                                   
                                </div>                    
                                <div class="form-group row">
                                    <div class="col-md-6">
                                        <label for="labelSurname">Codi postal</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-map-marker-alt"></i></div>
                                            </div>                                                                                                                        
                                            <input name="zipCode" type="text" placeholder="Codi postal" class="form-control" value='<%= hairdressing.getZipCode() != null ? hairdressing.getZipCode() : ""%>'>                                    
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="labelSurname">Direcció</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-map-marker-alt"></i></i></div>
                                            </div>                                                                                                                        
                                            <input name="address" type="text" placeholder="Direcció" class="form-control" value='<%= hairdressing.getAddress() != null ? hairdressing.getAddress() : ""%>'>                                    
                                        </div>
                                    </div>                                   
                                </div>                    
                                <div class="form-group row">
                                    <div class="col-md-6">
                                        <label for="labelName">Web</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-text"></i></div>
                                            </div>                                                                                
                                            <input name="website" type="text" placeholder="Web" class="form-control" value='<%=hairdressing.getWebsite() != null ? hairdressing.getWebsite() : ""%>'>                        
                                        </div>
                                    </div> 
                                    <div class="col-md-6">
                                        <label for="labelSurname">Instagram</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fab fa-instagram" style="font-size: 21px !important;"></i></i></div>
                                            </div>                                                                                                                        
                                            <input name="instagram" type="text" placeholder="Instagram" class="form-control" value='<%=hairdressing.getInstagram() != null ? hairdressing.getInstagram() : ""%>'>                                    
                                        </div>
                                    </div>
                                </div>                    
                                <div class="form-group row">
                                    <div class="col-md-6">
                                        <label for="labelName">Descripció</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-text"></i></div>
                                            </div>                                                                                
                                            <input name="description" type="text" placeholder="Descripció" class="form-control" value='<%=hairdressing.getDescription() != null ? hairdressing.getDescription() : ""%>'>                        
                                        </div>
                                    </div>       
                                    <div class="col-md-6">
                                        <label for="labelPhoneNumber">Numero de telefon</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-phone-alt"><span class="ml-2">+34</span></i></div>                                                
                                            </div>                                                                                                                                                                                                            
                                            <input id="phone" name="phoneNumber" type="tel" placeholder="Numero de telefon" class="form-control" value='<%=hairdressing.getPhoneNumber() != null ? hairdressing.getPhoneNumber() : ""%>'>
                                        </div>
                                    </div>       
                                </div>                    
                                <div class="form-group row pl-3 pr-3">
                                    <button class="btn btn-success mt-4 w-100">Actualitzar</button>
                                </div>   
                                <input id="type" name="type" type="hidden" value="${hairdressing.type}"/>
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
            <%--<c:if test="${request.getAttribute('edited')}">--%>
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
            <%--</c:if>--%>
        </script>
    </body>
</html>
