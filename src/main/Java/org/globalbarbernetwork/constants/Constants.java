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
    public static final String EDIT_CLIENT_JSP = "editClient.jsp";
    public static final String EDIT_HAIRDRESSING_JSP = "editHairdressing.jsp";
    public static final String MANAGE_HAIRDRESSING_JSP = "manageHairdressing.jsp";
    public static final String HISTORICAL_CLIENT_JSP = "clientHistorical.jsp";
    public static final String MANAGE_RESERVES_JSP = "manageReserves.jsp";
    public static final Integer INCREMENT_MINUTES = 30;
    public static final String STATE_PENDING = "P";
    public static final String STATE_COMPLETED = "C";
    public static final String STATE_ANNULLED = "A";

    // Email Verified
    public static final String SUBJECT_MAIL_VERIFICATION = "Verifica l'adreça electrònica de l'aplicació Global Barber Network";
    
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

    
    public static final String PATH_PROPERTIES = "C:/tmp/config.properties";
    
    // Email reserve confirmation
    public static final String SUBJECT_MAIL_RESERVE_CONFIRMATION = "Confirmación de la reserva en %COMPANY_NAME%";
    
    public static String BODY_MAIL_RESERVE_CONFIRMATION = "Hola, %DISPLAY_NAME%,"
            + "<br/><br/>"
            + "La teva reserva a %COMPANY_NAME% esta confirmada."
            + "<br/><br/>"
            + "<b><u>Detalls de la reserva</u></b>"
            + "<br/>"
            + "Data i hora: %TIME_INIT%"
            + "<br/>"
            + "Servei: %SERVICE_NAME%"
            + "<br/>"
            + "Empleat: %EMPLOYEE_NAME%"
            + "<br/>"
            + "Ubicació: %HAIRDRESSING_ADDRESS% "
            + "<br/><br/>"
            + "Gràcies per utilitzar el nostre servei.";
}
