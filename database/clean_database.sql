-- ============================================
-- SCRIPT DE LIMPIEZA COMPLETA DE BASE DE DATOS
-- Aplicación: Recetas Seguras
-- ============================================
--
-- ADVERTENCIA: Este script ELIMINARÁ COMPLETAMENTE la base de datos
-- y todos sus datos. Use con precaución.
--
-- Este script:
-- 1. Elimina todas las vistas
-- 2. Elimina todas las tablas (con CASCADE automático por FK)
-- 3. Elimina la base de datos completa
-- 4. Los índices se eliminan automáticamente al eliminar las tablas
-- 5. Los AUTO_INCREMENT se resetean automáticamente
-- ============================================

-- ============================================
-- PASO 1: Seleccionar la base de datos
-- ============================================
USE recetas_db;

-- ============================================
-- PASO 2: Desactivar verificación de claves foráneas temporalmente
-- Esto permite eliminar tablas sin preocuparse por el orden
-- ============================================
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- PASO 3: Eliminar todas las vistas si existen
-- ============================================
DROP VIEW IF EXISTS vista_recetas_populares;
DROP VIEW IF EXISTS vista_recetas_recientes;

-- ============================================
-- PASO 4: Eliminar todas las tablas
-- El orden no importa gracias a FOREIGN_KEY_CHECKS = 0
-- ============================================

-- Tablas de funcionalidades (comentarios, valoraciones, multimedia)
DROP TABLE IF EXISTS comentarios;
DROP TABLE IF EXISTS valoraciones;
DROP TABLE IF EXISTS recetas_media;

-- Tabla principal de recetas
DROP TABLE IF EXISTS recetas;

-- Tablas de usuarios y roles
DROP TABLE IF EXISTS usuarios_roles;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS roles;

-- ============================================
-- PASO 5: Reactivar verificación de claves foráneas
-- ============================================
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- PASO 6: Mostrar confirmación
-- ============================================
SELECT 'Todas las tablas y vistas han sido eliminadas' AS Resultado;

-- Verificar que no queden tablas
SELECT
    CASE
        WHEN COUNT(*) = 0 THEN 'Base de datos limpiada correctamente - No hay tablas restantes'
        ELSE CONCAT('ADVERTENCIA: Aún quedan ', COUNT(*), ' tablas')
    END AS Estado
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'recetas_db';

-- ============================================
-- OPCIONAL: Eliminar la base de datos completa
-- Descomente la siguiente línea si desea eliminar la BD completa
-- ============================================
-- DROP DATABASE IF EXISTS recetas_db;
-- SELECT 'Base de datos recetas_db eliminada completamente' AS Resultado;

-- ============================================
-- NOTAS IMPORTANTES:
-- ============================================
--
-- 1. Los índices (INDEX) se eliminan automáticamente al eliminar las tablas
-- 2. Los AUTO_INCREMENT se resetean al eliminar las tablas
-- 3. Las claves foráneas (FOREIGN KEY) se eliminan con las tablas
-- 4. Los constraints (CHECK, UNIQUE) se eliminan con las tablas
-- 5. Para recrear la base de datos después de limpiarla:
--    mysql -uroot -p < schema.sql
--    mysql -uroot -p recetas_db < data.sql
--
-- ============================================
