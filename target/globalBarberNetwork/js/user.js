$(document).on('click', '[class*="fusubmit"]', function (event) {
    event.preventDefault();
    
   
    var servletURL = "user?action=newUser";
//&user=" + $("#user").val()+"&name=" + $("#name").val()+"&lastname=" + $("#lastname").val();
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            if(data.resposta == "OK"){
                alert("TOT CORRECTE");
            }else{
                alert(data.resposta);
            }                        
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});