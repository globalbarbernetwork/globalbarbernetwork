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

$(document).ready(function() {
    initializeDatepicker();
    
    $("#modalReserve").on('hidden.bs.modal', function () {
        cleanModalReserva();
    });
});

function loadInfoModalReserve(element) {
    var idHairdressingSelected = $(element).data("uid");
    $("#modalReserveLongTitle").text("Realitzar reserva en " + $(element).data("company"));
    
    $.ajax({
        url: 'ManagementServlet/menuOption/manageHairdressing/getEmployeesAjax',
        data: {
            idHairdressing: idHairdressingSelected
        },
        success: function (data) {
            var hairdressersJSON = JSON.parse(data);
            var hairdressersJSONArray = hairdressersJSON.jsonArray;
            
            for (var i in hairdressersJSONArray) {
                $("#hairdressers").append(new Option(hairdressersJSONArray[i].name), i);
            }
        },
        error: function () {
            console.log("No se ha podido obtener la información");
        }
    });
                
}

function cleanModalReserva() {
    $("#chooseHairdresser").prop('checked', false);
    showOrHideHairdressers(false);
    $("#reservationDate").val("");
    $("#availableHours").val(0);
}

function initializeDatepicker() {
    $('.datepicker').datepicker({
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
        $("#hairdressers").val(0);
    }
}
