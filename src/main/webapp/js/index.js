/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var servletURL = "index?action=loadPage";
    $.ajax({
        type: "GET",
        crossDomain: true,
        async: true,
        url: servletURL,
        success: function (data) {
            console.dir(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});

