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
package org.globalbarbernetwork.managers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.globalbarbernetwork.interfaces.Manager;

/**
 *
 * @author Grup 3
 */
public class AccessManager implements Manager{
    final static String LOGIN = "login";
    final static String REGISTER = "register";
    final static String REGISTER_HAIRDRESSING = "registerHairdressing";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {
        RequestDispatcher rd = null;
        
        switch(action) {
            case LOGIN:
                rd = request.getRequestDispatcher("/login.jsp");
                break;
            case REGISTER:
                rd = request.getRequestDispatcher("/register.jsp");
                break;
            case REGISTER_HAIRDRESSING:
                rd = request.getRequestDispatcher("/registerHairdressing.jsp");
                break;
        }
        
        try {
            if (rd != null) {
                rd.forward(request, response);
            }
        } catch (ServletException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AccessManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
