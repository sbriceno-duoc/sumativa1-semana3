-- ============================================
-- SCRIPT DE ELIMINACIÓN COMPLETA DE BASE DE DATOS
-- Aplicación: Recetas Seguras
-- ============================================
--
-- ADVERTENCIA CRÍTICA: Este script DESTRUIRÁ COMPLETAMENTE
-- la base de datos recetas_db y TODOS sus datos de forma PERMANENTE.
--
-- ⚠️ NO HAY VUELTA ATRÁS después de ejecutar este script
-- ⚠️ Asegúrese de tener un backup si necesita los datos
--
-- ============================================

-- ============================================
-- ELIMINAR BASE DE DATOS COMPLETA
-- ============================================
--
-- Esta acción elimina automáticamente:
-- ✓ Todas las tablas
-- ✓ Todos los índices
-- ✓ Todas las vistas
-- ✓ Todas las claves foráneas
-- ✓ Todos los constraints
-- ✓ Todos los triggers (si existieran)
-- ✓ Todos los procedimientos almacenados (si existieran)
-- ✓ Todos los datos
-- ✓ Secuencias AUTO_INCREMENT
--
-- ============================================

DROP DATABASE IF EXISTS recetas_db;

-- ============================================
-- VERIFICACIÓN
-- ============================================

SELECT
    CASE
        WHEN COUNT(*) = 0 THEN '✅ Base de datos recetas_db eliminada completamente'
        ELSE '⚠️ ADVERTENCIA: La base de datos aún existe'
    END AS Resultado
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'recetas_db';

-- ============================================
-- INSTRUCCIONES DE USO:
-- ============================================
--
-- 1. Para ejecutar desde terminal:
--    mysql -uroot -p < database/drop_database.sql
--
-- 2. Para ejecutar con usuario específico:
--    mysql -urecetas_user -precetas_pass < database/drop_database.sql
--
-- 3. Para ejecutar desde Docker:
--    docker exec -i recetas_mysql mysql -uroot -proot_password < database/drop_database.sql
--
-- 4. Para RECREAR la base de datos después de eliminarla:
--    mysql -uroot -p < database/schema.sql
--    mysql -uroot -p recetas_db < database/data.sql
--
-- ============================================
-- ALTERNATIVA MÁS SEGURA:
-- ============================================
--
-- Si solo desea limpiar las tablas pero mantener la estructura
-- de la base de datos, use en su lugar:
--
--    mysql -uroot -p < database/clean_database.sql
--
-- ============================================
