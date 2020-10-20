/*
 * Este JS carga el mapa con las localizacion del dispositivo y las peluquerias guardados en BBDD
 */

var map;

renderMap();

function renderMap (){
    var lat = 40.2;
    var long = -3.7;

    navigator.geolocation.getCurrentPosition(success, error, options);           

    mapboxgl.accessToken = 'pk.eyJ1IjoiZ2xvYmFsLWJhcmJlci1uZXR3b3JrIiwiYSI6ImNrZ2R1MWZneDBtZGwycW83aHU0anZ5MmMifQ.WXb5-N15u4z2pcL-qKR3ig';
    map = new mapboxgl.Map({
        container: 'map',
        style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location
        center: [long, lat], // starting position [lng, lat]
        zoom: 4.2 // starting zoom
    });

    var options = {
        enableHighAccuracy: true, // Mejora la presicion si el dispositivo lo permite. 
        timeout: 5000, // Tiempo que tiene para buscar la posicion actual.
        maximumAge: 0 // Define si se coge o no la posicion del cache. 0 = No cogera la posicion de cache y te calculara la actual. 1 = Cogerá la posicion de cache
    };        
    
    showHairDressing();

}

function showHairDressing (){
    // ¿Que peluqerias me ha devuelto la gestoria?
    // Las printaré
    // Foreach peluqeruia jsp:peluqeria
}

function success(pos) {
    var crd = pos.coords;

    lat =  crd.latitude;
    long = crd.longitude;

    map.jumpTo({'center': [long, lat], 'zoom': 12 });
};

function error(err) {
    console.warn('ERROR(' + err.code + '): ' + err.message);
};
          

