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
public class IndexManager implements Manager {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) {
        try {
            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            // Recoger lista peluquerias
            rd.forward(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(IndexManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IndexManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
