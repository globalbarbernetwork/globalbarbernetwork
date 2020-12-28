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

    var rows_range1 = $("div[class*='range1-row-']");
    var rows_range2 = $("div[class*='range2-row-']");

    rows_range1.each(function (index) {
        var input1 = $(this).find('input');
        var input2 = $(this).find('input');

        if (input1.val() === '' && input2.val() === '') {
            var checkbox = $(this).prev().find('input').prop("checked", true);
            $(input1).prop("disabled", true);
            $(input1).val(null);
            $(input2).val(null);
        }
    });

    rows_range2.each(function (index) {
        var input1 = $(this).find('input');
        var input2 = $(this).find('input');

        if (input1.val() === '' && input2.val() === '') {
            var checkbox = $(this).prev().find('input').prop("checked", true);
            $(input1).prop("disabled", true);
            $(input1).val(null);
            $(input2).val(null);
        }
    });

    $('input[type=checkbox]').on('change', function () {        
        var id = $(this).attr('id');
        var range_id = id.split('-')[0];
        var day = (id.split('-')[2]);

        var range_start = $('#' + range_id + '-start-' + 'day' + day);
        var range_end = $('#' + range_id + '-end-' + 'day' + day);

        if ($(this).is(":checked")) {
            range_start.prop('disabled', true);
            range_end.prop('disabled', true);
            range_start.val(null);
            range_end.val(null);
        } else {
            range_start.prop('disabled', false);
            range_end.prop('disabled', false);
        }

    });

});


