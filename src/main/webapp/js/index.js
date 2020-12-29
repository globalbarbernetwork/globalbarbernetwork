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
var holidaysHairdressingJSONArray;
var nonWorkingDaysOfWeekJSONArray;
var holidaysEmployeeJSONArray;

$(document).ready(function () {
    contextPath = $("#contextPath").val();

    // Inicializa el datepicker
    initializeDatepicker();

    // Limpia los campos del modal al cerrarse
    $("#modalReserve").on('hidden.bs.modal', function () {
        cleanModalReserva();
    });

    // Cuando cambia de peluquero/a se recogen y deshabilitan en el datepicker los días de vacaciones de este.
    $("#hairdressers").change(function () {
        var positionEmployee = $(this).val();

        if (positionEmployee != -1) {
            disableHolidaysSelectedEmployee(positionEmployee);
        } else {
            holidaysEmployeeJSONArray = [];
            $("#reservationDate").datepicker('setDatesDisabled', []);
        }
    });

    // Cuando cambia la fecha de reserva, se recogen las horas disponibles.
    $("#reservationDate").on('changeDate', function (e) {
        var selectedService = $("#services").val();
        if (e.date != undefined && selectedService != -1) {
            getAvailableHours();
        } else {
            cleanSelect("availableHours", "Tria una data primer", true);
        }
    });

    // Cuando cambia el servicio, se recogen las horas disponibles siempre y cuando este informada la fecha de reserva.
    $("#services").change(function () {
        var selectedDate = $("#reservationDate").datepicker('getDate');
        if ($(this).val() != -1 && selectedDate != null) {
            getAvailableHours();
        } else {
            cleanSelect("availableHours", "Tria una data primer", true);
        }
    });

    // Cuando cambia el peluquero/a, se recogen las horas disponibles siempre y cuando este informada la fecha de reserva y el servicio.
    $("#hairdressers").change(function () {
        var selectedService = $("#services").val();
        var selectedDate = $("#reservationDate").datepicker('getDate');
        
        if (selectedService != -1 && selectedDate != null) {
            getAvailableHours();
        } else {
            cleanSelect("availableHours", "Tria una data primer", true);
        }
    });
    
    $("#services, #availableHours").change(function () {
        var value = $(this).val();
        if (value == -1){
            $(this).addClass("is-invalid");
        } else {
            $(this).removeClass("is-invalid");
        }
        validationFieldsReserve();
    });
    
    $("#reservationDate").on('changeDate', function (e) {
        if (e.date == undefined){
            $(this).addClass("is-invalid");
        } else {
            $(this).removeClass("is-invalid");
        }
        validationFieldsReserve();
    });
    
    $("#doReserve").click(function () {        
        doReserve();
    });
});

function initializeDatepicker() {
    var today = new Date();
    var limit = new Date(today.setMonth(today.getMonth() + 2));
    
    $('#reservationDate').datepicker({
        language: "ca",
        format: "dd/mm/yyyy",
        startDate: new Date(),
        endDate: limit,
        todayHighlight: true,
        daysOfWeekHighlighted: [0, 6],
        clearBtn: true,
        autoclose: true,
        beforeShowDay: function (date) {
            var dd = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            var mm = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+1) : date.getMonth()+1;
            var yyyy = date.getFullYear();
            
            var dateFormat = dd + "/" + mm + "/" + yyyy;
            
            var today = new Date();
            var dd2 = today.getDate() < 10 ? "0" + today.getDate() : today.getDate();
            var mm2 = (today.getMonth() + 1) < 10 ? "0" + (today.getMonth()+1) : today.getMonth()+1;
            var yyyy2 = today.getFullYear();
            
            if (holidaysHairdressingJSONArray != undefined && holidaysHairdressingJSONArray.includes(dateFormat)) {
                return {enabled: false, classes: "disabled-date disabled-date-hairdressing", tooltip: "Perruqueria tancada"};
            } else if (holidaysEmployeeJSONArray != undefined && (parseInt(dd) >= parseInt(dd2) && parseInt(mm) >= parseInt(mm2) && parseInt(yyyy) >= parseInt(yyyy2)) 
                    && holidaysEmployeeJSONArray.includes(dateFormat)) {
                return {enabled: false, classes: "disabled-date disabled-date-employee", tooltip: "Perruquer/a no disponible per aquest dia"};
            } else if (nonWorkingDaysOfWeekJSONArray != undefined && nonWorkingDaysOfWeekJSONArray.includes(date.getDay())) {
                return {tooltip: "Perruqueria tancada"};
            }
        }
    });
}

function loadInfoModalReserve(element) {
    var idHairdressingSelected = $(element).data("uid");
    $("#selectedIdHairdressing").val(idHairdressingSelected);
    $("#modalReserveLongTitle").text("Realitzar reserva en " + $(element).data("company"));
    
    disableHolidaysSelectedHairdressing(idHairdressingSelected);

    loadSelectEmployees(idHairdressingSelected);

    loadSelectServices(idHairdressingSelected);
}

function disableHolidaysSelectedHairdressing(idHairdressing) {
    $.ajax({
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/getDisableDaysHairdressing',
        data: {
            idHairdressing: idHairdressing
        },
        dataType: "json",
        success: function (data) {
            holidaysHairdressingJSONArray = data.jsonArrayHolidaysHairdressing;
            nonWorkingDaysOfWeekJSONArray = data.jsonArrayNonWorkingDaysOfWeek;
            
            $("#reservationDate").datepicker('setDatesDisabled', holidaysHairdressingJSONArray);
            $("#reservationDate").datepicker('setDaysOfWeekDisabled', nonWorkingDaysOfWeekJSONArray);
        },
        error: function () {
            console.log("No se ha podido obtener la información");
        }
    });
}

