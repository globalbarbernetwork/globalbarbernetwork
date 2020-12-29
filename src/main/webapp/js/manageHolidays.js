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

$(document).ready(function () {
    initializeDatepickerHairdressingHolidays();
    stylizeDatePicker("datepickerHairdressingHolidays");
    loadHairdressingHolidays();
});

function initializeDatepickerHairdressingHolidays() {

    $('#datepickerHairdressingHolidays').datepicker({
        language: "ca",
        format: "dd/mm/yyyy",
        //daysOfWeekDisabled: [0, 6],
        todayHighlight: true,
        daysOfWeekHighlighted: [0, 6],
        clearBtn: true,
        autoclose: true,
        multidate: true
    });

    $('#datepickerHairdressingHolidays').on('changeDate', function () {
        $('#selectedHairdressingHolidaysDates').val($('#datepickerHairdressingHolidays').datepicker('getFormattedDate'));
    });

}

function stylizeDatePicker(idDatepicker) {
    var thead = $('#' + idDatepicker).find('thead:first').children('tr').eq(1);
    thead.addClass(" thead-hairdressing-schedule ");
    $(thead).children('th').addClass(" th-hairdressing-schedule ");
}

function loadHairdressingHolidays() {
    var data = JSON.parse($('#holidays').val());
    $("#datepickerHairdressingHolidays").datepicker('setDates', data);
    $("#datepickerHairdressingHolidays").datepicker('_setDate', new Date(), 'view');
}