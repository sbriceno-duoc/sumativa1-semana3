# Nuevas Funcionalidades - Recetas Seguras

## 📋 Resumen

Se han implementado las siguientes funcionalidades privadas (solo para usuarios autenticados):

1. **Menú desplegable (Dropdown)** en la navegación
2. **Publicar recetas** con formulario completo
3. **Sistema de comentarios** para cada receta
4. **Sistema de valoraciones** con estrellas (1-5)
5. **Botones de compartir** en redes sociales (UI demo)
6. **Almacenamiento de imágenes** con volumen Docker

---

## 🎯 Funcionalidades Implementadas

### 1. Menú Desplegable del Usuario

**Ubicación**: Header de todas las páginas (cuando el usuario está autenticado)

**Características**:
- Reemplaza el texto "Hola, {usuario}!" con un botón desplegable
- Opciones del menú:
  - **Publicar Recetas**: Navega al formulario de publicación
  - **Cerrar Sesión**: Cierra la sesión del usuario

**Archivos modificados**:
- `templates/index.html`
- `templates/buscar.html`
- `templates/detalle.html`
- `static/css/style.css` (estilos del dropdown)

### 2. Publicar Recetas

**URL**: `/recetas/publicar`  
**Acceso**: Solo usuarios autenticados  
**Método**: GET (formulario), POST (envío)

**Características**:
- Formulario completo con validación
- Campos:
  - Nombre de la receta (requerido)
  - Tipo de cocina (requerido)
  - País de origen (requerido)
  - Dificultad: Fácil, Media, Difícil (requerido)
  - Tiempo de cocción en minutos (requerido)
  - Porciones (requerido)
  - Descripción breve (opcional)
  - Ingredientes (requerido, separados por líneas)
  - Instrucciones de preparación (requerido)
  - Imagen de la receta (opcional)

**Subida de imágenes**:
- Almacenamiento: Volumen Docker `/app/uploads`
- Formatos aceptados: JPG, PNG, GIF
- Nombre único generado con UUID
- URL accesible: `/uploads/{filename}`

**Archivos creados**:
- `controller/PublicarRecetaController.java`
- `templates/publicar-receta.html`
- `config/WebConfig.java` (sirve archivos estáticos)

**Volumen Docker**:
```yaml
volumes:
  uploads_data:
    driver: local
```

### 3. Sistema de Comentarios

**Ubicación**: Página de detalle de receta (`/recetas/detalle/{id}`)  
**Acceso**: Solo usuarios autenticados

**Características**:
- Formulario para agregar comentarios
- Lista de comentarios ordenados por fecha (más recientes primero)
- Muestra: autor, fecha/hora, texto del comentario
- Límite: 1000 caracteres por comentario

**Base de datos**:
```sql
CREATE TABLE comentarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receta_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    texto TEXT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (receta_id) REFERENCES recetas(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
```

**Endpoints**:
- `GET /recetas/detalle/{id}`: Muestra comentarios
- `POST /recetas/detalle/{id}/comentario`: Crea nuevo comentario

**Archivos creados**:
- `model/Comentario.java`
- `repository/ComentarioRepository.java`
- `service/ComentarioService.java`

### 4. Sistema de Valoraciones

**Ubicación**: Página de detalle de receta  
**Acceso**: Solo usuarios autenticados

**Características**:
- Valoración de 1 a 5 estrellas
- Un usuario puede valorar una vez por receta (actualizable)
- Muestra promedio de valoraciones
- Muestra total de valoraciones
- Muestra valoración actual del usuario

**Base de datos**:
```sql
CREATE TABLE valoraciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receta_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    puntuacion INT NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
    fecha_valoracion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY (receta_id, usuario_id),
    FOREIGN KEY (receta_id) REFERENCES recetas(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
```

**Endpoints**:
- `GET /recetas/detalle/{id}`: Muestra valoraciones
- `POST /recetas/detalle/{id}/valorar`: Crea o actualiza valoración

