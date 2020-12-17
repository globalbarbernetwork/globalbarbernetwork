<%-- 
    Document   : manageWorkers
    Created on : Dec 6, 2020, 5:15:35 PM
    Author     : IOC DAW
--%>

<div id="table-responsive">
    <button id="btnAdd" type="button" data-toggle="modal" data-target="#modalEditEmployee" class="btn btn-info btn-rounded"><span class="fal fa-plus"></span> Afegir</button>
    <table id="dataTable" class="table table-striped" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th class="th-sm"></th>
                <th class="th-sm">Nom</th>
                <th class="th-sm">Cognoms</th>
                <th class="th-sm">DNI</th>
                <th class="th-sm">Edat</th>
                <th class="th-sm">Direcció</th>
                <th class="th-sm">Telèfon Mòbil</th>
                <th class="th-sm"></th>
            </tr>
        </thead>
        <tbody>
        <c:set var="contador" value="${0}"/>
        <c:forEach items="${employees}" var="employee">
            <tr>
                <td class="inline">
                    <button id="btnEdit${contador}" class="btn btn-primary btn-sm" title="Editar dades" data-title="Edit" data-toggle="modal" data-target="#modalEditEmployee" onclick="editEmployee(this)"><span class="fal fa-pencil"></span></button>
                    <button id="btnHolidays${contador}" class="btn btn-warning btn-sm" title="Gestionar de vacances" data-idhairdressing="${employee.idHairdressing}" data-idemployee="${employee.nationalIdentity}" data-name="${employee.name}" data-surname="${employee.surname}" data-toggle="modal" data-target="#modalHolidaysEmployee" onclick="loadInfoModalHolidays(this);"><i class="fal fa-calendar-alt"></i></button>
                </td>
                <td>${employee.name}</td>
                <td>${employee.surname}</td>
                <td>${employee.nationalIdentity}</td>
                <td>${employee.age}</td>
                <td>${employee.address}</td>
                <td>${employee.phoneNumber}</td>
                <td>
                    <button id="btnDelete${contador}" class="btn btn-danger btn-sm" title="Esborrar treballador" data-title="Delete" data-toggle="modal" data-target="#modalDeleteEmployee" onclick="getNatIdenEmployeeToDelete(this)"><span class="fal fa-trash"></span></button>
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
                        <input class="form-control" type="text" id="name" name="name" placeholder="Nom" title="Omple aquest camp">
                    </div>
                    <div class="form-group">
                        <input class="form-control" type="text" id="surname" name="surname" placeholder="Cognoms" title="Omple aquest camp">
                    </div>
                    <div class="form-group">
                        <input class="form-control" type="text" id="nationalIdentity" name="nationalIdentity" placeholder="DNI o NIE" title="Omple aquest camp">
                    </div>
                    <div class="form-group">
                        <input class="form-control" type="text" id="age" name="age" placeholder="Edat" title="Omple aquest camp">
                    </div>
                    <div class="form-group">
                        <input class="form-control" type="text" id="address" name="address" placeholder="Direcció" title="Omple aquest camp">
                    </div>
                    <div class="form-group">
                        <input class="form-control" type="text" id="phoneNumber" name="phoneNumber" placeholder="Telèfon Mòbil" title="Omple aquest camp">
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="btnConfirmAddEdit" type="submit" class="btn btn-lg" style="width:100%;" onclick="validFormEmployee()"/>
                </div>
            </form>
        </div>
    </div>
</div>



<div class="modal fade" id="modalDeleteEmployee" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title custom_align" id="Heading">Eliminar treballador</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="fas fa-times" aria-hidden="true"></span></button>
            </div>
            <div class="modal-body">
                <div class="alert alert-danger"><i class="fas fa-exclamation-triangle"></i> Estas segur que vols eliminar aquest treballador?</div>
            </div>
            <div class="modal-footer">
                <form method="post" action="${contextPath}/ManagementServlet/menuOption/manageHaird/deleteEmployee">
                    <input type="hidden" id="natIdenEmployee" name="natIdenEmployee">
                    <button id="confirmDelete" type="submit" class="btn btn-success"><i class="fas fa-check"></i> Si</button>
                </form>
                <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fas fa-times"></i> No</button>
            </div>
        </div>
    </div>
</div>
                    

<div class="modal fade" id="modalHolidaysEmployee" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title custom_align" id="headModalHolidays">Gestionar vacances treballador</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="fas fa-times" aria-hidden="true"></span></button>
            </div>
            <div class="modal-body">
                <div class="datepicker date input-group shadow-sm">
                    <div id="datepickerHolidays"></div>
                    <input type="hidden" id="selectedHolidays"/>
                </div>
                <input type="hidden" id="idHairdressing"/>
                <input type="hidden" id="selectedIdEmployee"/>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel·lar</button>
                <button type="button" id="saveHolidays" class="btn btn-primary">Gravar</button>
            </div>
        </div>
    </div>
</div>
