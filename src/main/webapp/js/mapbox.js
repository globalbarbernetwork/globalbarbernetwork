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

/*
 * Este JS carga el mapa con las localizacion del dispositivo y las peluquerias guardados en BBDD
 */
$(document).ready(function() {
    renderMap();
});

var map;
function renderMap (){
    var lat = 40.2;
    var long = -3.7;

    navigator.geolocation.getCurrentPosition(success, error, options);           

    mapboxgl.accessToken = 'pk.eyJ1IjoiZ2xvYmFsLWJhcmJlci1uZXR3b3JrIiwiYSI6ImNrZ2R1MWZneDBtZGwycW83aHU0anZ5MmMifQ.WXb5-N15u4z2pcL-qKR3ig';
    map = new mapboxgl.Map({
        container: 'map',
        style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location
        center: [long, lat], // starting position [lng, lat]
        zoom: 5.7 // starting zoom
    });

    var options = {
        enableHighAccuracy: true, // Mejora la presicion si el dispositivo lo permite. 
        timeout: 5000, // Tiempo que tiene para buscar la posicion actual.
        maximumAge: 0 // Define si se coge o no la posicion del cache. 0 = No cogera la posicion de cache y te calculara la actual. 1 = Cogerá la posicion de cache
    };        
    
    //Añadir un marker
    /*var marker = new mapboxgl.Marker()
        .setLngLat([-3.7, 40.2])
        .addTo(map);*/


    map.on('load', function () {
        map.addSource('places', {
            'type': 'geojson',
            'data': {
            'type': 'FeatureCollection',
            'features': [
                            {
                                'type': 'Feature',
                                'properties': {
                                    'description': '<strong>Monclús Barber Shop</strong><p><a href="https://www.instagram.com/monclus_barbershop/" target="_blank" title="Obre en una nova finestra">Instagram Monclús Barber Shop</a></p><p>Horari -> 10:00-20:00 p.m.</p>',
                                    'icon': 'theatre'
                                },
                                'geometry':{
                                    'type':'Point',
                                    'coordinates': [2.156955, 41.502085]
                                }
                            }
                        ]
            }
        });
        
        map.addLayer({
            'id': 'places',
            'type': 'symbol',
            'source': 'places',
            'layout': {
                'icon-image': '{icon}-15',
                'icon-allow-overlap': true
            }
        });
        
        map.on('click', 'places', function (e) {
            var coordinates = e.features[0].geometry.coordinates.slice();
            var description = e.features[0].properties.description;

            // Ensure that if the map is zoomed out such that multiple
            // copies of the feature are visible, the popup appears
            // over the copy being pointed to.
            while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
            coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
            }

            new mapboxgl.Popup()
            .setLngLat(coordinates)
            .setHTML(description)
            .addTo(map);
        });

        // Change the cursor to a pointer when the mouse is over the places layer.
        map.on('mouseenter', 'places', function () {
            map.getCanvas().style.cursor = 'pointer';
        });

        // Change it back to a pointer when it leaves.
        map.on('mouseleave', 'places', function () {
            map.getCanvas().style.cursor = '';
        });
        
    });

    
    
    showHairDressing();

}

function success(pos) {
    var crd = pos.coords;

    lat =  crd.latitude;
    long = crd.longitude;

    map.jumpTo({'center': [long, lat], 'zoom': 14 });
};

function error(err) {
    console.warn('ERROR(' + err.code + '): ' + err.message);
};

function showHairDressing (){
    // ¿Que peluqerias me ha devuelto la gestoria?
    // Las printaré
    // Foreach peluqeruia jsp:peluqeria
}

