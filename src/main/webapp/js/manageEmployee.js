/* 
 * Copyright (C) 2020 IOC DAW
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

    //initializeDataTable();
    initializeDatepickerContract();
    $("#btnConfirmAddEdit").attr('disabled', true);

    $("#btnAdd").click(function () {
        addEmployee();
    });

    $("#modalEditEmployee").on('hidden.bs.modal', function () {
        cleanModalClose();
    });

    $("#contractIniDate, #contractEndDate").on('change', function () {
        checkDates($(this));
    });
    
    $("#modalEditEmployee").find("input[type=text]").blur(function () {
        validFieldsEmployee($(this));
    });


    initializeDatepickerEmployeeHolidays();
    $("#modalHolidaysEmployee").on('hidden.bs.modal', function () {
        cleanModalGestioVacances();
    });
    $("#saveHolidays").click(function () {
        saveHolidaysEmployee();
    });
});

function initializeDataTable() {
    $('#dataTable').DataTable({
        "pagingType": "numbers",
        "responsive": true,
        "order": [],
        "columnDefs": [{orderable: false, targets: [0, 7]}],
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Catalan.json"
        }
    });
}

function editEmployee(btnEdit) {
    var btnAddModal = $("#modalEditEmployee").find(".modal-footer").children();
    btnAddModal.removeClass("btn-info");
    btnAddModal.addClass("btn-warning");
    btnAddModal.text("Modificar");

    var row = $(btnEdit).parents("tr").children();
    var td = 1;
    $("#modalEditEmployee").find("input[type=text]").each(function () {
        $(this).val($(row.get(td)).text());
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

    $("#idNumberEmployeeToDelete").val(idNumber);
    $("#modalDeleteEmployee").find("h4").text("Eliminar a " + name + " " + surname);
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

function cleanModalClose() {
    $("#modalEditEmployee").find("input").each(function () {
        $(this).val("");
        $(this).removeClass("is-invalid");
        $(this).next().text("");
        $("#idNumber").prop('disabled', false);
    });
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
    };
    checkIfAllInputsOK();
}

function checkIfAllInputsOK(){
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
    var contractIniDate = $("#contractIniDate").val();
    var contractEndDate = $("#contractEndDate").val();

    if (input.val().length == 0) {
        input.next().text("");
        input.addClass('is-invalid');
    } else {
        if (contractIniDate != "" && contractEndDate != "") {
            var iniCorrectFormat = contractIniDate.split("/");
            var ini = new Date(iniCorrectFormat[2], iniCorrectFormat[1]-1, iniCorrectFormat[0]);
            
            var endCorrectFormat = contractEndDate.split("/");
            var end = new Date(endCorrectFormat[2], endCorrectFormat[1]-1, endCorrectFormat[0]);
            
            iniBiggerThanEndDate = ini.getTime() >= end.getTime();
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
        //daysOfWeekDisabled: [0, 6],
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