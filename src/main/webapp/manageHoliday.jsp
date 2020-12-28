<%-- 
    Document   : manageSchedule
    Created on : Dec 6, 2020, 5:15:35 PM
    Author     : Grup 3
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form method="POST" action="${contextPath}/ManagementServlet/menuOption/manageHairdressing/updateHolidays">
    <input type="hidden" name="holidays" id="holidays" value='<%= request.getAttribute("holidays")%>'>    
    <div class="container-fluid mt-5">
        <div class="card">
            <div class="card-header">
                <h4 style="display: inline-block">Assignar festius</h4>
                <input class="btn btn-success w20 float-right" type="submit" value="Gravar">
            </div>
            <div class="row">
                <div class="col-md-5">
                    <div class="card-body p-5">
                        <div class="datepicker date input-group">
                            <div id="datepickerHairdressingHolidays"></div>
                            <input type="hidden" name="selectedHairdressingHolidaysDates" id="selectedHairdressingHolidaysDates">
                        </div>  
                    </div>
                </div>
                <div class="col-md-7 container">    
                    <div class="p-5">
                        <table id="holidayDatatable" class="table table-striped customDataTable" cellspacing="0" width="100%">
                            <thead>
                                <tr>
                                    <th>Calendari de dies festius</th>
                                </tr>
                            </thead>
                            <tbody>  
                                <c:forEach var="item" items="<%=request.getAttribute("tableData")%>">
                                    <tr>
                                        <td>${item}</td>
                                    </tr>
                                </c:forEach>                                
                            </tbody>
                        </table>          
                    </div>
                </div>
            </div>  
        </div>        
    </div>
</form> 