<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("pageTitle", "Inicio");
%>
<%@ include file="WEB-INF/jspf/header.jspf" %>
<section class="hero">
  <div>
    <p class="eyebrow">REPOSITORIO ACADÉMICO</p>
    <h1>Material académico organizado por semanas.</h1>
    <p>Consulta actividades, documentos y recursos del curso desde cualquier dispositivo.</p>
    <div class="actions">
      <a class="button" href="semanas.jsp">Ver 16 semanas</a>
      <a class="button secondary" href="actividades.jsp">Ver actividades</a>
    </div>
  </div>
</section>
<section class="grid three">
  <article class="card"><h2>16 semanas</h2><p>Contenido organizado cronológicamente para facilitar la consulta.</p></article>
  <article class="card"><h2>Archivos</h2><p>Visualiza o descarga documentos publicados por el administrador.</p></article>
  <article class="card"><h2>Acceso público</h2><p>Los visitantes consultan el repositorio sin iniciar sesión.</p></article>
</section>
<%@ include file="WEB-INF/jspf/footer.jspf" %>
