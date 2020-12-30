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
    initializeDataTables();
});

function initializeDataTables() {
    $('#dataTableServices').DataTable({
        "pagingType": "numbers",
        "order": [],
        "columnDefs": [{orderable: false, targets: [0, 4]}],
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Catalan.json"
        }
    });

    $('#dataTable').DataTable({
        "pagingType": "numbers",
        "responsive": true,
        "order": [],
        "columnDefs": [{orderable: false, targets: [0, 7]}],
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Catalan.json"
        }
    });

    $('#holidayDatatable').DataTable({
        "pagingType": "numbers",
        "responsive": true,
        "searching": false,
        columnDefs: [
            {orderable: false, targets: 0}
        ],
        order: [],
        "language": {
            "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Catalan.json"
        }
    });
}