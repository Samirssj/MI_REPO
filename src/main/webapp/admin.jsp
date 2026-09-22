<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Panel administrativo"); %>
<%@ include file="WEB-INF/jspf/header.jspf" %>
<main class="container">
  <div class="section-head"><div><p class="eyebrow">ADMIN</p><h1>Panel administrativo</h1><p>Gestiona semanas, recursos y perfil.</p></div><button id="logout" class="button secondary">Cerrar sesión</button></div>
  <section class="admin-grid">
    <form id="resource-form" class="card">
      <h2>Agregar / editar recurso</h2>
      <input type="hidden" id="resource-id">
      <label>Título<input id="r-title" required></label>
      <label>Descripción<textarea id="r-description"></textarea></label>
      <label>Semana<select id="r-week"></select></label>
      <label>Tipo<select id="r-type"><option value="document">Documento</option><option value="activity">Actividad</option><option value="link">Enlace</option></select></label>
      <label>Archivo<input id="r-file" type="file"></label>
      <label>URL externa (opcional)<input id="r-url" type="url"></label>
      <div class="actions"><button class="button" type="submit">Guardar</button><button class="button secondary" id="clear-resource" type="button">Limpiar</button></div>
      <p id="resource-message" class="message"></p>
    </form>
    <form id="profile-form" class="card">
      <h2>Perfil</h2>
      <label>Nombre<input id="p-name" required></label>
      <label>Carrera<input id="p-career" required></label>
      <label>Descripción<textarea id="p-description"></textarea></label>
      <label>Foto<input id="p-photo" type="file" accept="image/*"></label>
      <div class="actions"><button class="button" type="submit">Guardar perfil</button></div>
      <p id="profile-message" class="message"></p>
    </form>
  </section>
  <section class="card">
    <h2>Recursos publicados</h2><div id="admin-resources" class="resource-list"></div>
  </section>
</main>

<script src="assets/js/admin.js"></script>
<%@ include file="WEB-INF/jspf/footer.jspf" %>
