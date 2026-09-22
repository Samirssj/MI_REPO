# Repositorio Académico Web

Aplicación académica basada en Java 25, JSP, Maven y Apache Tomcat 10.1, con Supabase Auth, PostgreSQL/Data API y Storage.

## Arquitectura

- Java 25 + Jakarta Servlet 6.0
- JSP/Jakarta Pages 3.1
- Maven WAR
- Apache Tomcat 10.1
- Supabase Auth: email/password
- Supabase REST Data API + RLS
- Supabase Storage
- Render mediante Docker

La aplicación no usa `service_role` ni ninguna secret key. El navegador autentica al administrador con Supabase Auth usando la Publishable Key. El access token se sincroniza con una sesión HTTP del servidor y las llamadas Java a Supabase llevan el JWT del usuario; por tanto, las políticas RLS deciden qué puede hacer el usuario.

## Variables

- `SUPABASE_URL`
- `SUPABASE_PUBLISHABLE_KEY`

Nunca subas claves secretas al repositorio.

## Desarrollo

```bash
mvn clean package
```

Copia `target/repositorio-academico.war` a `webapps/` de Tomcat 10.1 y arranca Tomcat.

También puedes usar:

```bash
mvn clean package
```

y desplegar el WAR en tu IDE o instalación local de Tomcat.

## Render

Render detecta `render.yaml` y construye el Dockerfile. Define las dos variables de entorno en el servicio y conecta el repositorio GitHub. `autoDeploy: true` permite nuevos despliegues al actualizar la rama conectada.
