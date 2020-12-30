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
var incrementMin;

$(document).ready(function () {
    contextPath = $("#contextPath").val();
    incrementMin = $("#incrementMin").val();

    $("#btnConfirmAddEditService").attr('disabled', true);

    initializeTimepicker();

    $("#btnAddService").click(function () {
        addService();
    });

    $("#modalEditService").on('hidden.bs.modal', function () {
        cleanModalClose();
    });

    $("#nameService, #priceService").blur(function () {
        validInputs($(this));
    });
});

function validInputs(input) {
    var inputValue = input.val();
    var allInputsOk = false;

    if (input.attr('name') === "nameService") {
        if (inputValue === '' || inputValue.length > 50) {
            input.addClass('is-invalid');
            allInputsOk = false;
        } else {
            input.removeClass('is-invalid');
            allInputsOk = true;
        }
    } else if (input.attr('name') === "timePickerService") {
        if (inputValue === '') {
            allInputsOk = false;
        } else {
            allInputsOk = true;
        }
    } else if (input.attr('name') === "priceService") {
        if (inputValue === '') {
            allInputsOk = false;
            input.addClass('is-invalid');
            input.next().text('');
        } else {
            if ((/^(\d*\,?\d+|\d{1,3}(,\d{1,2})*(\,\d+)?)$/).test(inputValue) === true) {
                allInputsOk = true;
                input.removeClass('is-invalid');
                input.next().text('');
            } else {
                allInputsOk = false;
                input.addClass('is-invalid');
                input.next().text('* El format del preu es incorrecte: Ex. 0,90; 50; 0,9;');
            }
        }
    }

    $("#modalEditService").find("input[type=text]").each(function () {
        if ($(this).val() === '') {
            allInputsOk = false;
        }
    });

    if (allInputsOk) {
        $("#btnConfirmAddEditService").removeAttr('disabled');
    } else {
        $("#btnConfirmAddEditService").attr('disabled', true);
    }
}

function editService(btnEdit) {
    var btnAddModal = $("#modalEditService").find(".modal-footer").children();
    btnAddModal.removeClass("btn-info");
    btnAddModal.addClass("btn-warning");
    btnAddModal.text("Modificar");

    var row = $(btnEdit).parents("tr").children();
    var td = 1;
    $("#modalEditService").find("input[type=text]").each(function () {
        $(this).val($(row.get(td)).text());
        td++;
    });

    var h4TitleModal = $("#modalEditService").find("h4");
    h4TitleModal.text('Modificant el servei "' + $(btnEdit).data("name") + '"');
    $("#formService").attr("action", contextPath + "/ManagementServlet/menuOption/manageHairdressing/editService");
    $("#idServiceToUpdate").val($(btnEdit).data("id"));
    $("#durationService").val($(btnEdit).data("duration"));
}

function addService() {
    var btnAddModal = $("#modalEditService").find(".modal-footer").children();
    btnAddModal.removeClass("btn-warning");
    btnAddModal.addClass("btn-info");
    btnAddModal.text("Afegir");
    $("#formService").attr("action", contextPath + "/ManagementServlet/menuOption/manageHairdressing/addService");
    $("#durationService").val($("#timePickerService").val());

    var h4TitleModal = $("#modalEditService").find("h4");
    h4TitleModal.text("Afegeix un nou servei");
}

function deleteService(btnDelete) {
    var name = $(btnDelete).data("name");
    var id = $(btnDelete).data("id");

    $("#idServiceToDelete").val(id);
    $("#modalDeleteService").find("h4").text('Eliminar el servei "' + name + '"');
}

function cleanModalClose() {
    $("#nameService").val("");
    $("#nameService").removeClass("is-invalid");
    $("#priceService").val("");
    $("#timePickerService").val(intervalToHHMM(':'));
    $("#btnConfirmAddEditService").attr('disabled', true);
}

function initializeTimepicker() {
    $('input#timePickerService').timepicker({
        timeFormat: 'HH:mm',
        change: putValueToDuration,
        interval: incrementMin,
        minTime: intervalToHHMM(':'),
        maxTime: '06:00',
        defaultTime: intervalToHHMM('_'),
        startTime: intervalToHHMM(':'),
        dynamic: false,
        dropdown: true,
        scrollbar: true
    });
}

function putValueToDuration() {
    $("#durationService").val($("#timePickerService").val());
    validInputs($("#timePickerService"));
}

function intervalToHHMM(separator) {
    var sec_num = incrementMin*60; // don't forget the second param
    var hours = Math.floor(sec_num / 3600);
    var minutes = Math.floor((sec_num - (hours * 3600)) / 60);

    if (hours < 10) {
        hours = "0" + hours;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    return hours + separator + minutes;
}