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

var contextPath = $("#contextPath").val();

$(document).ready(function () {
    var allInputsOk = true;
    $("#btnConfirmAddEdit").attr('disabled', true);
    $('#dataTable').DataTable({
        "pagingType": "numbers",
        "order": [],
        "columnDefs": [{orderable: false, targets: [0,7]}]
    });

    $("#btnAdd").click(function () {
        addEmployee();
    });

    $("#modalEditEmployee").find("input").blur(function () {

        //Miramos que ningun campo este vacio.
        $("#modalEditEmployee").find("input").each(function () {
            if ($(this).val() === '') {
                allInputsOk = false;
            }
        });


        if ($(this).val() === '') {
            $(this).addClass('is-invalid');
        } else {
            switch ($(this).attr('name')) {
                case "name" :
                    if ($(this).val().length > 30) {
                        $(this).addClass('is-invalid');
                    } else {
                        $(this).removeClass('is-invalid');
                    }
                    break;
                case "surname" :
                    if ($(this).val().length > 50) {
                        $(this).addClass('is-invalid');
                    } else {
                        $(this).removeClass('is-invalid');
                    }
                    break;
                case "nationalIdentity" :
                    var correctNatIdent = checkIfDniNieCorrect($(this));
                    if (!correctNatIdent) {
                        $(this).addClass('is-invalid');
                    } else {
                        $(this).removeClass('is-invalid');
                    }
                    break;
                case "age" :
                    var isCorrectAge = ($(this).val()).match(/^[0-9]{1,2}$/);
                    if (!isCorrectAge) {
                        $(this).addClass('is-invalid');
                    } else {
                        $(this).removeClass('is-invalid');
                    }
                    break;
                case "address" :
                    if ($(this).val().length > 50) {
                        $(this).addClass('is-invalid');
                    } else {
                        $(this).removeClass('is-invalid');
                    }
                    break;
                case "phoneNumber" :
                    var isCorrectPhone = ($(this).val()).match(/^\d{9}$/);
                    if (!isCorrectPhone) {
                        $(this).addClass('is-invalid');
                    } else {
                        $(this).removeClass('is-invalid');
                    }
                    break;
            }
            ;
            allInputsOk = !$("#modalEditEmployee").find("input").hasClass("is-invalid");
        }



        if (allInputsOk) {
            $("#btnConfirmAddEdit").removeAttr('disabled');
        } else {
            $("#btnConfirmAddEdit").attr('disabled', true);
        }
    });
    
    initializeDatepicker();
    $("#modalHolidaysEmployee").on('hidden.bs.modal', function () {
        cleanModalGestioVacances();
    });
    $("#saveHolidays").click(function() {
        saveHolidaysEmployee();
    });
});

function editEmployee(btnEdit) {
    var btnAddModal = $("#modalEditEmployee").find(".modal-footer").children();
    btnAddModal.removeClass("btn-info");
    btnAddModal.addClass("btn-warning");
    btnAddModal.text("Modificar");
    var h4TitleModal = $("#modalEditEmployee").find("h4");
    h4TitleModal.text("Modifica un treballador");

    var row = $(btnEdit).parents("tr").children();
    var td = 1;
    $("#modalEditEmployee").find("input").each(function () {
        $(this).val($(row.get(td)).text());
        td++;
    });

    $("#formEmployee").attr("action", contextPath + "/ManagementServlet/menuOption/manageHaird/editEmployee");
}

function addEmployee() {
    var btnAddModal = $("#modalEditEmployee").find(".modal-footer").children();
    btnAddModal.removeClass("btn-warning");
    btnAddModal.addClass("btn-info");
    btnAddModal.text("Afegir");
    $("#formEmployee").attr("action", contextPath + "/ManagementServlet/menuOption/manageHaird/addEmployee");

    var h4TitleModal = $("#modalEditEmployee").find("h4");
    h4TitleModal.text("Afegeix un nou treballador");

    $("#modalEditEmployee").find("input").each(function () {
        $(this).val("");
    });
}
function getNatIdenEmployeeToDelete(btnDelete) {
    var row = $(btnDelete).parents("tr").children();
    console.log(row);
    $("#natIdenEmployee").val($(row.get(3)).text());
}

function validFormEmployee() {

    $("#modalEditEmployee").find("input").each(function () {
        console.log($(this).val());
    });
}

function checkIfDniNieCorrect(fieldNationalIdent) {
    //Comprobamos que el dni o nie introducido sea correcto
    var number;
    var letter;
    var possLetters;
    var natIdent = fieldNationalIdent.val();
    //Comprobamos DNI
    if ((/^\d{8}[a-zA-Z]$/).test(natIdent) === true) {
        number = natIdent.substr(0, natIdent.length - 1);
        letter = natIdent.substr(natIdent.length - 1, 1);
        number = number % 23;
        possLetters = 'TRWAGMYFPDXBNJZSQVHLCKE';
        possLetters = possLetters.substring(number, number + 1);
        return possLetters === letter.toUpperCase();
    } else if ((/^[T|X|Y|Z]\d{8}[a-zA-Z]$/).test(natIdent) === true) { //Comprobamos NIE
        number = natIdent.substr(1, natIdent.length - 2);
        letter = natIdent.substr(natIdent.length - 1, 1);
        number = number % 23;
        possLetters = 'TRWAGMYFPDXBNJZSQVHLCKE';
        possLetters = possLetters.substring(number, number + 1);
        return possLetters === letter.toUpperCase();
    }
    return false;
}

function initializeDatepicker() {
    $('#datepickerHolidays').datepicker({
        multidate: true,
        language: "ca",
        clearBtn: true,
        format: "dd/mm/yyyy",
        autoclose: true,
        //daysOfWeekDisabled: [0, 6],
        daysOfWeekHighlighted: [0, 6],
        todayHighlight: true
    });


    // Guardamos las fechas seleccionadas en un hidden input
    $('#datepickerHolidays').on('changeDate', function () {
        $('#selectedHolidays').val($('#datepickerHolidays').datepicker('getFormattedDate'));
    });
}

function cleanModalGestioVacances() {
    $("#datepickerHolidays").data('datepicker').setDate(null);
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
}

function saveHolidaysEmployee() {
    var idHairdressing = $("#idHairdressing").val();
    var idEmployee = $("#selectedIdEmployee").val();
    var selectedHolidays = $("#selectedHolidays").val();
    
    $.ajax({
        url: 'manageHaird/saveHolidaysEmployeeAjax',
        data: {
            idHairdressing: idHairdressing,
            idEmployee: idEmployee,
            selectedHolidays: selectedHolidays
        },
        success: function (data) {
            $("#modalHolidaysEmployee").modal('hide');
            Swal.fire({
                icon: 'success',
                title: 'Grabación correcta',
                showConfirmButton: false,
                timer: 1500
            });
        },
        error: function () {
            console.log("No se ha podido obtener la información");
            $("#modalHolidaysEmployee").modal('hide');
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Grabación incorrecta, contacta con el administrador por favor!'
            });
        }
    });
}