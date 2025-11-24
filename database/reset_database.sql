-- ============================================
-- SCRIPT DE RESET COMPLETO DE BASE DE DATOS
-- Aplicación: Recetas Seguras
-- ============================================
--
-- Este script realiza un reset completo:
-- 1. Elimina la base de datos existente
-- 2. La recrea desde cero
-- 3. Crea todas las tablas
--
-- ⚠️ ADVERTENCIA: Todos los datos existentes se perderán
--
-- Después de ejecutar este script, deberá ejecutar data.sql
-- para insertar los datos iniciales.
-- ============================================

-- ============================================
-- PASO 1: Eliminar base de datos existente
-- ============================================
DROP DATABASE IF EXISTS recetas_db;

SELECT '✅ Base de datos anterior eliminada (si existía)' AS Paso1;

-- ============================================
-- PASO 2: Crear base de datos limpia
-- ============================================
CREATE DATABASE recetas_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

SELECT '✅ Base de datos recetas_db creada' AS Paso2;

-- ============================================
-- PASO 3: Seleccionar la base de datos
-- ============================================
USE recetas_db;

-- ============================================
-- PASO 4: Crear estructura de tablas
-- ============================================

-- Tabla: roles
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT chk_rol_nombre CHECK (nombre LIKE 'ROLE_%')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SELECT '✅ Tabla roles creada' AS Paso3;

-- Tabla: usuarios
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100),
    email VARCHAR(100),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SELECT '✅ Tabla usuarios creada' AS Paso4;

-- Tabla: usuarios_roles
CREATE TABLE usuarios_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SELECT '✅ Tabla usuarios_roles creada' AS Paso5;

-- Tabla: recetas
CREATE TABLE recetas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    tipo_cocina VARCHAR(50),
    pais_origen VARCHAR(50),
    dificultad VARCHAR(20) NOT NULL,
    tiempo_preparacion INT NOT NULL,
    ingredientes TEXT,
    instrucciones TEXT,
    foto_url VARCHAR(255),
    media_type VARCHAR(10) DEFAULT 'image',
    descripcion VARCHAR(500),
    porciones INT,
    popular BOOLEAN NOT NULL DEFAULT FALSE,
    reciente BOOLEAN NOT NULL DEFAULT FALSE,
    visualizaciones INT DEFAULT 0,
    usuario_id BIGINT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nombre (nombre),
    INDEX idx_tipo_cocina (tipo_cocina),
    INDEX idx_dificultad (dificultad),
    INDEX idx_popular (popular),
    INDEX idx_reciente (reciente),
    CONSTRAINT chk_porciones CHECK (porciones > 0 OR porciones IS NULL),
    CONSTRAINT chk_tiempo_preparacion CHECK (tiempo_preparacion > 0),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SELECT '✅ Tabla recetas creada' AS Paso6;

-- Tabla: comentarios
CREATE TABLE comentarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receta_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    texto TEXT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_receta (receta_id),
    INDEX idx_usuario (usuario_id),
    INDEX idx_fecha (fecha_creacion DESC),
    FOREIGN KEY (receta_id) REFERENCES recetas(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SELECT '✅ Tabla comentarios creada' AS Paso7;

-- Tabla: valoraciones
CREATE TABLE valoraciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receta_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    estrellas INT NOT NULL,
    fecha_valoracion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_receta (receta_id),
    INDEX idx_usuario (usuario_id),
    CHECK (estrellas BETWEEN 1 AND 5),
    UNIQUE KEY unique_valoracion (receta_id, usuario_id),
    FOREIGN KEY (receta_id) REFERENCES recetas(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SELECT '✅ Tabla valoraciones creada' AS Paso8;

-- Tabla: recetas_media
CREATE TABLE recetas_media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receta_id BIGINT NOT NULL,
    media_url VARCHAR(255) NOT NULL,
    media_type VARCHAR(10) NOT NULL DEFAULT 'image',
    orden INT DEFAULT 0,
    CONSTRAINT fk_recetas_media_receta FOREIGN KEY (receta_id) REFERENCES recetas(id) ON DELETE CASCADE,
    CHECK (media_type IN ('image', 'video'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SELECT '✅ Tabla recetas_media creada' AS Paso9;

-- ============================================
-- PASO 5: Crear índices adicionales
-- ============================================

CREATE INDEX idx_busqueda_compuesta ON recetas(tipo_cocina, pais_origen, dificultad);
CREATE INDEX idx_fecha_creacion ON recetas(fecha_creacion DESC);

SELECT '✅ Índices adicionales creados' AS Paso10;

-- ============================================
-- PASO 6: Crear vistas
-- ============================================

CREATE OR REPLACE VIEW vista_recetas_populares AS
SELECT
    id,
    nombre,
    tipo_cocina,
    pais_origen,
    dificultad,
    tiempo_preparacion,
    visualizaciones,
    foto_url
FROM recetas
WHERE popular = TRUE
ORDER BY visualizaciones DESC;

CREATE OR REPLACE VIEW vista_recetas_recientes AS
SELECT
    id,
    nombre,
    tipo_cocina,
    pais_origen,
    dificultad,
    tiempo_preparacion,
    fecha_creacion,
    foto_url
FROM recetas
WHERE reciente = TRUE
ORDER BY fecha_creacion DESC;

SELECT '✅ Vistas creadas' AS Paso11;

-- ============================================
-- PASO 7: Verificación final
-- ============================================

SELECT '========================================' AS Separador;
SELECT '✅ RESET COMPLETADO EXITOSAMENTE' AS Resultado;
SELECT '========================================' AS Separador;

SELECT
    CONCAT('✅ Base de datos "', SCHEMA_NAME, '" lista para usar') AS Estado,
    DEFAULT_CHARACTER_SET_NAME AS Charset,
    DEFAULT_COLLATION_NAME AS Collation
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'recetas_db';

SELECT '--- Tablas creadas ---' AS Info;
SELECT TABLE_NAME AS Tabla, TABLE_ROWS AS Filas
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'recetas_db'
AND TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;

SELECT '--- Vistas creadas ---' AS Info;
SELECT TABLE_NAME AS Vista
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'recetas_db'
AND TABLE_TYPE = 'VIEW'
ORDER BY TABLE_NAME;

-- ============================================
-- PRÓXIMO PASO:
-- ============================================

SELECT '========================================' AS Separador;
SELECT '📝 Próximo paso: Insertar datos iniciales' AS Instruccion;
SELECT 'Ejecute: mysql -uroot -p recetas_db < database/data.sql' AS Comando;
SELECT '========================================' AS Separador;

-- ============================================
-- INSTRUCCIONES DE USO:
-- ============================================
--
-- 1. Para ejecutar este reset completo:
--    mysql -uroot -p < database/reset_database.sql
--
-- 2. Luego insertar datos de prueba:
--    mysql -uroot -p recetas_db < database/data.sql
--
-- 3. Con Docker (reset completo):
--    docker exec -i recetas_mysql mysql -uroot -proot_password < database/reset_database.sql
--    docker exec -i recetas_mysql mysql -uroot -proot_password recetas_db < database/data.sql
--
-- ============================================
