# Guía de instalación y configuración

## 1. Java 25

Instala un JDK 25 (no solo JRE) y verifica:

```bash
java -version
javac -version
```

Debe aparecer Java 25.

## 2. Maven

Instala Maven 3.9+ y verifica:

```bash
mvn -version
```

Maven debe usar Java 25.

## 3. Tomcat 10.1

Descarga Tomcat 10.1.x y descomprímelo. Tomcat 10.1 usa Jakarta Servlet 6.0/Jakarta Pages 3.1 y funciona con Java 25.

En Windows:

```bat
set JAVA_HOME=C:\ruta\al\jdk-25
cd C:\ruta\apache-tomcat-10.1.x\bin
startup.bat
```

Comprueba `http://localhost:8080`.

## 4. Supabase

1. Crea un proyecto.
2. En Authentication > Users crea manualmente el único usuario administrador.
3. Copia Project URL.
4. En Settings > API Keys copia la **Publishable Key**.
5. Abre SQL Editor y ejecuta `supabase/schema.sql`.
6. Obtén el UUID del usuario creado y ejecuta:

```sql
insert into public.profiles (id,name,career,description,role)
values ('UUID_DEL_USUARIO','Tu nombre','Ingeniería de Sistemas y Computación','Descripción del estudiante','admin');
```

No necesitas crear un usuario visitante. Los visitantes consultan las filas públicas.

### Seguridad

La Publishable Key es apta para componentes públicos cuando RLS está configurado. Nunca coloques `service_role`/secret keys en JSP, JavaScript o archivos públicos.

El bucket `repository-files` es público para lectura porque el requisito es permitir que visitantes vean y descarguen archivos. Las operaciones de escritura, actualización y eliminación están protegidas por Storage RLS mediante `public.is_admin()`.

## 5. Supabase JS

Este proyecto usa el navegador únicamente para `signInWithPassword`. Descarga la versión actual de la compilación UMD/IIFE de `@supabase/supabase-js` desde la documentación oficial y reemplaza:

`src/main/webapp/assets/js/supabase.min.js`

por el archivo de navegador correspondiente. Debe exponer `window.supabase`.

## 6. Variables locales

En Windows PowerShell:

```powershell
$env:SUPABASE_URL="https://TU-PROJECT-REF.supabase.co"
$env:SUPABASE_PUBLISHABLE_KEY="sb_publishable_..."
```

En Linux/macOS:

```bash
export SUPABASE_URL="https://TU-PROJECT-REF.supabase.co"
export SUPABASE_PUBLISHABLE_KEY="sb_publishable_..."
```

## 7. Construir

```bash
mvn clean package
```

Resultado:

`target/repositorio-academico.war`

Copia el WAR a `apache-tomcat-10.1.x/webapps/`.

Abre:

`http://localhost:8080/repositorio-academico/`

## 8. GitHub

```bash
git init
git add .
git commit -m "Crear repositorio académico web"
git branch -M main
git remote add origin https://github.com/TU-USUARIO/TU-REPOSITORIO.git
git push -u origin main
```

El `.gitignore` evita subir `target/` y `.env`.

## 9. Render

1. Conecta tu cuenta de GitHub.
2. Crea un Web Service desde el repositorio.
3. Render detectará `Dockerfile`, o usa el `render.yaml`.
4. Define:
   - `SUPABASE_URL`
   - `SUPABASE_PUBLISHABLE_KEY`
   - Verifica que el archivo `src/main/webapp/assets/js/supabase.min.js` sea la compilación UMD/IIFE real de Supabase JS y no el archivo placeholder.
5. Mantén `autoDeploy: true`.
6. Render construirá el contenedor en cada push a la rama conectada.

El script de Docker adapta Tomcat al puerto `PORT` que Render proporciona.

## 10. Flujo de uso

Visitante:
- Inicio
- Semanas
- Semana específica
- Actividades
- Perfil
- Visualizar/descargar archivos

Administrador:
- Login con Supabase Auth
- Se crea sesión HTTP después de validar el JWT
- Java envía el JWT a Supabase Data API/Storage
- RLS verifica `auth.uid()` y `public.is_admin()`
- CRUD de recursos y edición de perfil

## Nota de producción

Para producción conviene activar HTTPS (Render lo proporciona), cambiar la cookie de sesión a `Secure=true` y añadir protección CSRF para formularios de modificación. El diseño actual separa autenticación, autorización y persistencia para que estas mejoras puedan incorporarse sin cambiar el modelo de datos.
