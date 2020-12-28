<%-- 
    Document   : manageSchedule
    Created on : Dec 6, 2020, 5:15:35 PM
    Author     : Grup 3
--%>

<%@page import="java.util.HashMap"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="schedule" scope="request" class="java.util.HashMap"></jsp:useBean>
<jsp:useBean id="daysOfWeek" scope="request" class="java.util.HashMap"></jsp:useBean>

    <form method="POST" action="${contextPath}/ManagementServlet/menuOption/manageHairdressing/updateSchedule">
    <div class="container-fluid mt-5">
        <div class="card">
            <div class="card-header">
                <h4 style="display: inline-block">Assignar horaris</h4>
                <input class="btn btn-success w20 float-right" type="submit" value="Gravar">
            </div>
            <div class="card-body">        
                <div class="tab-content" id="myTabContent2">                      
                    <% for (int i = 1; i <= daysOfWeek.size(); i++) {
                            HashMap rangeHour1 = null;
                            HashMap rangeHour2 = null;
                            if (!schedule.isEmpty()) {
                                rangeHour1 = ((HashMap) ((HashMap) schedule.get(String.valueOf(i))).get("rangeHour1"));
                                rangeHour2 = ((HashMap) ((HashMap) schedule.get(String.valueOf(i))).get("rangeHour2"));
                            }
                    %>                    
                    <h4><%=daysOfWeek.get(String.valueOf(i))%></h4><hr>
                    <div class="form-group row">
                        <div class="col-6">
                            <div class="custom-control custom-checkbox my-1 mr-sm-2">
                                <input type="checkbox" class="custom-control-input" id="range1-check-<%=i%>">
                                <label class="custom-control-label" for="range1-check-<%=i%>">Deshabilitar rang horari</label>
                            </div>
                            <div class="row range1-row-<%=i%>">
                                <div class="col-12">
                                    <label for="example-time-input" class="col-form-label font-weight-bold">Primer rang horari: </label>                                           
                                </div>
                                <div class="col-2">
                                    <label for="example-time-input" class="col-form-label">Horari obertura: </label>                                    
                                </div>
                                <div class="col-4">
                                    <input id="range1-start-day<%=i%>" name="range1-start-day<%=i%>" type="time" class="form-control" value="<%= !schedule.isEmpty() ? rangeHour1.get("startHour") : ""%>">
                                </div>
                                <div class="col-2">
                                    <label for="example-time-input" class="col-form-label">Horari tancament: </label>                                    
                                </div>                                
                                <div class="col-4">
                                    <input id="range1-end-day<%=i%>" name="range1-end-day<%=i%>" type="time" class="form-control" value="<%= !schedule.isEmpty() ? rangeHour1.get("endHour") : ""%>">
                                </div>                                                        
                            </div>
                        </div>           
                        <div class="col-6">
                            <div class="custom-control custom-checkbox my-1 mr-sm-2">
                                <input type="checkbox" class="custom-control-input" id="range2-check-<%=i%>">
                                <label class="custom-control-label" for="range2-check-<%=i%>">Deshabilitar rang horari</label>
                            </div>
                            <div class="row range2-row-<%=i%>">
                                <div class="col-12">
                                    <label for="example-time-input" class="col-form-label font-weight-bold">Segon rang horari: </label>                                          
                                </div>
                                <div class="col-2">
                                    <label for="example-time-input" class="col-form-label">Horari obertura: </label>                                    
                                </div>
                                <div class="col-4">
                                    <input id="range2-start-day<%=i%>" name="range2-start-day<%=i%>" type="time" class="form-control" value="<%= !schedule.isEmpty() ? rangeHour2.get("startHour") : ""%>">
                                </div>
                                <div class="col-2">
                                    <label for="example-time-input" class="col-form-label">Horari tancament: </label>                                    
                                </div>                                
                                <div class="col-4">
                                    <input id="range2-end-day<%=i%>" name="range2-end-day<%=i%>" type="time" class="form-control" value="<%= !schedule.isEmpty() ? rangeHour2.get("endHour") : ""%>">
                                </div>                                
                            </div>
                        </div>
                    </div>   
                    <% }
                    %>
                </div>    
            </div>
        </div>
    </div>
</form> 