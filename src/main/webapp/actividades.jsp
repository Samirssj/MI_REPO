<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Actividades"); %>
<%@ include file="WEB-INF/jspf/header.jspf" %>
<main class="container">
  <div class="section-head"><div><p class="eyebrow">TRABAJOS</p><h1>Actividades</h1><p>Consulta las actividades almacenadas en el repositorio.</p></div></div>
  <div id="activities" class="resource-list"></div>
</main>
<script src="assets/js/app.js"></script>
<%@ include file="WEB-INF/jspf/footer.jspf" %>