**Archivos creados**:
- `model/Valoracion.java`
- `repository/ValoracionRepository.java`
- `service/ValoracionService.java`

### 5. Compartir en Redes Sociales

**Ubicación**: Página de detalle de receta  
**Acceso**: Todos los usuarios autenticados

**Características**:
- Botones visuales para:
  - Facebook
  - Twitter
  - WhatsApp
  - Pinterest
- **Solo UI (interfaz)**: No implementa integración real
- Muestra mensaje de alerta al hacer clic (demo)

**Nota**: Se implementó solo la interfaz visual como solicitado.

### 6. Usuario Autor de Recetas

**Características**:
- Cada receta publicada registra el autor
- Relación `ManyToOne` entre Receta y Usuario
- Columna `usuario_id` en tabla `recetas`

---

## 🗄️ Migraciones de Base de Datos

Se creó el archivo `database/migration_comentarios_valoraciones.sql` que:

1. Agrega columna `usuario_id` a tabla `recetas`
2. Crea tabla `comentarios`
3. Crea tabla `valoraciones`
4. Actualiza constraint de dificultad para incluir "Media"

**Ejecutar migración**:
```bash
docker exec -i recetas_mysql mysql -urecetas_user -precetas_pass recetas_db < database/migration_comentarios_valoraciones.sql
```

---

## 🔒 Seguridad

Todas las nuevas funcionalidades están protegidas:

### Spring Security
```java
.requestMatchers("/recetas/detalle/**", "/recetas/publicar/**").authenticated()
```

### Controladores
```java
@PreAuthorize("isAuthenticated()")
public class PublicarRecetaController { ... }
```

### Protección CSRF
Todos los formularios incluyen token CSRF:
```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
```

### Content Security Policy
Las imágenes subidas están permitidas:
```java
"img-src 'self' data: https://images.unsplash.com; "
```

---

## 🎨 Estilos CSS

Se agregaron estilos para:

- **Dropdown menu**: `.dropdown`, `.dropbtn`, `.dropdown-content`
- **Formularios**: `.publicar-form`, `.form-group`, `.form-row`
- **Compartir**: `.social-share`, `.share-buttons`, `.share-btn`
- **Valoraciones**: `.valoraciones-section`, `.star-rating`, `.stars`
- **Comentarios**: `.comentarios-section`, `.comentario`, `.nuevo-comentario`
- **Alertas**: `.alert-success`, `.alert-error`

---

## 📦 Docker

### Volumen de Uploads

**docker-compose.yml**:
```yaml
app:
  volumes:
    - uploads_data:/app/uploads

volumes:
  uploads_data:
    driver: local
```

**Acceso a imágenes**:
- Interno (contenedor): `/app/uploads/`
- Externo (web): `http://localhost:8082/uploads/{filename}`

### Reiniciar servicios

```bash
# Detener y eliminar contenedores
./docker-stop.sh

# Iniciar servicios
./docker-start.sh

# O reiniciar
docker-compose restart app
```

---

## 🧪 Pruebas

### Probar Publicar Receta

1. Iniciar sesión: http://localhost:8082/login
   - Usuario: `admin` / Contraseña: `admin123`
   - Usuario: `user` / Contraseña: `user123`

2. Click en dropdown → "Publicar Recetas"

3. Llenar formulario y enviar

4. Verificar redirección al detalle de la receta

### Probar Comentarios

1. Navegar a detalle de receta
2. Scroll al final → Sección "Comentarios"
3. Escribir comentario y enviar
4. Verificar que aparece en la lista

### Probar Valoraciones

1. Navegar a detalle de receta
2. Sección "Valoración de la Receta"
3. Seleccionar estrellas (1-5)
4. Click en "Valorar"
5. Verificar promedio actualizado

---

## 📁 Estructura de Archivos Nuevos/Modificados

