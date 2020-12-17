<%-- 
    Document   : manageWorkers
    Created on : Dec 6, 2020, 5:15:35 PM
    Author     : Grup 3
--%>

<div id="table-responsive">
    <table id="dataTable" class="table table-striped" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th class="th-sm centerButton w7">
                    <button id="btnAdd" type="button" data-toggle="modal" data-target="#modalEditEmployee" class="btn btn-info btn-rounded btn-sm"><i class="fas fa-user-plus"></i></button>
                </th>
                <th class="th-sm w10">Nom</th>
                <th class="th-sm w25">Cognoms</th>
                <th class="th-sm w7">DNI</th>
                <th class="th-sm w5">Edat</th>
                <th class="th-sm w35">Direcci�</th>
                <th class="th-sm w10">Tel�fon M�bil</th>
                <th class="th-sm w5"></th>
            </tr>
        </thead>
        <tbody>
        <c:set var="contador" value="${0}"/>
        <c:forEach items="${employees}" var="employee">
            <tr>
                <td class="inline w7 centerButton">
                    <button id="btnEdit${contador}" class="btn btn-primary btn-sm" title="Editar dades" data-title="Edit" data-toggle="modal" data-target="#modalEditEmployee" data-name="${employee.name}" data-surname="${employee.surname}" data-idnumber="${employee.idNumber}" onclick="editEmployee(this)"><span class="fal fa-pencil"></span></button>
                    <button id="btnHolidays${contador}" class="btn btn-warning btn-sm" title="Gestionar de vacances" data-title="Manage" data-toggle="modal" data-target="#modalManageHolidaysEmployee"><i class="fal fa-calendar-alt"></i></button>
                </td>
                <td class="w10">${employee.name}</td>
                <td class="w25">${employee.surname}</td>
                <td class="w7">${employee.idNumber}</td>
                <td class="w5">${employee.age}</td>
                <td class="w30">${employee.address}</td>
                <td class="w10">${employee.phoneNumber}</td>
                <td class="centerButton w5">
                    <button id="btnDelete${contador}" class="btn btn-danger btn-sm" title="Esborrar treballador" data-title="Delete" data-toggle="modal" data-target="#modalDeleteEmployee" data-name="${employee.name}" data-surname="${employee.surname}" data-idnumber="${employee.idNumber}" onclick="deleteEmployee(this)"><span class="fal fa-trash"></span></button>
                </td>
            <c:set var="contador" value="${contador + 1}" />
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <input type="hidden" id="contextPath" name="contextPath" value="${contextPath}">
</div>


<div class="modal fade" id="modalEditEmployee" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="Heading"></h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="fas fa-times" aria-hidden="true"></span></button>
            </div>
            <form id="formEmployee" method="post">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="name">Nom</label>
                        <input class="form-control" type="text" id="name" name="name" placeholder="Nom" title="Omple aquest camp">
                    </div>
                    <div class="form-group">
                        <label for="surname">Cognoms</label>
                        <input class="form-control" type="text" id="surname" name="surname" placeholder="Cognoms" title="Omple aquest camp">
                    </div>
                    <div class="form-group">
                        <label for="idNumber">N�mero Identitat</label>
                        <input class="form-control" type="text" id="idNumber" name="idNumber" placeholder="DNI o NIE" title="Omple aquest camp">
                        <small id="errorInputIdNumber" class="text-danger"></small>
                        <input type="hidden" id="idNumberEmployeeToEdit" name="idNumberEmployeeToEdit">
                    </div>
                    <div class="form-group">
                        <label for="age">Edat</label>
                        <input class="form-control" type="text" id="age" name="age" placeholder="Edat" title="Omple aquest camp">
                    </div>
                    <div class="form-group">
                        <label for="address">Direcci�</label>
                        <input class="form-control" type="text" id="address" name="address" placeholder="Direcci�" title="Omple aquest camp">
                    </div>
                    <div class="form-group">
                        <label for="phoneNumber">Tel�fon M�bil</label>
                        <input class="form-control" type="text" id="phoneNumber" name="phoneNumber" placeholder="Tel�fon M�bil" title="Omple aquest camp">
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="btnConfirmAddEdit" type="submit" class="btn btn-lg" style="width:100%;"/>
                </div>
            </form>
        </div>
    </div>
</div>



<div class="modal fade" id="modalDeleteEmployee" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title custom_align" id="Heading"></h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="fas fa-times" aria-hidden="true"></span></button>
            </div>
            <div class="modal-body">
                <div class="alert alert-danger"><i class="fas fa-exclamation-triangle"></i> Estas segur que vols eliminar aquest treballador?</div>
            </div>
            <div class="modal-footer">
                <form method="post" action="${contextPath}/ManagementServlet/menuOption/manageHairdressing/deleteEmployee">
                    <input type="hidden" id="idNumberEmployeeToDelete" name="idNumberEmployeeToDelete">
                    <button id="confirmDelete" type="submit" class="btn btn-success"><i class="fas fa-check"></i>�Si</button>
                </form>
                <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fas fa-times"></i>�No</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="modalManageHolidaysEmployee" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title custom_align" id="Heading">Gestionar vacances treballador</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="fas fa-times" aria-hidden="true"></span></button>
            </div>
            <div class="modal-body">
                <div class="datepicker date input-group shadow-sm">
                    <div id="datepicker" data-date=""></div>
                    <input type="hidden" id="my_hidden_input">
                </div>
            </div>
            <div class="modal-footer">
                <form method="post" action="${contextPath}/ManagementServlet/menuOption/manageHairdressing/deleteEmployee">
                    <input type="hidden" id="idNumberEmployee" name="idNumberEmployee">
                    <button id="confirmDelete" type="submit" class="btn btn-success"><i class="fas fa-check"></i>�Si</button>
                </form>
                <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fas fa-times"></i>�No</button>
            </div>
        </div>
    </div>
</div>
