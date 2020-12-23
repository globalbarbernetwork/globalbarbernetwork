$(document).ready(function () {
    contextPath = $("#contextPath").val();
    type = $('#type').val();
    uid = $('#uid').val();

    var newPassword;
    console.dir($('.swal2-confirm'));
    $("#changePassword").on("click", function () {
        var sweetAlert = SweetAlert.fire({
            showConfirmButton: true,
            showCancelButton: true,
            cancelButtonText: 'Cancelar',
            confirmButtonText: 'Canviar',
            Stitle: 'Multiple inputs',
            html: '<h4>Canviar contrasenya</h4><br><div class="input-group mb-3"><div class="input-group-prepend"><div class="input-group-text"><i class="fas fa-key"></i></div>\n\
                    </div><input id="actualPassword" name="actualPassword" type="password" placeholder="Contrasenya actual" class="form-control" value=""></div></div>\n\
                    <div class="input-group"><div class="input-group-prepend"><div class="input-group-text"><i class="fas fa-key"></i></div>\n\
                    </div><input id="newPassword" name="newPassword" required type="password" placeholder="Contrasenya nova" class="form-control" value=""></div>',
            focusConfirm: false,
        });
        $('.swal2-confirm').prop("disabled", true);
        sweetAlert.then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    type: "POST",
                    url:  contextPath + "/ManagementServlet/menuOptions/editProfile/changePassword",
                    data: {
                        newPassword: newPassword,                        
                    },
                    success: function () {
                        window.location.replace(contextPath + "/ManagementServlet/");
                    },
                    error: function () {
                        alert('error');
                    }
                });
            }
        })
    });
    $(document).on('change', "#actualPassword", function () {
        $.ajax({
            type: "POST",
            url: "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyCdxvdJ6PudEb3bAF7rFrwdTyJHck88bfg",
            data: {
                email: $('#email').val(),
                password: $(this).val()
            },
            success: function (data) {
                $("#actualPassword").addClass("is-valid");
                $("#actualPassword").removeClass("is-invalid");
                if ($('#newPassword').val() !== "") {
                    $('.swal2-confirm').prop("disabled", false);
                }
            },
            error: function (data) {
                $('.swal2-confirm').prop("disabled", true);
                $("#actualPassword").addClass("is-invalid");
            }
        });
    });

    $(document).on('keyup', '#newPassword', function () {
        newPassword = $(this).val();
        if (newPassword === "") {
            $('.swal2-confirm').prop("disabled", true);
        } else if (newPassword.length < 6) {
            $('.swal2-confirm').prop("disabled", true);
        } else {
            $('.swal2-confirm').prop("disabled", false);
        }
    }
    );
});