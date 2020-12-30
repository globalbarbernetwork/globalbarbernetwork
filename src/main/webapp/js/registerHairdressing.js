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
let accessToken = 'pk.eyJ1IjoiZ2xvYmFsLWJhcmJlci1uZXR3b3JrIiwiYSI6ImNrZ2R1MWZneDBtZGwycW83aHU0anZ5MmMifQ.WXb5-N15u4z2pcL-qKR3ig';

$(document).ready(function () {
    $("#zipCode").blur(function () {
        var zipCode = $(this).val();
        if (zipCode !== null && zipCode !== '' && !$(this).hasClass("is-invalid")) {
            var url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + zipCode + '.json?access_token=' + accessToken + '&limit=1&country=ES&language=ca';
            findPlaceByZipcode(url);
        } else {
            putValuesInFields(false);
        }
    });

    $("#btnRegister").click(function (event) {
        event.preventDefault();
        var zipCode = $("#zipCode").val();
        var address = $("#address").val();
        var city = $("#city").val();

        var url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + zipCode + "," + city + "," + address + '.json?access_token=' + accessToken + '&limit=1&country=ES&language=ca';
        confirmLocalization(url);
    });
});



function getJSON(url, callback) {

    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.responseType = 'json';

    xhr.onload = function () {
        var status = xhr.status;
        if (status == 200) {
            callback(null, xhr.response);
        } else {
            callback(status);
        }
    };
    xhr.send();
}

function findPlaceByZipcode(url) {
    getJSON(url, function (err, data) {
        if (err != null) {
            console.log(err);
        } else {
            if (data !== null && data.features != '') {
                getValuesFromJSON(data);
            } else {
                console.log("Este zipcode no devuelve registros");
            }
        }
    });
}

function getValuesFromJSON(data) {
    var contextSize = data.features[0].context.length.toString();
    var values = null;
    switch (contextSize) {
        case "3":
            values = [data.features[0].context[0].text, data.features[0].context[1].text, data.features[0].context[2].text];
            break;
        case "4":
            values = [data.features[0].context[1].text, data.features[0].context[2].text, data.features[0].context[3].text];
            break;
    }
    if (values != null) {
        putValuesInFields(values, true);
    }
}

function putValuesInFields(values, putValues) {
    if (putValues) {
        $("#city").val(values[0]);
        $("#province").val(values[1]);
        $("#country").val(values[2]);
        
        $("#cityHidden").val(values[0]);
        $("#provinceHidden").val(values[1]);
        $("#countryHidden").val(values[2]);
    } else {
        $("#city").val("");
        $("#province").val("");
        $("#country").val("");
        
        $("#cityHidden").val("");
        $("#provinceHidden").val("");
        $("#countryHidden").val("")
    }
}


function confirmLocalization(url) {
    getJSON(url, function (err, data) {
        if (err != null) {
            console.log(err);
        } else {
            addInfoInModal(data);
        }
    });
}

function addInfoInModal(data) {
    var isAddress = data !== null && data.features !== '' && data.features[0].place_type[0] === "address";
    var text;
    if (isAddress) {
        text = "<p>" +
                "Hem localitzat la teva perruqueria a :</br>" +
                "<h5>" + data.features[0].place_name + "</h5></br>" +
                "Es correcte ?</br>" +
                "En el cas de no ser-ho corretgeix l'adreça." +
                "</p>";
        $("#btnConfmodal").attr('type', 'submit');
        $("#btnConfmodal").removeAttr('data-dismiss');
        $("#coordHairdressing").val(data.features[0].geometry.coordinates[0] + "," + data.features[0].geometry.coordinates[1]);
    } else {
        text = "<p>" +
                "<h5>No hem localitzat la teva perruqueria</h5></br>" +
                "Si us plau, corretgeix l'adreça i/o introdueix mès informació." +
                "</p>";
        $("#btnConfmodal").attr('type', 'button');
        $("#btnConfmodal").attr('data-dismiss', 'modal');
        $("#coordHairdressing").val("");
    }

    $(".modal-body").html(text);
}