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
        "columnDefs": [{orderable: false, targets: [0, 1, 2]}]
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
});

function editEmployee(btnEdit) {
    var btnAddModal = $("#modalEditEmployee").find(".modal-footer").children();
    btnAddModal.removeClass("btn-info");
    btnAddModal.addClass("btn-warning");
    btnAddModal.text("Modificar");
    var h4TitleModal = $("#modalEditEmployee").find("h4");
    h4TitleModal.text("Modifica un treballador");

    var row = $(btnEdit).parents("tr").children();
    var td = 3;
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
    $("#natIdenEmployee").val($(row.get(5)).text());
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