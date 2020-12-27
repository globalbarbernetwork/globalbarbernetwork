<%-- 
    Document   : manageSchedule
    Created on : Dec 6, 2020, 5:15:35 PM
    Author     : Grup 3
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form method="POST" action="${contextPath}/ManagementServlet/menuOption/manageHairdressing/updateHolidays">
    <input type="hidden" name="holidays" id="holidays" value='<%= request.getAttribute("holidays") %>'>    
    <div class="container-fluid mt-5">
        <div class="card">
            <div class="card-header">
                <h4 style="display: inline-block">Assignar festius</h4>
                <input class="btn btn-success w20 float-right" type="submit" value="Guardar">
            </div>
            <div class="card-body p-5">
                <div class="datepicker date input-group shadow-sm">
                    <div id="datepickerHairdressingHolidays"></div>
                    <input type="hidden" name="selectedHairdressingHolidaysDates" id="selectedHairdressingHolidaysDates">
                </div>  
            </div>  
        </div>
    </div>
</form> 