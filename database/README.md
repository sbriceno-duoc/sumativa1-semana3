# 🗄️ Base de Datos - Recetas Seguras

Directorio de scripts SQL para la base de datos `recetas_db`.

---

## 📁 Estructura de Archivos

```
database/
├── schema.sql              ⭐ Estructura de la BD
├── data.sql                ⭐ Datos de prueba
├── clean_database.sql      🧹 Limpiar tablas
├── drop_database.sql       💣 Eliminar BD completa
├── reset_database.sql      🔄 Reset completo
├── update_passwords.sql    🔐 Actualizar contraseñas (opcional)
├── CONSOLIDACION_SQL.md    📚 Documentación completa
└── backup_*/               💾 Backups anteriores
```

---

## ⚡ Inicio Rápido

### 1️⃣ Instalación Inicial

```bash
# Crear estructura
mysql -uroot -p < database/schema.sql

# Insertar datos de prueba
mysql -uroot -p recetas_db < database/data.sql
```

### 2️⃣ Con Docker

```bash
docker exec -i recetas_mysql mysql -uroot -proot_password < database/schema.sql
docker exec -i recetas_mysql mysql -uroot -proot_password recetas_db < database/data.sql
```

---

## 🔀 Guía de Scripts

### 📊 Creación y Datos

| Archivo | Descripción | Cuándo usar |
|---------|-------------|-------------|
| `schema.sql` | Crea la estructura completa de la BD | Primera instalación o después de eliminar la BD |
| `data.sql` | Inserta datos de prueba (usuarios, recetas) | Después de crear el schema o hacer reset |

### 🛠️ Mantenimiento

| Archivo | Descripción | Cuándo usar |
|---------|-------------|-------------|
| `clean_database.sql` | Elimina todas las tablas (mantiene BD) | Quieres limpiar datos pero mantener la BD |
| `drop_database.sql` | Elimina completamente la BD | Quieres eliminar TODO |
| `reset_database.sql` | Elimina y recrea la estructura (sin datos) | Quieres empezar de cero con estructura limpia |

---

## 💡 Casos de Uso Comunes

### Caso 1: Primera vez usando el proyecto

```bash
mysql -uroot -p < database/schema.sql
mysql -uroot -p recetas_db < database/data.sql
```

### Caso 2: La BD está corrupta, quiero empezar de cero

```bash
mysql -uroot -p < database/reset_database.sql
mysql -uroot -p recetas_db < database/data.sql
```

### Caso 3: Quiero limpiar solo los datos

```bash
mysql -uroot -p < database/clean_database.sql
mysql -uroot -p recetas_db < database/data.sql
```

### Caso 4: Quiero eliminar todo y reinstalar

```bash
mysql -uroot -p < database/drop_database.sql
mysql -uroot -p < database/schema.sql
mysql -uroot -p recetas_db < database/data.sql
```

---

## 🐳 Comandos Docker

### Reset completo en Docker

```bash
# Opción 1: Usar scripts SQL
docker exec -i recetas_mysql mysql -uroot -proot_password < database/reset_database.sql
docker exec -i recetas_mysql mysql -uroot -proot_password recetas_db < database/data.sql

# Opción 2: Eliminar volumen (borra todo)
./docker-stop.sh
docker volume rm sumativa1-semana3_mysql_data
./docker-start.sh  # Se recrea automáticamente con schema.sql y data.sql
```

### Limpiar solo tablas en Docker

```bash
docker exec -i recetas_mysql mysql -uroot -proot_password < database/clean_database.sql
docker exec -i recetas_mysql mysql -uroot -proot_password recetas_db < database/data.sql
```

---

## 📋 Estructura de la Base de Datos

### Tablas Principales

```
roles
  ├── id (PK)
  └── nombre (UNIQUE)

usuarios
  ├── id (PK)
  ├── username (UNIQUE)
  ├── password (BCrypt)
  └── nombre_completo

usuarios_roles (Relación M:N)
  ├── usuario_id (FK → usuarios)
  └── rol_id (FK → roles)

recetas
  ├── id (PK)
  ├── nombre
  ├── tipo_cocina
  ├── dificultad ('Fácil', 'Media', 'Difícil')
  ├── tiempo_preparacion
  ├── media_type ('image', 'video')
  └── usuario_id (FK → usuarios)

comentarios
  ├── id (PK)
  ├── receta_id (FK → recetas)
  ├── usuario_id (FK → usuarios)
  └── texto

valoraciones
  ├── id (PK)
  ├── receta_id (FK → recetas)
  ├── usuario_id (FK → usuarios)
  └── estrellas (1-5)
  └── UNIQUE(receta_id, usuario_id)

recetas_media
  ├── id (PK)
  ├── receta_id (FK → recetas)
  ├── media_url
  ├── media_type ('image', 'video')
  └── orden
```

### Vistas

- `vista_recetas_populares` - Recetas marcadas como populares
- `vista_recetas_recientes` - Recetas marcadas como recientes

---

## 👤 Usuarios de Prueba

Después de ejecutar `data.sql`, tendrás estos usuarios:

| Usuario | Contraseña | Rol | Descripción |
|---------|------------|-----|-------------|
| `admin` | `admin123` | ADMIN + USER | Administrador del sistema |
| `usuario1` | `usuario123` | USER | Usuario regular (Carlos Pérez) |
| `usuario2` | `usuario456` | USER | Usuario regular (María González) |
| `chef` | `chef2025` | USER | Chef profesional (Juan Ramírez) |

---

## 🔒 Seguridad

- ✅ Todas las contraseñas están encriptadas con **BCrypt** (fuerza 12)
- ✅ Las claves foráneas garantizan **integridad referencial**
- ✅ Los constraints validan datos a nivel de BD
- ✅ Índices optimizan las búsquedas frecuentes

---

## 📚 Documentación Adicional

Para más detalles sobre la consolidación de scripts y cambios aplicados, consulta:

📄 **[CONSOLIDACION_SQL.md](./CONSOLIDACION_SQL.md)**

---

## ⚠️ Advertencias

- ⚠️ `drop_database.sql` elimina **TODOS** los datos permanentemente
- ⚠️ `reset_database.sql` elimina y recrea la BD (pierdes datos)
- ⚠️ `clean_database.sql` elimina todas las tablas (pierdes datos)
- ✅ Siempre ten backups antes de operaciones destructivas

---

## 💾 Backups

Los backups de scripts anteriores están en:

```
database/backup_20251123_174420/
```

Para restaurar scripts antiguos:

```bash
cd database
cp backup_20251123_174420/*.sql .
```

---

## 🆘 Solución de Problemas

### Error: "Unknown database 'recetas_db'"

```bash
# Crear la BD primero
mysql -uroot -p < database/schema.sql
```

### Error: "Table 'X' already exists"

```bash
# Limpiar primero
mysql -uroot -p < database/clean_database.sql
# Luego ejecutar schema
mysql -uroot -p < database/schema.sql
```

### Error: "Cannot add foreign key constraint"

```bash
# Hacer reset completo
mysql -uroot -p < database/reset_database.sql
mysql -uroot -p recetas_db < database/data.sql
```

---

**Última actualización:** 23 de Noviembre de 2025
**Versión BD:** 2.0 (Consolidada)