```
src/main/java/com/duoc/recetas/
├── config/
│   └── WebConfig.java                      [NUEVO]
├── controller/
│   ├── PublicarRecetaController.java       [NUEVO]
│   └── RecetaController.java               [MODIFICADO]
├── model/
│   ├── Comentario.java                     [NUEVO]
│   ├── Valoracion.java                     [NUEVO]
│   └── Receta.java                         [MODIFICADO]
├── repository/
│   ├── ComentarioRepository.java           [NUEVO]
│   └── ValoracionRepository.java           [NUEVO]
└── service/
    ├── ComentarioService.java              [NUEVO]
    └── ValoracionService.java              [NUEVO]

src/main/resources/
├── static/css/
│   └── style.css                           [MODIFICADO]
└── templates/
    ├── buscar.html                         [MODIFICADO]
    ├── detalle.html                        [MODIFICADO]
    ├── index.html                          [MODIFICADO]
    └── publicar-receta.html                [NUEVO]

database/
└── migration_comentarios_valoraciones.sql  [NUEVO]

docker-compose.yml                          [MODIFICADO]
```

---

## 🚀 Despliegue

1. **Reconstruir contenedor app** (si hay cambios en Dockerfile):
   ```bash
   docker-compose build app
   ```

2. **Reiniciar aplicación**:
   ```bash
   docker-compose restart app
   ```

3. **Ver logs**:
   ```bash
   docker logs -f recetas_app
   ```

4. **Aplicar migración** (si aún no se aplicó):
   ```bash
   docker exec -i recetas_mysql mysql -urecetas_user -precetas_pass recetas_db < database/migration_comentarios_valoraciones.sql
   ```

---

## ✅ Checklist de Implementación

- [x] Menú desplegable con opciones de usuario
- [x] Formulario de publicar recetas
- [x] Subida de imágenes con volumen Docker
- [x] Sistema de comentarios
- [x] Sistema de valoraciones con estrellas
- [x] Botones de compartir en redes sociales (UI)
- [x] Migración de base de datos
- [x] Configuración de Spring Security
- [x] Estilos CSS para todos los componentes
- [x] Integración con backend (todo en BD)
- [x] Protección CSRF en formularios
- [x] Validación de datos
- [x] Mensajes de éxito/error

---

## 📝 Notas Técnicas

### Validación de Estrellas
La puntuación se valida tanto en frontend (HTML `required`) como en backend (constraint CHECK en BD y validación en servicio).

### Unicidad de Valoraciones
Un usuario solo puede tener una valoración por receta (constraint UNIQUE en BD). Al valorar nuevamente, se actualiza la valoración existente.

### Ordenamiento de Comentarios
Los comentarios se muestran ordenados del más reciente al más antiguo usando `ORDER BY fecha_creacion DESC`.

### Almacenamiento de Imágenes
Las imágenes se almacenan con nombre UUID para evitar colisiones y problemas de seguridad con nombres de archivo maliciosos.

---

## 🐛 Troubleshooting

### Problema: Imágenes no se cargan

**Solución**:
```bash
# Verificar volumen
docker volume inspect sumativa1-semana3_uploads_data

# Verificar permisos en contenedor
docker exec recetas_app ls -la /app/uploads
```

### Problema: Formulario de publicar no se envía

**Solución**: Verificar que el token CSRF está presente en el formulario:
```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
```

### Problema: Errores de base de datos

**Solución**: Verificar que la migración se ejecutó correctamente:
```bash
docker exec recetas_mysql mysql -urecetas_user -precetas_pass recetas_db -e "SHOW TABLES;"
```

---

## 📞 Contacto

Para preguntas o problemas con la implementación, consultar:
- Código fuente en: `/src/main/java/com/duoc/recetas/`
- Logs de aplicación: `docker logs recetas_app`
- Base de datos: `docker exec -it recetas_mysql mysql -urecetas_user -precetas_pass recetas_db`
