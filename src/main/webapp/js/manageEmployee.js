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

$(document).ready(function () {
    contextPath = $("#contextPath").val();

    initializeDatepickerContract();
    $("#btnConfirmAddEdit").attr('disabled', true);

    $("#btnAdd").click(function () {
        addEmployee();
    });

    $("#modalEditEmployee").on('hidden.bs.modal', function () {
        cleanModalEmployee();
    });

    $("#contractIniDate, #contractEndDate").on('change', function () {
        checkDates($(this));
    });

    $("#modalEditEmployee").find("input[type=text]").blur(function () {
        validFieldsEmployee($(this));
    });


    initializeDatepickerEmployeeHolidays();
    stylizeDatePicker("datepickerEmployeeHolidays"); //function de manageHolidays.js
    $("#modalHolidaysEmployee").on('hidden.bs.modal', function () {
        cleanModalGestioVacances();
    });
    $("#saveHolidays").click(function () {
        saveHolidaysEmployee();
    });
});


function editEmployee(btnEdit) {
    var btnAddModal = $("#modalEditEmployee").find(".modal-footer").children();
    btnAddModal.removeClass("btn-info");
    btnAddModal.addClass("btn-warning");
    btnAddModal.text("Modificar");

    var row = $(btnEdit).parents("tr").children();
    var td = 1;
    $("#modalEditEmployee").find("input[type=text]").each(function () {
        $(this).val($(row.get(td)).text());
        if ($(this).attr('id') == "contractIniDate" || $(this).attr('id') == "contractEndDate") {
            $(this).datepicker('setDates', $(row.get(td)).text());
        }
        td++;
    });

    var name = $(btnEdit).data("name");
    var surname = $(btnEdit).data("surname");
    var idNumber = $(btnEdit).data("idnumber");

    var h4TitleModal = $("#modalEditEmployee").find("h4");
    h4TitleModal.text("Modificant a " + name + " " + surname);
    $("#formEmployee").attr("action", contextPath + "/ManagementServlet/menuOption/manageHairdressing/editEmployee");
    $("#idNumberEmployeeToEdit").val(idNumber);
    $("#idNumber").prop('disabled', true);
}

function addEmployee() {
    var btnAddModal = $("#modalEditEmployee").find(".modal-footer").children();
    btnAddModal.removeClass("btn-warning");
    btnAddModal.addClass("btn-info");
    btnAddModal.text("Afegir");
    $("#formEmployee").attr("action", contextPath + "/ManagementServlet/menuOption/manageHairdressing/addEmployee");

    var h4TitleModal = $("#modalEditEmployee").find("h4");
    h4TitleModal.text("Afegeix un nou treballador");
}

function deleteEmployee(btnDelete) {
    var name = $(btnDelete).data("name");
    var surname = $(btnDelete).data("surname");
    var idNumber = $(btnDelete).data("idnumber");

    checkIfEmployeeHasReserves(idNumber);

    $("#idNumberEmployeeToDelete").val(idNumber);
    $("#modalDeleteEmployee").find("h4").text("Eliminar a " + name + " " + surname);
}

function checkIfEmployeeHasReserves(idNumber) {
    $.ajax({
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/checkEmployeeHasReserveAjax',
        data: {
            idNumberEmployee: idNumber
        },
        success: function (data) {
            var employeeHasReserves = data["employeeHasReserves"];
            console.log(employeeHasReserves);
            if (employeeHasReserves) {
                $("#infoDelete").text(" Aquest treballador té reserves a càrrec seu! Estàs segur que vols eliminar-lo?\n"
                        + "En cas afirmatiu, s'eliminarà les reserves associades com tota la seva informació.");
            } else {
                $("#infoDelete").text(" Estàs segur que vols eliminar aquest treballador?");
            }
            $("#modalDeleteEmployee").modal('show');
        },
        error: function () {
            console.log("No se ha podido obtener la información");
        }
    }
    );
}

