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

document.addEventListener('DOMContentLoaded', function () {
    var optionsBusinessHours = getOptionsBusinessHours();
    var reservesEvents = getReservesEvents();
    
    initializeFullCalendar(optionsBusinessHours, reservesEvents);
});

function initializeFullCalendar(optionsBusinessHours, reservesEvents) {
    var fullCalendarReserves = document.getElementById('fullcalendarReserves');
    var calendar = new FullCalendar.Calendar(fullCalendarReserves, {
        //fixedWeekCount: false //Dependiendo del mes salen mas rows de semanas o no
        //showNonCurrentDates: false //Se ocultan los dias de otros meses en la vista month
        height: 600,
        locale: 'ca',
        initialView: 'dayGridMonth', // dayGridWeek
        headerToolbar: {start: 'dayGridMonth,timeGridWeek', center: 'title'},
        views: {
            timeGridWeek: {// name of view
                titleFormat: {year: 'numeric', month: '2-digit', day: '2-digit'},
                dayHeaderFormat: {weekday: 'long', day: 'numeric'},
                weekNumberFormat: {week: 'short'},
                nowIndicator: true
            }
        },
        dayHeaderFormat: {weekday: 'long'},
        weekNumbers: true,
        weekNumberFormat: {week: 'numeric'},
        businessHours: optionsBusinessHours,
        events: reservesEvents,
        eventTimeFormat: {
            hour: 'numeric',
            minute: '2-digit'
        }
    });

    calendar.render();
}

function getOptionsBusinessHours() {
    var businessHoursJSON = JSON.parse($("#businessHoursJSON").val());
    var businessHoursJSONArray = businessHoursJSON.jsonArray;
    
    var jsonArrayBusinessHours = [];
    if (businessHoursJSONArray != undefined) {
        for (var i in businessHoursJSONArray) {
            var json = '{"daysOfWeek":[' + businessHoursJSONArray[i].dayOfWeek + '],';
            json += '"startTime":"' + businessHoursJSONArray[i].rangeHourInit + '",';
            json += '"endTime":"' + businessHoursJSONArray[i].rangeHourEnd + '"}';

            jsonArrayBusinessHours[i] = JSON.parse(json);
        }
    }
    
    return jsonArrayBusinessHours;
}

function getReservesEvents() {
    var reservesEventsJSON = JSON.parse($("#reservesEventsJSON").val());
    var reservesEventsJSONArray = reservesEventsJSON.jsonArray;
    
    console.log(reservesEventsJSONArray);
    
    var jsonArrayReservesEvents = [];
    if (reservesEventsJSONArray != undefined) {
        for (var i in reservesEventsJSONArray) {
            var json = '{"title":"' + reservesEventsJSONArray[i].title + '",';
            json += '"start":"' + reservesEventsJSONArray[i].startDateTime + '",';
            json += '"end":"' + reservesEventsJSONArray[i].endDateTime + '"}';

            jsonArrayReservesEvents[i] = JSON.parse(json);
        }
    }
    
    return jsonArrayReservesEvents;
}