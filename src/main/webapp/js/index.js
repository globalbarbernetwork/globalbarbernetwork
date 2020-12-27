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
            $("#reservationDate").datepicker('setDatesDisabled', []);
        }
    });

    // Cuando cambia la fecha de reserva, se recogen las horas disponibles.
    $("#reservationDate").on('changeDate', function (e) {
        var dateS = e.date;
        console.log("Month :"+dateS.getMonth());
        if (e.date != undefined) {
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
        }
    });

    // Cuando cambia el peluquero/a, se recogen las horas disponibles siempre y cuando este informada la fecha de reserva y el servicio.
    $("#hairdressers").change(function () {
        var selectedService = $("#services").val();
        var selectedDate = $("#reservationDate").datepicker('getDate');

        if (selectedService != -1 && selectedDate != null) {
            getAvailableHours();
        }
    });
});

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
}

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
            var holidaysEmployeeJSONArray = data.jsonArray;
            var selectedDate = $("#reservationDate").datepicker('getFormattedDate');

            if (holidaysEmployeeJSONArray.includes(selectedDate)) {
                $("#reservationDate").datepicker('setDate', null);
            }
            $("#reservationDate").datepicker('setDatesDisabled', holidaysEmployeeJSONArray);
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
    console.log(selectedDate);
    console.log(idServiceSelected);
    console.log(idHairdresserSelected);

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

            cleanSelect("availableHours", "Tria una hora", false);
            for (var i in availableHoursJSONArray) {
                $("#availableHours").append(new Option(availableHoursJSONArray[i].timeInFormat, availableHoursJSONArray[i].timeInMinutes));
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
}

function cleanModalReserva() {
    $("#chooseHairdresser").prop('checked', false);
    showOrHideHairdressers(false); // Donde situarlo o que hacer con el

    cleanSelect("hairdressers", "Escull un/a perruquer/a", false);

    cleanSelect("services", "Escull un servei", false);

    $("#reservationDate").datepicker('clearDates');

    cleanSelect("availableHours", "Tria una data primer", false);
}
