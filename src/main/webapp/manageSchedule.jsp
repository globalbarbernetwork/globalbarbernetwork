<%-- 
    Document   : manageSchedule
    Created on : Dec 6, 2020, 5:15:35 PM
    Author     : Grup 3
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="days" scope='request'>Dilluns, Dimarts, Dimecres, Dijous, Divendres, Disabte, Diumenge</c:set>

    <form method="POST" action="${contextPath}/ManagementServlet/menuOption/manageHairdressing/updateSchedule">
    <div class="container-fluid mt-5">
        <div class="card">
            <div class="card-header">
                <h4 style="display: inline-block">Asignar horaris</h4>
                <input class="btn btn-success w20 float-right" type="submit" value="Guardar">
            </div>
            <div class="card-body">        
                <div class="tab-content" id="myTabContent2">

                    <c:forEach var="day" items="${days}" varStatus="loop">                    
                        <h4>${day}</h4><hr>
                        <div class="form-group row">
                            <div class="col-6">
                                <div class="row">
                                    <div class="col-12">
                                        <label for="example-time-input" class="col-form-label font-weight-bold">Primer rang horari: </label>                                  
                                    </div>
                                    <div class="col-2">
                                        <label for="example-time-input" class="col-form-label">Horari obertura: </label>                                    
                                    </div>
                                    <div class="col-4">
                                        <input id="range1-start-day${loop.index}" type="time" class="form-control">
                                    </div>
                                    <div class="col-2">
                                        <label for="example-time-input" class="col-form-label">Horari tancament: </label>                                    
                                    </div>                                
                                    <div class="col-4">
                                        <input id="range1-end-day${loop.index}" type="time" class="form-control">
                                    </div>                                                        
                                </div>
                            </div>           
                            <div class="col-6">
                                <div class="row">
                                    <div class="col-12">
                                        <label for="example-time-input" class="col-form-label font-weight-bold">Segon rang horari: </label>                                  
                                    </div>
                                    <div class="col-2">
                                        <label for="example-time-input" class="col-form-label">Horari obertura: </label>                                    
                                    </div>
                                    <div class="col-4">
                                        <input id="range2-start-day${loop.index}" type="time" class="form-control">
                                    </div>
                                    <div class="col-2">
                                        <label for="example-time-input" class="col-form-label">Horari tancament: </label>                                    
                                    </div>                                
                                    <div class="col-4">
                                        <input id="range2-end-day${loop.index}" type="time" class="form-control">
                                    </div>                                
                                </div>
                            </div>
                        </div>   
                    </c:forEach> 
                </div>    
            </div>
        </div>
    </div>
</form>                     