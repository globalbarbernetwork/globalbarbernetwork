/*
 * Copyright (C) 2020 Adrian
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
package org.globalbarbernetwork.bo;

import javax.servlet.http.HttpServletRequest;
import org.globalbarbernetwork.entities.Client;
import org.globalbarbernetwork.entities.Employee;
import org.globalbarbernetwork.entities.Hairdressing;
import org.globalbarbernetwork.entities.Service;
import org.globalbarbernetwork.entities.User;
import org.globalbarbernetwork.firebase.FirebaseDAO;

/**
 *
 * @author Grup 3
 */
public class ReserveBO {

    private final FirebaseDAO firebaseDAO = new FirebaseDAO();

    public ReserveBO() {
    }

    public Hairdressing getHairdressing(String uid) {
        return firebaseDAO.getHairdressing(uid);
    }

    public Service getService(String uidHairdressing, String uidService) {
        return firebaseDAO.getServiceById(uidHairdressing, uidService);
    }
    
    public Employee getEmployee(String hairdressing, String dni){
        return firebaseDAO.getEmployeeByIDNumber(hairdressing, dni);
    }
    
}
