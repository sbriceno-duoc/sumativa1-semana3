-- ============================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS
-- Aplicación: Recetas Seguras
-- OWASP Top 10 Compliant
-- ============================================

-- Eliminar base de datos si existe (usar con precaución en producción)
-- DROP DATABASE IF EXISTS recetas_db;

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS recetas_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE recetas_db;

-- ============================================
-- TABLA: roles
-- Almacena los roles del sistema
-- ============================================
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT chk_rol_nombre CHECK (nombre LIKE 'ROLE_%')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: usuarios
-- Almacena información de usuarios del sistema
-- SEGURIDAD: Las contraseñas DEBEN estar encriptadas con BCrypt
-- ============================================
CREATE TABLE IF NOT EXISTS usuarios (
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

-- ============================================
-- TABLA: usuarios_roles
-- Tabla de relación muchos a muchos entre usuarios y roles
-- ============================================
CREATE TABLE IF NOT EXISTS usuarios_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: recetas
-- Almacena la información de las recetas
-- ============================================
CREATE TABLE IF NOT EXISTS recetas (
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

-- ============================================
-- TABLA: comentarios
-- Almacena comentarios de usuarios en recetas
-- ============================================
CREATE TABLE IF NOT EXISTS comentarios (
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

-- ============================================
-- TABLA: valoraciones
-- Almacena valoraciones (1-5 estrellas) de usuarios en recetas
-- ============================================
CREATE TABLE IF NOT EXISTS valoraciones (
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

-- ============================================
-- TABLA: recetas_media
-- Almacena múltiples archivos multimedia (imágenes/videos) por receta
-- ============================================
CREATE TABLE IF NOT EXISTS recetas_media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receta_id BIGINT NOT NULL,
    media_url VARCHAR(255) NOT NULL,
    media_type VARCHAR(10) NOT NULL DEFAULT 'image',
    orden INT DEFAULT 0,
    CONSTRAINT fk_recetas_media_receta FOREIGN KEY (receta_id) REFERENCES recetas(id) ON DELETE CASCADE,
    CHECK (media_type IN ('image', 'video'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- ============================================

-- Índice compuesto para búsquedas frecuentes
CREATE INDEX idx_busqueda_compuesta ON recetas(tipo_cocina, pais_origen, dificultad);

-- Índice para ordenamiento por fecha
CREATE INDEX idx_fecha_creacion ON recetas(fecha_creacion DESC);

-- ============================================
-- VISTAS (Opcional - para consultas frecuentes)
-- ============================================

-- Vista de recetas populares
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

-- Vista de recetas recientes
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

-- ============================================
-- INFORMACIÓN DEL ESQUEMA
-- ============================================

SELECT 'Base de datos creada exitosamente' AS Mensaje;
SELECT TABLE_NAME, TABLE_ROWS, TABLE_COMMENT 
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'recetas_db';

