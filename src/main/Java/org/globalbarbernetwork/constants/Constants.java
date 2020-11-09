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
package org.globalbarbernetwork.constants;

/**
 *
 * @author Grup 3
 */
public class Constants {

    public static final String POST = "POST";
    public static final String LOGIN_JSP = "login.jsp";
    public static final String REGISTER_JSP = "register.jsp";
    public static final String REGISTER_HAIRDRESSING_JSP = "registerHairdressing.jsp";

    // Email Verified
    public static String BODY_MAIL_VERIFICATION = "Hola, %DISPLAY_NAME%,\n"
            + "\n"
            + "Segueix aquest enllaç per verificar la teva adreça electrònica.\n"
            + "\n"
            + "%LINK_VERIFICATION%\n"
            + "\n"
            + "Si no has demanat verificar aquesta adreça, pots ignorar aquest correu.\n"
            + "\n"
            + "Gràcies,\n"
            + "\n"
            + "Global Barber Network";

    public static final String SUBJECT_MAIL_VERIFICATION = "Verifica l'adreça electrònica de l'aplicació Global Barber Network";
}
