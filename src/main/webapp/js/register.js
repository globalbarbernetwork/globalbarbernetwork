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
    var allInputsOk = false;
    var passwOk = false;

    $("input").blur(function () {

        if ($(this).attr('id') !== "terms") {
            if ($(this)[0].validity.valid) {
                $(this).removeClass("is-invalid");
            } else {
                $(this).addClass("is-invalid");
            }
        }

        passwOk = checkPasswords();
        controlBtnRegister(allInputsOk, passwOk);
        msgErrorInputs($(this));
    });

    $("#terms").change(function () {
        controlBtnRegister(allInputsOk, passwOk);
    });

    $(".show_hide_password i").on('click', function (event) {
        event.preventDefault();
        showHidePasswords($(this));
    });
});

//Funcion para controlar los diferentes mensajes de error que mostraremos por pantalla
function msgErrorInputs(input) {
    var value = input.val();
    var pattern = new RegExp(input[0].pattern);
    var name = input.attr('name');
    var id = input.attr('id');

    if (id !== "password" && id !== "confirmPassword" && id !== "terms") {
        if (value === "") {
            input.next().text("* Aquest camp es obligatori i no pot estar buit.");
        } else if (pattern !== null && !pattern.test(value)) {
            if (name === "email") {
                input.next().text("* Aquest format de correu no es correcte");
            } else {
                input.next().text("* Aquest format de telefon no es correcte, ha de tenir 9 numeros.");
            }
        } else {
            input.next().text("");
        }
    }
}

//Funcion para controlar si las contraseñas son iguales
function checkPasswords() {
    var pass = $("#password").val();
    var confPass = $("#confirmPassword").val();

    if (!pass.length == 0 && !confPass.length == 0) {
        if (pass !== confPass) {
            $("#confirmPassword").addClass('is-invalid');
            $("#password").addClass('is-invalid');
            $("#passwordHelp").removeAttr('hidden');
            $("#passwordHelp").text("Les contrasenyes no coincideixen");
            return false;
        } else {
            $("#confirmPassword").removeClass('is-invalid');
            $("#password").removeClass('is-invalid');
            $("#passwordHelp").attr('hidden', true);
            return true;
        }
    }
    return false;
}

//Funcion para mostrar u ocultar las contraseñas mediante el ojo
function showHidePasswords(input) {
    var inputPass = input.parents(".show_hide_password").children().first();

    if (inputPass.attr("type") === "text") {
        inputPass.attr('type', 'password');
        input.addClass("fa-eye-slash");
        input.removeClass("fa-eye");
    } else if (inputPass.attr("type") === "password") {
        inputPass.attr('type', 'text');
        input.removeClass("fa-eye-slash");
        input.addClass("fa-eye");
    }
}

//Funcion para activar o desactivar el boton de registrar
function controlBtnRegister(allInputsOk, passwOk) {
    allInputsOk = $("input:invalid").length == 0 && !$("input").hasClass("is-invalid") && $("#terms").prop('checked');

    if (allInputsOk && passwOk) {
        $("#btnRegister").removeAttr('disabled');
    } else {
        $("#btnRegister").attr('disabled', true);
    }
}
