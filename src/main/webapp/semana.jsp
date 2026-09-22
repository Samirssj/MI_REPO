<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Semana"); %>
<%@ include file="WEB-INF/jspf/header.jspf" %>
<main class="container">
  <div id="week-detail" data-week="${param.n}"></div>
</main>
<script src="assets/js/app.js"></script>
<%@ include file="WEB-INF/jspf/footer.jspf" %>