function checkIfDniNieCorrect(fieldIdNumber) {
    //Comprobamos que el dni o nie introducido sea correcto
    var number;
    var letter;
    var possLetters;
    var idNumber = fieldIdNumber.val();
    //Comprobamos DNI
    if ((/^\d{8}[a-zA-Z]$/).test(idNumber) === true) {
        number = idNumber.substr(0, idNumber.length - 1);
        letter = idNumber.substr(idNumber.length - 1, 1);
        number = number % 23;
        possLetters = 'TRWAGMYFPDXBNJZSQVHLCKE';
        possLetters = possLetters.substring(number, number + 1);
        return possLetters === letter.toUpperCase();
    } else if ((/^[X|Y|Z]\d{7}[a-zA-Z]$/).test(idNumber) === true) { //Comprobamos NIE
        number = idNumber.substr(1, idNumber.length - 2);
        letter = idNumber.substr(idNumber.length - 1, 1);
        var firstLetter = idNumber.substr(0, 1);
        if (firstLetter === "Y") {
            number = "1" + number;
        } else if (firstLetter === "Z") {
            number = "2" + number;
        }
        number = number % 23;
        possLetters = 'TRWAGMYFPDXBNJZSQVHLCKE';
        possLetters = possLetters.substring(number, number + 1);
        return possLetters === letter.toUpperCase();
    }
    return false;
}

function checkIfIdNumberExists(inputIdNumber) {
    var idNumber = inputIdNumber.val();
    $.ajax({
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/checkEmployeeAjax',
        data: {
            idNumberEmployee: idNumber
        },
        success: function (data) {
            var idNumberExists = data["idNumberExistInHairdressing"];

            if (idNumberExists) {
                inputIdNumber.addClass('is-invalid');
                inputIdNumber.next().text("* Aquest identificador ja hi és a la perruqueria");
            } else {
                inputIdNumber.removeClass('is-invalid');
                inputIdNumber.next().text("");
            }
        },
        error: function () {
            console.log("No se ha podido obtener la información");
        }
    }
    );
}

function cleanModalEmployee() {
    $("#modalEditEmployee").find("input[type=text]").each(function () {
        if ($(this).parent().hasClass('datepicker')) {
            $(this).datepicker('clearDates');
            $(this).parent().next().text("");
            $(this).removeClass("is-invalid");
        } else {
            $(this).val("");
            $(this).next().text("");
            $(this).removeClass("is-invalid");
        }
    });
    $("#idNumber").prop('disabled', false);
}

function validFieldsEmployee(input) {
    var inputValue = input.val();

    switch (input.attr('name')) {
        case "name" :
            if (inputValue === '' || inputValue.length > 30) {
                input.addClass('is-invalid');
            } else {
                input.removeClass('is-invalid');
            }
            break;
        case "surname" :
            if (inputValue === '' || inputValue.length > 35) {
                input.addClass('is-invalid');
            } else {
                input.removeClass('is-invalid');
            }
            break;
        case "idNumber" :
            var correctIdNumber = checkIfDniNieCorrect(input);
            if (inputValue === '' || !correctIdNumber) {
                input.addClass('is-invalid');
                input.next().text("");
            } else {
                checkIfIdNumberExists(input);
            }
            break;
        case "phoneNumber" :
            var isCorrectPhone = (inputValue).match(/^\d{9}$/);
            if (inputValue === '' || !isCorrectPhone) {
                input.addClass('is-invalid');
            } else {
                input.removeClass('is-invalid');
            }
            break;
    }
    ;
    checkIfAllInputsOK();
}

function checkIfAllInputsOK() {
    allInputsOk = !$("#modalEditEmployee").find("input[type=text]").hasClass("is-invalid");
    $("#modalEditEmployee").find("input[type=text]").each(function () {
        if ($(this).val() === '') {
            allInputsOk = false;
        }
    });

    if (allInputsOk) {
        $("#btnConfirmAddEdit").removeAttr('disabled');
    } else {
        $("#btnConfirmAddEdit").attr('disabled', true);
    }
}

function checkDates(input) {
    var iniBiggerThanEndDate;
    var contractIniDate = $("#contractIniDate").datepicker('getDate');
    var contractEndDate = $("#contractEndDate").datepicker('getDate');

    if (input.val().length == 0) {
        input.parent().next().text("");
        input.addClass('is-invalid');
    } else {
        if (contractIniDate != null && contractEndDate != null) {
            iniBiggerThanEndDate = contractIniDate >= contractEndDate;
        }
        var textError = "* La data inici no pot ser igual o major que la data final";
        if (input.attr('name') == "contractEndDate") {
            textError = "* La data final no pot ser igual o menor que la data inici";
        }

        $("#contractIniDate").removeClass('is-invalid');
        $("#contractIniDate").parent().next().text("");

        $("#contractEndDate").removeClass('is-invalid');
        $("#contractEndDate").parent().next().text("");

        if (iniBiggerThanEndDate) {
            input.addClass('is-invalid');
            input.parent().next().text(textError);
        } else {
            input.removeClass('is-invalid');
            input.parent().next().text("");
        }
    }
    checkIfAllInputsOK();
}

