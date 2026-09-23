document.addEventListener("DOMContentLoaded", () => {
	const config = document.getElementById("app-config");
	const form = document.getElementById("login-form");
	const emailInput = document.getElementById("email");
	const passwordInput = document.getElementById("password");
	const message = document.getElementById("login-message");

	form.addEventListener("submit", async (event) => {
		event.preventDefault();
		message.textContent = "";

		if (!window.supabase) {
			message.textContent = "No se pudo cargar el servicio de autenticación.";
			return;
		}
		if (!config?.dataset.url || !config.dataset.key) {
			message.textContent = "Falta configurar Supabase en el servidor.";
			return;
		}

		message.textContent = "Ingresando...";
		try {
			const client = window.supabase.createClient(config.dataset.url, config.dataset.key);
			const { data, error } = await client.auth.signInWithPassword({
				email: emailInput.value.trim(),
				password: passwordInput.value
			});
			if (error) {
				message.textContent = error.message;
				return;
			}

			const response = await fetch("api/auth/session", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ access_token: data.session.access_token })
			});
			if (!response.ok) {
				message.textContent = "La cuenta no tiene permisos de administrador.";
				await client.auth.signOut();
				return;
			}
			location.href = "admin.jsp";
		} catch (error) {
			message.textContent = "No se pudo iniciar sesión. Revisa la configuración de Supabase.";
			console.error(error);
		}
	});
});