function loadSelectEmployees(idHairdressing) {
    // Carga empleados
    $.ajax({
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/getEmployeesAjax',
        data: {
            idHairdressing: idHairdressing
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
}

function loadSelectServices(idHairdressing) {
    // Carga servicios
    $.ajax({
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/getServicesAjax',
        data: {
            idHairdressing: idHairdressing
        },
        dataType: "json",
        success: function (data) {
            servicesJSONArray = data.jsonArray;
            for (var i in servicesJSONArray) {
                $("#services").append(new Option(servicesJSONArray[i].descService, servicesJSONArray[i].idService));
            }
        },
        error: function () {
            console.log("No se ha podido obtener la información");
        }
    });
}

function showOrHideHairdressers(isChecked) {
    if (isChecked) {
        $("#hairdressers").show();
    } else {
        $("#hairdressers").hide();
        $("#hairdressers").val(-1);
        $("#hairdressers").trigger("change");
        $("#reservationDate").datepicker('setDatesDisabled', []);
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
            holidaysEmployeeJSONArray = data.jsonArray;
            var selectedDate = $("#reservationDate").datepicker('getFormattedDate');

            if (holidaysEmployeeJSONArray.includes(selectedDate)) {
                $("#reservationDate").datepicker('setDate', null);
            }
            
            //Seteamos a 0 para que se active la función beforeShowDay y ahi entonces deshabilitarlos cumpliendo X condiciones,
            //ya que si deshabilitamos aquí todos los días pero después no cumplen X condición pues seguirán deshabilitados.
            $("#reservationDate").datepicker('setDatesDisabled', []);
        },
        error: function () {
            console.log("[ERROR] Ha habido algún error durante el proceso de recogida de vacaciones.");
        }
    });
}

function getAvailableHours() {
    var selectedDate = $('#reservationDate').datepicker('getFormattedDate');
    var idServiceSelected = $("#services").val();
    var idHairdressingSelected = $("#selectedIdHairdressing").val();

    var positionHaidresser = $("#hairdressers").val();
    var idHairdresserSelected = null;

    if (positionHaidresser !== "-1") {
        idHairdresserSelected = hairdressersJSONArray[positionHaidresser].idNumber;
    }

    $.ajax({
        url: contextPath + '/ManagementServlet/schedule/getAvailableHoursAjax',
        data: {
            selectedDate: selectedDate,
            idHairdresserSelected: idHairdresserSelected,
            idServiceSelected: idServiceSelected,
            idHairdressingSelected: idHairdressingSelected
        },
        dataType: "json",
        success: function (data) {
            var availableHoursJSONArray = data.jsonArray;
            
            if (availableHoursJSONArray.length == 0) {
                cleanSelect("availableHours", "Hores no disponibles", true);
            } else {
                cleanSelect("availableHours", "Tria una hora", false);
                for (var i in availableHoursJSONArray) {
                    $("#availableHours").append(new Option(availableHoursJSONArray[i].timeInFormat, availableHoursJSONArray[i].timeInMinutes));
                }
            }
        },
        error: function () {
            console.log("[ERROR] Ha habido algún error durante el proceso de recogida de vacaciones.");
        }
    });
}

function cleanSelect(idSelect, nameOption, disabled) {
    $("#" + idSelect).find('option').remove();
    $("#" + idSelect).append(new Option(nameOption, -1));
    $("#" + idSelect).prop("disabled", disabled);
    $("#" + idSelect).removeClass("is-invalid");
}

function cleanModalReserva() {
    cleanSelect("hairdressers", "Escull un/a perruquer/a", false);

    cleanSelect("services", "Escull un servei", false);

    $("#reservationDate").datepicker('clearDates');
    $("#reservationDate").removeClass("is-invalid");

    cleanSelect("availableHours", "Tria una data primer", true);
    
    $("#chooseHairdresser").prop('checked', false);
    showOrHideHairdressers(false);
}

function validationFieldsReserve() {
    var idService = $("#services").val();
    var date = $("#reservationDate").datepicker('getDate');
    var time = $("#availableHours").val();

    if (idService != -1 && date != null && time != -1) {
        $("#doReserve").prop("disabled", false);
    } else {
        $("#doReserve").prop("disabled", true);
    }
}

function doReserve() {
    var idHairdressing = $("#selectedIdHairdressing").val();
    
    var positionHaidresser = $("#hairdressers").val();
    var idHairdresser = null;
    if (positionHaidresser !== "-1") {
        idHairdresser = hairdressersJSONArray[positionHaidresser].idNumber;
    }
    
    var idService = $("#services").val();
    var date = $("#reservationDate").datepicker('getFormattedDate');
    var time = $("#availableHours").val();
    
    $.ajax({
        url: contextPath + '/ManagementServlet/schedule/addReserveAjax',
        data: {
            idHairdressing: idHairdressing,
            idHairdresser: idHairdresser,
            idService: idService,
            date: date,
            time: time
        },
        dataType: "json",
        success: function (data) {
            $("#modalReserve").modal('hide');
            Swal.fire({
                icon: 'success',
                title: 'Gravació correcte',
                text: data.message
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status == 409) {
                console.log("Error controlat: " + errorThrown);
            } else {
                console.log("Error desconocido.");
            }
            
            $("#modalReserve").modal('hide');
            Swal.fire({
                icon: 'error',
                title: 'Gravació incorrecte',
                text: errorThrown
            });
        }
    });
}
