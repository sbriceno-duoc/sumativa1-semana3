# 🍳 Recetas Seguras - Aplicación Web con Spring Security

Aplicación web segura desarrollada con **Spring Boot**, **Spring Security** y **Thymeleaf**, cumpliendo con los estándares **OWASP Top 10** para la actividad sumativa de la semana 3.

## 📑 Tabla de Contenidos

- [⚡ Inicio Rápido](#-inicio-rápido-quick-start)
- [📋 Descripción del Proyecto](#-descripción-del-proyecto)
- [🆕 Nuevas Funcionalidades](#-nuevas-funcionalidades-últimas-24-horas)
- [🛠️ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
- [📦 Requisitos Previos](#-requisitos-previos)
- [🚀 Instalación y Configuración](#-instalación-y-configuración)
- [👤 Usuarios de Prueba](#-usuarios-de-prueba)
- [🗺️ Estructura del Proyecto](#️-estructura-del-proyecto)
- [🔒 Características de Seguridad](#-características-de-seguridad-implementadas)
- [🧪 Probar la Aplicación](#-probar-la-aplicación)
- [📊 Análisis con ZAP Proxy](#-análisis-con-zap-proxy)
- [🔍 Análisis de Calidad con SonarQube](#-análisis-de-calidad-con-sonarqube)
- [📊 Cobertura de Código con JaCoCo](#-cobertura-de-código-con-jacoco)
- [📝 Generar Informe](#-generar-informe)
- [🎥 Grabar Video Demo](#-grabar-video-demo)
- [🐳 Docker](#-docker---infraestructura-completa)
- [🚨 Solución de Problemas](#-solución-de-problemas-comunes)
- [📚 Recursos Adicionales](#-recursos-adicionales)

---

## 📋 Descripción del Proyecto

Sistema web de gestión de recetas de cocina que implementa:

- ✅ Autenticación y autorización con Spring Security
- ✅ Protección de URLs (públicas y privadas)
- ✅ Encriptación de contraseñas con BCrypt
- ✅ Protección contra CSRF
- ✅ Headers de seguridad configurados
- ✅ Cumplimiento de OWASP Top 10

### Funcionalidades Implementadas

#### Páginas Públicas (Sin autenticación)
- **Página de Inicio**: Muestra recetas populares y recientes con carrusel multimedia
- **Búsqueda de Recetas**: Búsqueda avanzada por nombre, tipo de cocina, país y dificultad
- **Login**: Formulario de inicio de sesión seguro

#### Páginas Privadas (Requieren autenticación)
- **Detalle de Receta**: Vista completa con:
  - 🎬 **Carrusel multimedia** (imágenes y videos)
  - 💬 **Sistema de comentarios** completo
  - ⭐ **Valoraciones** con estrellas (1-5)
  - 🔗 **Compartir** en redes sociales (Facebook, Twitter, WhatsApp, Pinterest)
  - 📋 **Copiar enlace** directo al portapapeles
- **Publicar Recetas**: Formulario completo para crear nuevas recetas con:
  - 📸 Subida de imágenes/videos
  - ✅ Validación de campos
  - 💾 Almacenamiento persistente
- **Menú de Usuario**: Dropdown con opciones personalizadas

---

## ✅ Cumplimiento de Requisitos

El proyecto cumple completamente con los **3 requisitos funcionales privados** solicitados:

### 1️⃣ [Privada] Agregar fotos y videos a las recetas publicadas ✅

**Implementación:**
- ✅ Formulario de publicación en `/recetas/publicar`
- ✅ Subida de archivos multimedia (imágenes y videos)
- ✅ Almacenamiento en volumen Docker persistente (`/app/uploads`)
- ✅ Tabla `recetas_media` para múltiples archivos por receta
- ✅ Modelo `RecetaMedia.java` con soporte para tipo de media

**Archivos clave:**
- `PublicarRecetaController.java` - Manejo de subida de archivos
- `RecetaMedia.java` - Entidad de multimedia
- `publicar-receta.html` - Formulario con campo de archivo

### 2️⃣ [Privada] Compartir las recetas publicadas en sitio web y redes sociales ✅

**Implementación:**
- ✅ Compartir en **Facebook** (ventana emergente)
- ✅ Compartir en **Twitter/X** (ventana emergente)
- ✅ Compartir en **WhatsApp** (enlace directo)
- ✅ Compartir en **Pinterest** (con imagen de la receta)
- ✅ **Copiar enlace** al portapapeles con notificación
- ✅ Compatible con CSP (scripts externos)

**Archivos clave:**
- `social-share.js` - JavaScript funcional (creado 23/11/2025 16:43)
- `detalle.html` - Sección de compartir (líneas 200-220)
- Funciones: `window.open()`, `navigator.clipboard`, `navigator.share`

### 3️⃣ [Privada] Comentar y valorar recetas publicadas ✅

**Implementación Comentarios:**
- ✅ Formulario para agregar comentarios
- ✅ Lista ordenada por fecha (más recientes primero)
- ✅ Muestra autor y timestamp
- ✅ Tabla `comentarios` en base de datos
- ✅ Límite de 1000 caracteres por comentario

**Implementación Valoraciones:**
- ✅ Sistema de estrellas interactivo (1-5)
- ✅ Cálculo de promedio de valoraciones
- ✅ Constraint UNIQUE: un usuario = una valoración por receta
- ✅ Tabla `valoraciones` en base de datos
- ✅ Actualización de valoración existente

**Archivos clave:**
- `Comentario.java`, `ComentarioService.java`, `ComentarioRepository.java`
- `Valoracion.java`, `ValoracionService.java`, `ValoracionRepository.java`
- `RecetaController.java` - Endpoints POST (líneas 144+)
- `detalle.html` - Formularios y visualización

### 🔒 Protección de Seguridad

Todas las funcionalidades privadas están protegidas por Spring Security:

```java
// SecurityConfig.java - líneas 51-52
.requestMatchers("/recetas/detalle/**", "/recetas/publicar/**").authenticated()
```

**Requisitos:**
- ✅ Usuario debe estar autenticado
- ✅ Token CSRF en todos los formularios
- ✅ Validación del lado servidor
- ✅ Encriptación de contraseñas con BCrypt

---

## ⚡ Inicio Rápido (Quick Start)

### 1. Ejecutar la Aplicación

```bash
# Iniciar aplicación y MySQL
./docker-start.sh

# Esperar 10-15 segundos
# Acceder a: http://localhost:8082
```

**Credenciales de prueba:**
- Usuario: `admin` | Contraseña: `admin123`

### 2. Ejecutar SonarQube (Análisis de Calidad)

```bash
# Iniciar SonarQube
./sonarqube-start.sh

# Esperar 2-3 minutos
# Acceder a: http://localhost:9000
# Usuario: admin | Contraseña: DuocCalidad2025#

# Ejecutar análisis
./sonar-scan.sh

# Ver resultados en: http://localhost:9000/dashboard?id=sumativa2
```

### 3. Detener Servicios

```bash
# Detener aplicación
./docker-stop.sh

# Detener SonarQube
./sonarqube-stop.sh
```

---

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security 6**
- **Spring Data JPA**
- **Thymeleaf**
- **MySQL** (o H2 para pruebas)
- **Maven**
- **Bootstrap CSS** (custom)

---

## 📦 Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

1. **Java JDK 17 o superior**
   ```bash
   java -version
   ```

2. **Maven 3.6 o superior**
   ```bash
   mvn -version
   ```

3. **Docker Desktop** (Recomendado - forma más fácil)
   ```bash
   docker --version
   ```

4. **Git** (para clonar el repositorio)
   ```bash
   git --version
   ```

---

## 🆕 Nuevas Funcionalidades (Últimas 24 horas)

### 📊 Resumen de Cambios Recientes

Se han implementado **27 nuevos archivos** y modificado **26 archivos existentes** con un total de **+2,604 líneas** de código.

#### ✨ Funcionalidades Principales

1. **🎬 Carrusel Multimedia**
   - Navegación por múltiples imágenes y videos
   - Controles laterales (flechas) centrados verticalmente
   - Indicadores de posición
   - Soporte táctil y teclado
   - Archivo: `carousel.js`

2. **💬 Sistema de Comentarios**
   - Formulario para agregar comentarios
   - Lista ordenada por fecha
   - Muestra autor y timestamp
   - Base de datos: Tabla `comentarios`
   - Archivos: `Comentario.java`, `ComentarioService.java`, `ComentarioRepository.java`

3. **⭐ Sistema de Valoraciones**
   - Valoración de 1 a 5 estrellas
   - Promedio de valoraciones
   - Un usuario = una valoración por receta
   - Base de datos: Tabla `valoraciones`
   - Archivos: `Valoracion.java`, `ValoracionService.java`, `ValoracionRepository.java`

4. **📝 Publicar Recetas**
   - Formulario completo de publicación
   - Subida de imágenes/videos
   - Validación de campos
   - Almacenamiento en volumen Docker
   - Archivos: `PublicarRecetaController.java`, `publicar-receta.html`

5. **🔗 Compartir en Redes Sociales** ⭐ NUEVO
   - Compartir en Facebook, Twitter, WhatsApp, Pinterest
   - Copiar enlace al portapapeles
   - Notificaciones visuales
   - Compatible con CSP (sin scripts inline)
   - Archivo: `social-share.js` (creado hoy)

6. **📦 Sistema Multimedia**
   - Tabla `recetas_media` para múltiples archivos
   - Soporte para imágenes y videos
   - Relación OneToMany con recetas
   - Archivo: `RecetaMedia.java`

#### 📁 Archivos Creados (Últimas 24h)

**Backend (10 archivos):**
- `WebConfig.java` - Configuración de archivos estáticos
- `PublicarRecetaController.java` - Controlador de publicación
- `Comentario.java`, `RecetaMedia.java`, `Valoracion.java` - Entidades
- `ComentarioRepository.java`, `RecetaMediaRepository.java`, `ValoracionRepository.java` - Repositorios
- `ComentarioService.java`, `ValoracionService.java` - Servicios

**Frontend (4 archivos):**
- `publicar-receta.html` - Formulario de publicación
- `carousel.js` - Carrusel multimedia
- `receta-modal.js` - Modal de confirmación
- `social-share.js` - Compartir en redes sociales ⭐ NUEVO (16:43)

**Base de Datos (6 archivos SQL):**
- `migration_comentarios_valoraciones.sql`
- `create_recetas_media.sql`
- `add_media_type.sql`
- `rename_tiempo_coccion.sql`
- `fix_dificultad_constraint.sql`
- `assign_recetas_usuarios.sql`

**Documentación (2 archivos):**
- `NUEVAS_FUNCIONALIDADES.md` - Documentación completa
- `RESUMEN_IMPLEMENTACION.md` - Resumen ejecutivo

#### 📝 Archivos Modificados

- `detalle.html` - **+261 líneas** (carrusel, comentarios, valoraciones, compartir)
- `style.css` - **+717 líneas** (estilos completos)
- `RecetaController.java` - **+144 líneas** (comentarios y valoraciones)
- `index.html` - **+94 líneas** (carrusel y dropdown)
- `buscar.html` - **+69 líneas** (mejoras UI)
- `Receta.java` - **+46 líneas** (relaciones)
- `schema.sql` - **+47 líneas** (nuevas tablas)

Para más detalles técnicos, consulta:
- 📄 **[NUEVAS_FUNCIONALIDADES.md](./NUEVAS_FUNCIONALIDADES.md)** - Documentación completa
- 📄 **[RESUMEN_IMPLEMENTACION.md](./RESUMEN_IMPLEMENTACION.md)** - Resumen ejecutivo

---

## 🚀 Instalación y Configuración

### Paso 1: Clonar o Descargar el Proyecto

```bash
# Si está en Git
git clone [URL_DEL_REPOSITORIO]
cd sumativa_1_semana_3

# O simplemente extraer el archivo ZIP en una carpeta
```

### Paso 2: Configurar Base de Datos

#### 🐳 Opción A: Usar Docker Compose (⭐ RECOMENDADO)

Esta es la forma más fácil y rápida. Todo está preconfigurado.

```bash
# 1. Iniciar MySQL con Docker
./docker-start.sh

# 2. Esperar 10-15 segundos a que MySQL esté listo

# 3. Iniciar la aplicación
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

**¡Listo!** La aplicación estará en `http://localhost:8082`

📖 **Ver documentación completa:** [DOCKER_README.md](./DOCKER_README.md)

#### Opción B: Usar MySQL Local

1. **Iniciar MySQL:**
   ```bash
   # Linux/Mac
   sudo systemctl start mysql
   
   # Windows
   net start MySQL80
   ```

2. **Crear la base de datos:**
   ```bash
   mysql -u root -p
   ```
   
   Luego ejecutar:
   ```sql
   source database/schema.sql
   source database/data.sql
   exit;
   ```

3. **Configurar credenciales:**
   
   Editar `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/recetas_db
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_CONTRASEÑA
   ```

#### Opción C: Usar H2 (Base de datos en memoria)

1. En `application.properties`, comentar MySQL y descomentar H2:
   ```properties
   # MySQL
   #spring.datasource.url=jdbc:mysql://localhost:3306/recetas_db
   
   # H2
   spring.datasource.url=jdbc:h2:mem:recetas_db
   spring.datasource.driverClassName=org.h2.Driver
   spring.h2.console.enabled=true
   ```

2. **Nota:** Con H2, los datos se perderán al reiniciar la aplicación.

### Paso 3: Compilar el Proyecto

```bash
mvn clean install
```

O si no tienes Maven instalado globalmente:
```bash
./mvnw clean install  # Linux/Mac
mvnw.cmd clean install  # Windows
```

### Paso 4: Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

O si usas el wrapper de Maven:
```bash
./mvnw spring-boot:run  # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

### Paso 5: Acceder a la Aplicación

Abrir el navegador y visitar:
```
http://localhost:8082
```

**⚠️ IMPORTANTE:** La aplicación usa el puerto **8082** (NO 8080).
- ✅ Correcto: `http://localhost:8082`
- ❌ Incorrecto: `http://localhost:8080` (Apache de otro proyecto)

---

## 👤 Usuarios de Prueba

La aplicación viene con 4 usuarios precargados:

| Usuario | Contraseña | Rol | Descripción |
|---------|------------|-----|-------------|
| `admin` | `admin123` | ADMIN, USER | Administrador del sistema |
| `usuario1` | `usuario123` | USER | Usuario estándar |
| `usuario2` | `usuario123` | USER | Usuario estándar |
| `chef` | `usuario123` | USER | Usuario chef |

**✅ ACTUALIZADAS:** Las contraseñas han sido verificadas y están funcionando correctamente con BCrypt.

**📝 Nota:** Para simplificar las pruebas, usuario1, usuario2 y chef comparten la misma contraseña (usuario123).

**⚠️ IMPORTANTE:** En producción, cambiar todas las contraseñas por unas más seguras y únicas.

---

## 🗺️ Estructura del Proyecto

```
sumativa1-semana3/
├── src/
│   ├── main/
│   │   ├── java/com/duoc/recetas/
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java          # Configuración Spring Security
│   │   │   │   ├── WebConfig.java               # ⭐ Config archivos estáticos
│   │   │   │   └── SecurityHeadersFilter.java   # Filtro headers seguridad
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java          # Controlador principal
│   │   │   │   ├── RecetaController.java        # Controlador de recetas
│   │   │   │   └── PublicarRecetaController.java # ⭐ Publicar recetas
│   │   │   ├── model/
│   │   │   │   ├── Usuario.java                 # Entidad Usuario
│   │   │   │   ├── Rol.java                     # Entidad Rol
│   │   │   │   ├── Receta.java                  # Entidad Receta
│   │   │   │   ├── Comentario.java              # ⭐ Entidad Comentario
│   │   │   │   ├── Valoracion.java              # ⭐ Entidad Valoración
│   │   │   │   └── RecetaMedia.java             # ⭐ Entidad Multimedia
│   │   │   ├── repository/
│   │   │   │   ├── UsuarioRepository.java       # Repositorio usuarios
│   │   │   │   ├── RolRepository.java           # Repositorio roles
│   │   │   │   ├── RecetaRepository.java        # Repositorio recetas
│   │   │   │   ├── ComentarioRepository.java    # ⭐ Repo comentarios
│   │   │   │   ├── ValoracionRepository.java    # ⭐ Repo valoraciones
│   │   │   │   └── RecetaMediaRepository.java   # ⭐ Repo multimedia
│   │   │   ├── service/
│   │   │   │   ├── UserDetailsServiceImpl.java  # Servicio autenticación
│   │   │   │   ├── RecetaService.java           # Servicio recetas
│   │   │   │   ├── ComentarioService.java       # ⭐ Servicio comentarios
│   │   │   │   └── ValoracionService.java       # ⭐ Servicio valoraciones
│   │   │   └── RecetasApplication.java          # Clase principal
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css                # ✏️ Estilos (+717 líneas)
│   │       │   └── js/
│   │       │       ├── carousel.js              # ⭐ Carrusel multimedia
│   │       │       ├── receta-modal.js          # ⭐ Modal confirmación
│   │       │       └── social-share.js          # ⭐ Compartir redes
│   │       ├── templates/
│   │       │   ├── index.html                   # ✏️ Inicio (+94 líneas)
│   │       │   ├── login.html                   # Página login
│   │       │   ├── buscar.html                  # ✏️ Búsqueda (+69 líneas)
│   │       │   ├── detalle.html                 # ✏️ Detalle (+261 líneas)
│   │       │   ├── publicar-receta.html         # ⭐ Publicar receta
│   │       │   └── error.html                   # Página error
│   │       ├── application.properties           # Config aplicación
│   │       └── application-docker.properties    # Config Docker
├── database/
│   ├── schema.sql                               # ✏️ Schema (+47 líneas)
│   ├── data.sql                                 # Datos de prueba
│   ├── migration_comentarios_valoraciones.sql   # ⭐ Migración principal
│   ├── create_recetas_media.sql                 # ⭐ Tabla multimedia
│   ├── add_media_type.sql                       # ⭐ Tipo de media
│   ├── rename_tiempo_coccion.sql                # ⭐ Renombrar campo
│   ├── fix_dificultad_constraint.sql            # ⭐ Fix constraint
│   └── assign_recetas_usuarios.sql              # ⭐ Asignar recetas
├── docs/
│   ├── NUEVAS_FUNCIONALIDADES.md                # ⭐ Nuevas features
│   ├── RESUMEN_IMPLEMENTACION.md                # ⭐ Resumen ejecutivo
│   ├── SOLUCIONES_ALERTAS_ZAP.md                # Soluciones ZAP
│   ├── DOCKER_README.md                         # Guía Docker
│   └── TROUBLESHOOTING_LINUX.md                 # Solución problemas
├── scripts/
│   ├── docker-start.sh                          # Iniciar app + MySQL
│   ├── docker-stop.sh                           # Detener servicios
│   ├── docker-reset.sh                          # Reset completo
│   ├── sonarqube-start.sh                       # ⭐ Iniciar SonarQube
│   ├── sonarqube-stop.sh                        # ⭐ Detener SonarQube
│   └── sonar-scan.sh                            # ⭐ Análisis SonarQube
├── docker-compose.yml                           # ✏️ Docker Compose
├── docker-compose.sonarqube.yml                 # ⭐ SonarQube Compose
├── Dockerfile                                   # Dockerfile app
├── pom.xml                                      # Dependencias Maven
└── README.md                                    # ✏️ Este archivo

Leyenda:
⭐ = Archivo nuevo (últimas 24h)
✏️ = Archivo modificado (últimas 24h)
```

---

## 🔒 Características de Seguridad Implementadas

### OWASP Top 10 Compliance

| OWASP | Vulnerabilidad | Implementación |
|-------|----------------|----------------|
| **A01** | Broken Access Control | ✅ URLs protegidas con Spring Security |
| **A02** | Cryptographic Failures | ✅ Contraseñas encriptadas con BCrypt (fuerza 12) |
| **A03** | Injection | ✅ JPA/Hibernate con consultas parametrizadas |
| **A04** | Insecure Design | ✅ Arquitectura segura con capas |
| **A05** | Security Misconfiguration | ✅ Headers de seguridad configurados |
| **A06** | Vulnerable Components | ✅ Dependencias actualizadas (Spring Boot 3.2) |
| **A07** | Authentication Failures | ✅ Spring Security con autenticación robusta |
| **A08** | Data Integrity Failures | ✅ Protección CSRF habilitada |
| **A09** | Logging Failures | ✅ Logging configurado en application.properties |
| **A10** | SSRF | ✅ Validación de URLs |

### Configuraciones de Seguridad

```java
// SecurityConfig.java
- CSRF Protection: ✅ Habilitado con CookieCsrfTokenRepository
- Password Encoding: ✅ BCrypt con fuerza 12
- Session Management: ✅ Máximo 1 sesión por usuario
- Headers Security: ✅ X-Frame-Options, XSS-Protection
- URL Protection: ✅ Rutas públicas/privadas definidas
```

---

## 🧪 Probar la Aplicación

### 1. Probar Páginas Públicas

- Visita `http://localhost:8082` → Debe mostrar la página de inicio
- Visita `http://localhost:8082/buscar` → Debe mostrar búsqueda
- Intenta acceder a `http://localhost:8082/detalle/1` → Debe redirigir al login

### 2. Probar Autenticación

- Ir a `http://localhost:8082/login`
- Ingresar: `admin` / `admin123`
- Debe redirigir a la página de inicio con sesión iniciada

### 3. Probar Páginas Privadas

- Con sesión iniciada, visita `http://localhost:8082/detalle/1`
- Debe mostrar el detalle de la receta

### 4. Probar Logout

- Click en "Cerrar Sesión"
- Debe cerrar sesión y redirigir al login

---

## 📊 Análisis con ZAP Proxy

### Instalación de ZAP

1. Descargar desde: https://www.zaproxy.org/download/
2. Instalar siguiendo el asistente
3. Ejecutar ZAP

### Configuración

1. Abrir ZAP
2. Ir a Tools > Options > Local Proxies
3. Configurar puerto (por defecto 8080, cambiar si es necesario)

### Realizar Escaneo

1. **Escaneo Automático:**
   - En ZAP, ir a "Quick Start"
   - URL: `http://localhost:8082`
   - Click en "Attack"

2. **Escaneo Manual:**
   - Navegar por la aplicación con ZAP como proxy
   - Click derecho en la URL > Attack > Active Scan

3. **Revisar Resultados:**
   - Panel "Alerts" muestra vulnerabilidades encontradas
   - Clasificadas por criticidad: Alta, Media, Baja

### Generar Reporte

- Tools > Generate HTML Report
- Guardar para incluir en el informe

---

## 🔍 Análisis de Calidad con SonarQube

### ¿Qué es SonarQube?

SonarQube es una plataforma de análisis estático de código que detecta:
- 🐛 **Bugs** y errores de código
- 🔒 **Vulnerabilidades de seguridad**
- 💩 **Code Smells** (malas prácticas)
- 📊 **Cobertura de tests**
- 📈 **Complejidad ciclomática**
- 🔄 **Código duplicado**

### 🚀 Inicio Rápido con SonarQube

#### Paso 1: Iniciar la Aplicación

```bash
# Iniciar la aplicación y MySQL
./docker-start.sh

# Esperar 10-15 segundos a que esté lista
# Verificar en: http://localhost:8082
```

#### Paso 2: Iniciar SonarQube

```bash
# Iniciar SonarQube con PostgreSQL
./sonarqube-start.sh

# Esperar 2-3 minutos a que SonarQube esté listo
# El script muestra el progreso...
```

**Acceso a SonarQube:**
- **URL:** http://localhost:9000
- **Usuario:** `admin`
- **Contraseña:** `DuocCalidad2025#`

⚠️ **IMPORTANTE:** Usa estas credenciales exactas (ya está configurado, NO se pedirá cambiar contraseña)

#### Paso 3: Ejecutar Análisis

```bash
# Ejecutar análisis del código
./sonar-scan.sh

# El script:
# 1. Verifica que los contenedores estén corriendo
# 2. Conecta los contenedores a la red
# 3. Ejecuta el análisis de SonarQube
# 4. Muestra la URL de resultados
```

**Ver Resultados:**
- **URL:** http://localhost:9000/dashboard?id=sumativa2
- **Proyecto:** Recetas Seguras

#### Paso 4: Detener SonarQube

```bash
# Detener SonarQube (mantiene datos)
./sonarqube-stop.sh

# Los datos se guardan en volúmenes Docker
```

### 📊 Interpretar Resultados

SonarQube muestra un dashboard con:

#### 🔴 Bugs
Errores probables en el código que pueden causar fallos.

#### 🟠 Vulnerabilidades
Problemas de seguridad clasificados por severidad:
- **Critical:** Requiere acción inmediata
- **Major:** Requiere atención
- **Minor:** Mejoras recomendadas

#### 🟡 Code Smells
Malas prácticas que dificultan el mantenimiento:
- Métodos muy largos
- Código duplicado
- Complejidad excesiva
- Variables mal nombradas

#### 🟢 Cobertura
Porcentaje de código cubierto por tests.

#### 📈 Métricas
- **Líneas de código**
- **Complejidad ciclomática**
- **Código duplicado (%)**
- **Deuda técnica**

### 🔧 Configuración Avanzada

#### Ver Token de Acceso

El token ya está configurado en `sonar-scan.sh`:
```bash
-Dsonar.token=sqp_07544b918e1e702ae9e26cdac1984b9f411c4806
```

#### Generar Nuevo Token (opcional)

1. Acceder a SonarQube: http://localhost:9000
2. Login: `admin` / `DuocCalidad2025#`
3. Ir a: **My Account > Security > Generate Tokens**
4. Nombre: `recetas-seguras-token`
5. Copiar el token generado
6. Actualizar en `sonar-scan.sh`

#### Configuración en pom.xml

El proyecto ya incluye la configuración de SonarQube:

```xml
<properties>
    <sonar.projectKey>sumativa2</sonar.projectKey>
    <sonar.projectName>Recetas Seguras</sonar.projectName>
    <sonar.host.url>http://localhost:9000</sonar.host.url>
</properties>
```

### 🐛 Troubleshooting SonarQube

#### Error: "SonarQube no responde"

```bash
# Verificar que el contenedor está corriendo
docker ps | grep sonarqube

# Ver logs
docker logs sonarqube

# Esperar más tiempo (puede tardar hasta 3 minutos)
curl http://localhost:9000/api/system/status
```

#### Error: "Token inválido"

```bash
# Verificar credenciales en SonarQube
# http://localhost:9000

# Generar nuevo token si es necesario
```

#### Error: "Contenedores no conectados"

```bash
# Conectar manualmente
docker network connect sumativa1-semana3_sonarqube-network recetas_app

# Verificar conexión
docker network inspect sumativa1-semana3_sonarqube-network
```

#### Error: "Análisis falla"

```bash
# Compilar el proyecto primero
docker exec recetas_app mvn clean compile

# Luego ejecutar análisis
./sonar-scan.sh
```

### 🔗 Recursos de SonarQube

- **Documentación oficial:** https://docs.sonarqube.org/
- **SonarQube Rules:** https://rules.sonarsource.com/
- **OWASP en SonarQube:** https://www.sonarsource.com/solutions/security-vulnerabilities/

---

## 📊 Cobertura de Código con JaCoCo

Este proyecto utiliza **JaCoCo** para medir la cobertura de código de las pruebas unitarias y de integración. JaCoCo genera reportes automáticos tras ejecutar los tests con Maven (`mvn test`), permitiendo visualizar qué partes del código están cubiertas y detectar áreas que requieren más pruebas. Los resultados se integran con SonarQube para un análisis completo de calidad.

Más información en [`documentacion_jacoco.md`](./documentacion_jacoco.md)

---

## 🐳 Docker - Infraestructura Completa

Este proyecto incluye una configuración completa de Docker Compose que facilita el despliegue.

### 📦 Inicio Rápido con Docker

```bash
# 1. Iniciar base de datos MySQL
./docker-start.sh

# 2. Iniciar aplicación con perfil Docker
mvn spring-boot:run -Dspring-boot.run.profiles=docker

# Acceder a: http://localhost:8082
```

### 🔧 Comandos Disponibles

```bash
# Iniciar aplicación y MySQL
./docker-start.sh

# Detener aplicación y MySQL (mantiene datos)
./docker-stop.sh

# Resetear todo (elimina datos)
./docker-reset.sh

# Ver logs
docker logs -f recetas_app
```

**Nota:** La aplicación se inicia automáticamente dentro del contenedor Docker. Accede a `http://localhost:8082`

### 📖 Documentación Completa

Para información detallada sobre Docker, configuración, troubleshooting y más, consulta:

**📄 [DOCKER_README.md](./DOCKER_README.md)**

### 🚀 Dockerizar la Aplicación (Opcional)

Si quieres ejecutar también la aplicación en Docker:

```bash
# Compilar JAR
mvn clean package -DskipTests

# Construir imagen Docker
docker build -t recetas-seguras .

# La imagen ya está lista en docker-compose.yml
# Descomenta la sección 'app' en docker-compose.yml

# Ejecutar todo el stack
docker-compose up -d
```

---

## 🚨 Solución de Problemas Comunes

### Error: "No se puede conectar a MySQL"

```bash
# Verificar que MySQL esté corriendo
sudo systemctl status mysql  # Linux
# o
net start MySQL80  # Windows

# Verificar credenciales en application.properties
```

### Error: "Puerto 8082 en uso"

```bash
# Ver qué está usando el puerto
lsof -i :8082

# Cambiar puerto en application.properties
server.port=8083
```

### Error: "Lombok no funciona"

```bash
# Asegurarse de tener el plugin de Lombok en tu IDE
# IntelliJ: Settings > Plugins > Lombok
# Eclipse: Instalar lombok.jar manualmente
```

### Error: "Las contraseñas no funcionan"

```bash
# Las contraseñas están encriptadas con BCrypt
# Usar las credenciales exactas del README
# Si creaste nuevos usuarios, encriptar con:
# https://bcrypt-generator.com/
```

---

## 🛡️ Análisis de Dependencias con Red Hat Dependency Analytics (RHDA)

Este proyecto utiliza la extensión **Red Hat Dependency Analytics (RHDA)** de VS Code para analizar las dependencias del archivo `pom.xml` y detectar vulnerabilidades activas en las librerías utilizadas. RHDA permite identificar riesgos de seguridad y obtener recomendaciones para actualizar o mitigar dependencias vulnerables.

### ¿Cómo se utiliza RHDA?
- Instala la extensión "Red Hat Dependency Analytics" desde el marketplace de VS Code.
- Abre el archivo `pom.xml` y haz clic derecho para seleccionar "Stack Analysis".
- Revisa el reporte generado, que muestra vulnerabilidades, CVEs y sugerencias de actualización.
- Aplica las recomendaciones para mantener el proyecto seguro y actualizado.

Más información: [Red Hat Dependency Analytics](https://marketplace.visualstudio.com/items?itemName=redhat.vscode-dependency-analytics)

---

## 📝 Resumen de Comandos

### Aplicación Principal

```bash
# Iniciar aplicación y MySQL
./docker-start.sh

# Detener servicios
./docker-stop.sh

# Reset completo (elimina datos)
./docker-reset.sh

# Ver logs
docker logs -f recetas_app
```

### SonarQube

```bash
# Iniciar SonarQube
./sonarqube-start.sh

# Ejecutar análisis
./sonar-scan.sh

# Detener SonarQube
./sonarqube-stop.sh

# Ver logs
docker logs -f sonarqube
```

### Accesos

| Servicio    | URL                    | Usuario   | Contraseña         |
|-------------|------------------------|-----------|--------------------|
| Aplicación  | http://localhost:8082  | `admin`   | `admin123`         |
| SonarQube   | http://localhost:9000  | `admin`   | `DuocCalidad2025#` |
| MySQL       | localhost:3307         | `recetas_user` | `recetas_pass` |

### Base de Datos

```bash
# Acceder a MySQL
docker exec -it recetas_mysql mysql -urecetas_user -precetas_pass recetas_db

# Ejecutar migración
docker exec -i recetas_mysql mysql -urecetas_user -precetas_pass recetas_db < database/migration_comentarios_valoraciones.sql

# Backup
docker exec recetas_mysql mysqldump -urecetas_user -precetas_pass recetas_db > backup.sql
```

---

## 🧪 Test Unitarios en el Proyecto

El proyecto incluye test unitarios para asegurar la calidad y el correcto funcionamiento de las principales funcionalidades. Los test se desarrollaron utilizando **JUnit 5**, la librería estándar para pruebas en Java.

### ¿Qué se testea y por qué?
- **Servicios de negocio:** Se validan los métodos de los servicios (`RecetaService`, `ComentarioService`, `ValoracionService`) para garantizar que la lógica de negocio funcione correctamente y que los datos se gestionen de forma segura.
- **Repositorios:** Se prueban los repositorios JPA para verificar que las operaciones de acceso a la base de datos (guardar, buscar, eliminar) se realicen correctamente.
- **Controladores:** Se realizan pruebas sobre los controladores para asegurar que las rutas y respuestas HTTP sean las esperadas, y que la seguridad (autenticación/autorización) esté correctamente aplicada.

### Ejemplos de test realizados
- Creación, edición y eliminación de recetas.
- Publicación y recuperación de comentarios.
- Valoración de recetas y cálculo de promedios.
- Pruebas de seguridad: acceso restringido a rutas privadas.

Estos test permiten detectar errores tempranamente, asegurar la robustez del sistema y cumplir con los estándares de calidad exigidos en la actividad sumativa.

---

## 📚 Recursos Adicionales

- [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- [Documentación Spring Security](https://spring.io/projects/spring-security)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [ZAP Proxy Documentation](https://www.zaproxy.org/docs/)
- [SonarQube Documentation](https://docs.sonarqube.org/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Red Hat Dependency Analytics](https://marketplace.visualstudio.com/items?itemName=redhat.vscode-dependency-analytics)
- [JaCoCo Documentation](https://www.jacoco.org/)
- [JUnit 5 Documentation](https://junit.org/junit5/)