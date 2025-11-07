# 🚀 Inicio Rápido - Sistema de Recetas con Docker

## ✅ Todo Listo en 3 Pasos

### 1️⃣ Iniciar Base de Datos

```bash
cd "/Users/sbriceno/Documents/DUOC/SEGURIDAD Y CALIDAD/sumativa_1_semana_3"
./docker-start.sh
```

### 2️⃣ Iniciar Aplicación

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

### 3️⃣ Acceder a la Aplicación

Abre tu navegador en: **http://localhost:8082**

⚠️ **IMPORTANTE:** Es el puerto **8082**, NO 8080

---

## 🔐 Credenciales de Prueba

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| `admin` | `admin123` | ADMIN + USER |
| `usuario1` | `usuario123` | USER |
| `usuario2` | `usuario123` | USER |
| `chef` | `usuario123` | USER |

---

## 🎯 Páginas para Probar

### Sin Login (Públicas)
- **Inicio:** http://localhost:8082/
- **Buscar:** http://localhost:8082/buscar
- **Login:** http://localhost:8082/login

### Con Login (Privadas)
- **Detalle:** http://localhost:8082/detalle/1

---

## 🛑 Detener Todo

```bash
# Detener aplicación: Ctrl + C en la terminal

# Detener Docker
./docker-stop.sh
```

---

## 🔧 Comandos Útiles

```bash
# Ver logs de MySQL
docker-compose logs -f mysql

# Ver contenedores activos
docker ps

# Entrar a MySQL
docker exec -it recetas_mysql mysql -urecetas_user -precetas_pass recetas_db

# Resetear base de datos (BORRA DATOS)
./docker-reset.sh
```

---

## 📊 Estado del Sistema

### ✅ Configuración Actual

- **Puerto Aplicación:** 8082
- **Puerto MySQL:** 3306
- **Base de Datos:** recetas_db
- **Usuario BD:** recetas_user
- **Contraseña BD:** recetas_pass

### 🐳 Servicios Docker

- **MySQL 8.0** - Contenedor: `recetas_mysql`
- **Red:** `recetas-network`
- **Volumen:** `mysql_data` (persiste datos)

---

## 📖 Documentación Completa

- **Docker:** [DOCKER_README.md](./DOCKER_README.md)
- **Proyecto:** [README.md](./README.md)

---

## 🆘 Problemas Comunes

### La aplicación no se conecta a MySQL

```bash
# Verificar que MySQL esté corriendo
docker ps | grep recetas_mysql

# Si no está corriendo
./docker-start.sh
```

### Puerto 3306 o 8082 ocupado

```bash
# Ver qué está usando el puerto
lsof -i :3306
lsof -i :8082

# Cambiar puerto en docker-compose.yml o application-docker.properties
```

### Base de datos sin datos

```bash
# Resetear completamente
./docker-reset.sh
```

---

**🎉 ¡Listo! Ahora tienes un entorno de desarrollo completamente funcional y centralizado.**

