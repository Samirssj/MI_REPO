<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Administrador"); %>
<%@ include file="WEB-INF/jspf/header.jspf" %>
<main class="auth-container">
  <form id="login-form" class="card auth-card">
    <p class="eyebrow">ADMINISTRACIÓN</p><h1>Iniciar sesión</h1>
    <label>Correo<input id="email" type="email" required autocomplete="username"></label>
    <label>Contraseña<input id="password" type="password" required autocomplete="current-password"></label>
    <button class="button" type="submit">Ingresar</button>
    <p id="login-message" class="message"></p>
  </form>
</main>
<script src="https://cdn.jsdelivr.net/npm/@supabase/supabase-js@2"></script>
<script src="assets/js/auth.js"></script>
<%@ include file="WEB-INF/jspf/footer.jspf" %>
