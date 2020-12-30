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

$(document).ready(function () {
    var errors = $('#errors_field').val();
    if (errors) {
        JSON.parse(errors);
    }

    if (errors[401]) {
        $('#email').addClass("is-invalid");
        $('#password').addClass("is-invalid");
        $('#password').parent().append("<small class='text-danger float-left mt-1 mb-3' style='font-size: 14px !important'>* " + errors[401] + "</small>");
    }

    if (errors[403]) {
        $('#email').addClass("is-invalid");
        $('#email').parent().append("<small class='text-danger float-left mt-1 mb-3' style='font-size: 14px !important'>* " + errors[403] + "</small>");
    }

});


