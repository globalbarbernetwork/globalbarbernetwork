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

$(document).ready(function() {
    renderMap();
    loadHairdressings();
});

var map;
function renderMap () {
    if (!mapboxgl.supported()) {
        alert('El teu navegador no suporta Mapbox GL');
    } else {
        var lat = 40.2;
        var lng = -3.7;

        // Config mapa
        mapboxgl.accessToken = 'pk.eyJ1IjoiZ2xvYmFsLWJhcmJlci1uZXR3b3JrIiwiYSI6ImNrZ2R1MWZneDBtZGwycW83aHU0anZ5MmMifQ.WXb5-N15u4z2pcL-qKR3ig';
        map = new mapboxgl.Map({
            container: 'map',
            style: 'mapbox://styles/global-barber-network/ckh6xvale0gy719mw6jxuvgjy', // Style aplicado al mapa
            center: [lng, lat], // Posición inicial [lng, lat]
            zoom: 5.7 // Zoom inicial
        });
        
        // Botón de geolocalización del cliente
        map.addControl(
            new mapboxgl.GeolocateControl({
                positionOptions: {
                    enableHighAccuracy: true // Indica si deseamos recibir la posición más precisa posible con conseqüencia de consumir más recursos.
                },
                trackUserLocation: true
            })
        );
    }
}

function loadHairdressings() {
    var hairdressingsJSON = JSON.parse($("#listHairdressingsJSON").val());
    var hairdressingsJSONArray = hairdressingsJSON.jsonArray;
    var jsonFeatures = [];
    for (var i in hairdressingsJSONArray) {
        var json = '{"type":"Feature", "properties":{"companyName":"'+ hairdressingsJSONArray[i].companyName +'",';
        json += '"UID":"' + hairdressingsJSONArray[i].UID + '",';
        json += '"description":"' + hairdressingsJSONArray[i].description + '",';
        json += '"urlInstagram":"'+ hairdressingsJSONArray[i].instagram +'",';
        json += '"icon":"scissors"},';
        json += '"geometry":{"type":"Point", "coordinates":['+ hairdressingsJSONArray[i].coordinates.lng +','+ hairdressingsJSONArray[i].coordinates.lat +']}}';
        jsonFeatures[i] = JSON.parse(json);
    }
    
    map.on('load', function () {
        map.addSource('places', {
            'type': 'geojson',
            'data': {
            'type': 'FeatureCollection',
            'features': jsonFeatures
            }
        });
                
        map.addLayer({
            'id': 'places',
            'type': 'symbol',
            'source': 'places',
            'layout': {
                'text-field':'{companyName}',
                'text-variable-anchor': ['top', 'bottom', 'left', 'right'],
                'text-radial-offset': 1,
                'text-justify': 'auto',
                'text-size': 10,
                'icon-image': '{icon}-15',
                'icon-allow-overlap': true
            }
        });
        
        map.on('click', 'places', function (e) {
            var coordinates = e.features[0].geometry.coordinates.slice();

            // Ensure that if the map is zoomed out such that multiple
            // copies of the feature are visible, the popup appears
            // over the copy being pointed to.
            while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
                coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
            }

            // Config popup
            var markerHeight = 10, markerRadius = 10, linearOffset = 25;
            var popupOffsets = {
                'top': [0, 0],
                'top-left': [0,0],
                'top-right': [0,0],
                'bottom': [0, -markerHeight],
                'bottom-left': [linearOffset, (markerHeight - markerRadius + linearOffset) * -1],
                'bottom-right': [-linearOffset, (markerHeight - markerRadius + linearOffset) * -1],
                'left': [markerRadius, (markerHeight - markerRadius) * -1],
                'right': [-markerRadius, (markerHeight - markerRadius) * -1]
            };
            
            // Variables hairdressing a mostrar en Popup
            var companyName = e.features[0].properties.companyName;
            var UID = e.features[0].properties.UID;
            var description = e.features[0].properties.description;
            var urlInstagram = e.features[0].properties.urlInstagram;
            
            // Llamada ajax para obtener horarios de apertura de la peluqueria
            $.ajax({
                url: 'ManagementServlet/schedule/timetable',
                data: {
                    uidHairdressing: UID
                },
                success: function(data) {
                    var horari = "";
                    var horari2 = "<div class='collapse' id='collapseOtherDays'> <p>";
                    
                    var date = new Date();
                    var dayOfWeek = date.getDay() !== 0 ? date.getDay() : 7;
                    if (data !== "") {
                        var timetableJSONArray = JSON.parse(data).jsonArray[0];
                        var restartCount = true;
                        var totalDays = 7;
                        for (var i = dayOfWeek; i <= totalDays; i++) {
                            if (dayOfWeek === i) {
                                horari += "<div class='dropdown-toggle' data-toggle='collapse' href='#collapseOtherDays' aria-expanded='false' aria-controls='collapseOtherDays'>";
                                if (i === 7) {
                                    horari += "<strong>" + timetableJSONArray[0] + "</strong>";
                                } else {
                                    horari += "<strong>" + timetableJSONArray[i] + "</strong>";
                                }
                                horari += "</div>";
                            } else {
                                if (i === 7) {
                                    horari2 += timetableJSONArray[0] + "<br>";
                                } else {
                                    horari2 += timetableJSONArray[i] + "<br>";
                                }
                            }

                            if (restartCount && i === totalDays) {
                                i = 0;
                                totalDays = dayOfWeek - 1;
                                restartCount = false;
                            }
                        }
                    }
                    horari2 += "</p> </div>";
                    
                    // HTML del Popup
                    var HTMLPopup = "<strong style='text-align:center'>" + companyName + "</strong>";
                    HTMLPopup += "<p>" + description + "</p>";
                    HTMLPopup += "<p><a href=" + urlInstagram + " target='_blank' title='Obre en una nova finestra'>Instagram</a></p>";
                    if(horari !== "") HTMLPopup += "Horari:<br>" + horari + horari2;
                    HTMLPopup += "<button id='reserva' onclick='modalReserve(this);' type='button' class='btn btn-success' data-uid='" + UID + "' data-toggle='modal' data-target='#modalReserve'>Fer una reserva</button>";

                    new mapboxgl.Popup({offset: popupOffsets})
                            .setLngLat(coordinates)
                            .setHTML(HTMLPopup)
                            .setMaxWidth("250px")
                            .addTo(map);
                },
                error: function() {
                    console.log("No se ha podido obtener la información");
                }
            });
        });

        // Cambia el cursor a puntero cuando se pasa por encima del icono de peluqueria.
        map.on('mouseenter', 'places', function () {
            map.getCanvas().style.cursor = 'pointer';
        });

        // Cambia el puntero a cursor cuando se sale del icono.
        map.on('mouseleave', 'places', function () {
            map.getCanvas().style.cursor = '';
        });
    });
}

function modalReserve(btn) {
    //alert($(btn).data("uid"));
    
}