function initializeDatepickerContract() {
    $('#contractIniDate').datepicker({
        language: "ca",
        clearBtn: true,
        format: "dd/mm/yyyy",
        autoclose: true,
        daysOfWeekHighlighted: [0, 6],
        todayHighlight: true
    });

    $('#contractEndDate').datepicker({
        language: "ca",
        clearBtn: true,
        format: "dd/mm/yyyy",
        autoclose: true,
        daysOfWeekHighlighted: [0, 6],
        todayHighlight: true
    });
}

// Part
function initializeDatepickerEmployeeHolidays() {
    $('#datepickerEmployeeHolidays').datepicker({
        language: "ca",
        format: "dd/mm/yyyy",
        multidate: true,
        daysOfWeekHighlighted: [0, 6],
        todayHighlight: true,
        clearBtn: true
    });


    // Guardamos las fechas seleccionadas en un hidden input
    $('#datepickerEmployeeHolidays').on('changeDate', function () {
        $('#selectedHolidays').val($('#datepickerEmployeeHolidays').datepicker('getFormattedDate'));
    });
}

function cleanModalGestioVacances() {
    $("#datepickerEmployeeHolidays").datepicker('clearDates');
    $("#datepickerEmployeeHolidays").datepicker('setViewMode', 0);
    $("#idHairdressing").val("");
    $("#selectedIdEmployee").val("");
}

function loadInfoModalHolidays(element) {
    var idHairdressing = $(element).data("idhairdressing");
    var idEmployee = $(element).data("idemployee");
    var nameAndSurname = $(element).data("name") + " " + $(element).data("surname");

    $("#headModalHolidays").html("Gestionar vacances de " + nameAndSurname);
    $("#idHairdressing").val(idHairdressing);
    $("#selectedIdEmployee").val(idEmployee);

    disableHolidaysHairdressingAndGetHolidaysEmployee(idHairdressing, idEmployee);
}

function disableHolidaysHairdressingAndGetHolidaysEmployee(idHairdressing, idEmployee) {
    $.ajax({
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/getDisableDaysHairdressing',
        data: {
            idHairdressing: idHairdressing
        },
        dataType: "json",
        success: function (data) {
            var holidaysHairdressingJSONArray = data.jsonArrayHolidaysHairdressing;
            var nonWorkingDaysOfWeekJSONArray = data.jsonArrayNonWorkingDaysOfWeek;

            $("#datepickerEmployeeHolidays").datepicker('setDatesDisabled', holidaysHairdressingJSONArray);
            $("#datepickerEmployeeHolidays").datepicker('setDaysOfWeekDisabled', nonWorkingDaysOfWeekJSONArray);

            getHolidaysEmployee(idHairdressing, idEmployee);
        },
        error: function () {
            console.log("No se ha podido obtener la información");
        }
    });
}

function getHolidaysEmployee(idHairdressing, idEmployee) {
    // Recoge festivos del empleado
    $.ajax({
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/getHolidaysEmployeeAjax',
        data: {
            idHairdressing: idHairdressing,
            idEmployee: idEmployee
        },
        dataType: "json",
        success: function (data) {
            $("#datepickerEmployeeHolidays").datepicker('setDates', data.jsonArray);
            $("#datepickerEmployeeHolidays").datepicker('_setDates', new Date(), 'view');
        },
        error: function () {
            console.log("No se ha podido obtener la información");
        }
    });
}

function saveHolidaysEmployee() {
    var idHairdressing = $("#idHairdressing").val();
    var idEmployee = $("#selectedIdEmployee").val();
    var selectedHolidays = $("#selectedHolidays").val();

    $.ajax({
        method: "POST",
        url: contextPath + '/ManagementServlet/menuOption/manageHairdressing/saveHolidaysEmployeeAjax',
        data: {
            idHairdressing: idHairdressing,
            idEmployee: idEmployee,
            selectedHolidays: selectedHolidays
        },
        success: function () {
            $("#modalHolidaysEmployee").modal('hide');
            Swal.fire({
                icon: 'success',
                title: 'Gravació correcte',
                showConfirmButton: false,
                timer: 1500
            });
        },
        error: function () {
            console.log("Error");
            $("#modalHolidaysEmployee").modal('hide');
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Gravació incorrecte, contacti amb l\'administrador si us plau!'
            });
        }
    });
}