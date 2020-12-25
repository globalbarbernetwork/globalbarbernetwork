/* 
 * Copyright (C) 2020 Grup 3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
var contextPath;
var hairdressersJSONArray;

$(document).ready(function() {
    contextPath = $("#contextPath").val();
    
    initializeDatepicker();
    
    $("#modalReserve").on('hidden.bs.modal', function () {
        cleanModalReserva();
    });
    
    $("#hairdressers").change(function() {
        var positionEmployee = $(this).val();
        
        if (positionEmployee != -1) {
            disableHolidaysSelectedEmployee(positionEmployee);
        } else {
            $("#reservationDate").data('datepicker').setDatesDisabled([]);
        }
    });
});

function loadInfoModalReserve(element) {
    var idHairdressingSelected = $(element).data("uid");
    $("#selectedIdHairdressing").val(idHairdressingSelected);
    $("#modalReserveLongTitle").text("Realitzar reserva en " + $(element).data("company"));
    
    // Carga empleados
    $.ajax({
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/getEmployeesAjax',
        data: {
            idHairdressing: idHairdressingSelected
        },
        dataType: "json",
        success: function (data) {
            hairdressersJSONArray = data.jsonArray;
            
            for (var i in hairdressersJSONArray) {
                $("#hairdressers").append(new Option(hairdressersJSONArray[i].nameSurname, i));
            }
        },
        error: function () {
            console.log("No se ha podido obtener la información");
        }
    });
    
    // Carga servicios
    $.ajax({
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/getServicesAjax',
        data: {
            idHairdressing: idHairdressingSelected
        },
        dataType: "json",
        success: function (data) {
            servicesJSONArray = data.jsonArray;
            console.log(data.jsonArray);
            for (var i in servicesJSONArray) {
                $("#services").append(new Option(servicesJSONArray[i].nameAndDuration, servicesJSONArray[i].idService));
            }
        },
        error: function () {
            console.log("No se ha podido obtener la información");
        }
    });
                
}

function cleanModalReserva() {
    $("#chooseHairdresser").prop('checked', false);
    $("#hairdressers").find('option').remove();
    $("#hairdressers").append(new Option("Escull un/a perruquer/a", -1));
    $("#services").find('option').remove();
    $("#services").append(new Option("Escull un servei", -1));
    showOrHideHairdressers(false);
    $("#reservationDate").val("");
    $("#availableHours").val(0);
}

function initializeDatepicker() {
    $('#reservationDate').datepicker({
        language: "ca",
        clearBtn: true,
        format: "dd/mm/yyyy",
        autoclose: true,
        daysOfWeekDisabled: [0, 6],
        daysOfWeekHighlighted: [0, 6],
        todayHighlight: true
    });


    // FOR DEMO PURPOSE
    $('#reservationDate').on('change', function () {
        var pickedDate = $('input').val();
        $('#pickedDate').html(pickedDate);
    });
}

function showOrHideHairdressers(isChecked) {
    if (isChecked) {
        $("#hairdressers").show();
    } else {
        $("#hairdressers").hide();
        $("#hairdressers").val(-1);
    }
}

function disableHolidaysSelectedEmployee(positionEmployee) {
    var idHairdressing = $("#selectedIdHairdressing").val();
    var idEmployee = hairdressersJSONArray[positionEmployee].idNumber;

    $.ajax({
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/getHolidaysEmployeeAjax',
        data: {
            idHairdressing: idHairdressing,
            idEmployee: idEmployee
        },
        success: function (data) {
            $("#reservationDate").data('datepicker').setDatesDisabled(data.jsonArray);
        },
        error: function () {
            console.log("No se ha podido obtener la información");
        }
    });
}