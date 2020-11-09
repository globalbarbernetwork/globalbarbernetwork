<%-- 
    Document   : index
    Created on : 31 oct. 2020, 18:20:31
    Author     : Grup 3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="listHairdressingsJSON" scope="request" class="java.lang.String"/>


    <jsp:include page="./templates/base.jsp"/>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src='https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.js'></script>
        <link href='https://api.mapbox.com/mapbox-gl-js/v1.12.0/mapbox-gl.css' rel='stylesheet'>
        <link rel="stylesheet" href="${contextPath}/css/index.css">
        <script src="${contextPath}/js/mapbox.js"></script>
        
    </head>
    
        <jsp:include page="./templates/header.jsp"/>
        <main>
            <div id='map'></div>
        </main>
        
        <!-- Modal -->
        <div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLongTitle">Realitzar reserva</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                ...
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel·lar</button>
                <button type="button" class="btn btn-primary">Reservar</button>
              </div>
            </div>
          </div>
        </div>
        <jsp:include page="./templates/footer.jsp"/>
        <input id="listHairdressingsJSON" name="listHairdressingsJSON" type="hidden" value='<%=listHairdressingsJSON%>'/>

