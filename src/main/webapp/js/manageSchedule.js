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

    $('input[type=checkbox]').on('change', function () {
        var id = $(this).attr('id');
        var id_splited = id.split('-');
        var range_id = id.split('-')[0];
        var day = (id.split('-')[2]);

        // range1-row-1

        if ($(this).is(":checked")) {            
            $('#'+range_id+'-start-'+'day'+day).prop('disabled', true);
            $('#'+range_id+'-end-'+'day'+day).prop('disabled', true);
        }else{
            $('#'+range_id+'-start-'+'day'+day).prop('disabled', false);
            $('#'+range_id+'-end-'+'day'+day).prop('disabled', false);
        }

    });

});


