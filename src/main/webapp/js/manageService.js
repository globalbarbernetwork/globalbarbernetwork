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

    $("#btnConfirmAddEditService").attr('disabled', true);

    initializeTimepicker();

    $("#btnAddService").click(function () {
        addService();
    });

    $("#modalEditService").on('hidden.bs.modal', function () {
        cleanModalClose();
    });

    $("#nameService").blur(function () {
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
    } else if (input.attr('name') === "timePickerService"){
        if (inputValue === '') {
            allInputsOk = false;
        } else {
            allInputsOk = true;
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
    $("#nameService").val("");
    $("#timePickerService").val("00:15");
    $("#btnConfirmAddEditService").attr('disabled', true);
}

function initializeTimepicker() {
    $('input#timePickerService').timepicker({
        timeFormat: 'HH:mm',
        change: putValueToDuration,
        interval: 15,
        minTime: '00:15',
        maxTime: '06:00',
        defaultTime: '00_15',
        startTime: '00:15',
        dynamic: false,
        dropdown: true,
        scrollbar: true
    });
}

function putValueToDuration() {
    $("#durationService").val($("#timePickerService").val());
    validInputs($("#timePickerService"));
}