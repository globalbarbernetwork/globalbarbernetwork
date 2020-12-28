<%-- 
    Document   : manageService
    Created on : Dec 19, 2020, 6:38:50 PM
    Author     : Grup 3
--%>

<div id="table-responsive">
    <table id="dataTableServices" class="table table-striped customDataTable" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th class="th-sm centerButton w15">
                    <button id="btnAddService" type="button" data-toggle="modal" data-target="#modalEditService" class="btn btn-info btn-rounded btn-sm"><i class="far fa-cut"></i> <i class="fal fa-plus"></i></button>
                </th>
                <th class="th-sm w40">Nom</th>
                <th class="th-sm w20">Duració Aproximada (h)</th>
                <th class="th-sm w15">Preu (&euro;)</th>
                <th class="th-sm w10"></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${services}" var="service">
            <tr>
                <td class="centerButton w15">
                    <button id="btnEdit${service.id}" class="btn btn-primary btn-sm" title="Editar servei" data-title="Edit" data-toggle="modal" data-target="#modalEditService" data-id="${service.id}" data-name="${service.name}" data-duration="${service.obtainDurationFormatted()}" onclick="editService(this)"><span class="fal fa-pencil"></span></button>
                </td>
                <td class="w40">${service.name}</td>
                <td class="w20">${service.obtainDurationFormatted()}</td>
                <td class="w15">${service.obtainPriceFormatted()}</td>
                <td class="centerButton w10">
                    <button id="btnDelete${service.id}" class="btn btn-danger btn-sm" title="Eliminar servei" data-title="Delete" data-toggle="modal" data-target="#modalDeleteService" data-id="${service.id}" data-name="${service.name}" onclick="deleteService(this)"><span class="fal fa-trash"></span></button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>


<div class="modal fade" id="modalEditService" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="Heading"></h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="fas fa-times" aria-hidden="true"></span></button>
            </div>
            <form id="formService" method="post">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="name">Nom</label>
                        <input class="form-control" type="text" id="nameService" name="nameService" placeholder="Nom" title="Omple aquest camp">
                    </div>
                    <div class="form-group">
                        <label for="timePickerService">Duració Aproximada</label>
                        <div class="input-group">
                            <input class="form-control" id="timePickerService" name="timePickerService" type="text" title="Introdueix la duració" readonly>
                            <div class="input-group-append">
                                <div class="input-group-text">
                                    <span><i class="fas fa-clock"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="priceService">Preu</label>
                        <input class="form-control" type="text" id="priceService" name="priceService" placeholder="0,00" title="Omple aquest camp">
                        <small id="errorInput" class="text-danger"></small>
                    </div>
                    <input type="hidden" id="idServiceToUpdate" name="idServiceToUpdate">
                    <input type="hidden" id="durationService" name="durationService">
                </div>
                <div class="modal-footer">
                    <button id="btnConfirmAddEditService" type="submit" class="btn btn-lg" style="width:100%;"/>
                </div>
            </form>
        </div>
    </div>
</div>



<div class="modal fade" id="modalDeleteService" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title custom_align" id="Heading"></h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="fas fa-times" aria-hidden="true"></span></button>
            </div>
            <div class="modal-body">
                <div class="alert alert-danger"><i class="fas fa-exclamation-triangle"></i> Estas segur que vols eliminar aquest servei?</div>
            </div>
            <div class="modal-footer">
                <form method="post" action="${contextPath}/ManagementServlet/menuOption/manageHairdressing/deleteService">
                    <input type="hidden" id="idServiceToDelete" name="idServiceToDelete">
                    <button id="confirmDeleteService" type="submit" class="btn btn-success"><i class="fas fa-check"></i> Si</button>
                </form>
                <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fas fa-times"></i> No</button>
            </div>
        </div>
    </div>
</div>
                    
<input type="hidden" id="incrementMin" name="incrementMin" value="${incrementMin}">