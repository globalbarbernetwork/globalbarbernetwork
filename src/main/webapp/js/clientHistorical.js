/* 
 * Copyright (C) 2023 arnau
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
    //Cuando se oculte el collapse, se cambia el icono que representa que está cerrado.
    $(".collapse").on('hidden.bs.collapse', function(){
        $(this).prev().find('i').removeClass('fa fa-angle-down').addClass('fa fa-angle-right');
    });
    
    //Cuando se muestre el collapse, se cambia el icono que representa que está abierto.
    $(".collapse").on('show.bs.collapse', function(){
        $(this).prev().find('i').removeClass('fa fa-angle-right').addClass('fa fa-angle-down');
    });
});