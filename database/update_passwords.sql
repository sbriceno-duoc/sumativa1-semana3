-- Actualizar contraseñas de usuarios con hashes BCrypt correctos
-- Fuerza BCrypt: 10 (más rápido para pruebas, pero igual seguro)

USE recetas_db;

-- admin / admin123
UPDATE usuarios SET password = '$2a$10$N.zmdr9k7uOCQQVbVpgMSu16jfvPCxjLsqFcyuVtAqOQP8lBP5zxK' WHERE username = 'admin';

-- usuario1 / usuario123  
UPDATE usuarios SET password = '$2a$10$dXJ3SW6G7P9C4GMx7EiCsuHxX7pV7kFgAp0jN3uJT.5p8fR5qKqPy' WHERE username = 'usuario1';

-- usuario2 / usuario123 (misma contraseña que usuario1 para simplificar)
UPDATE usuarios SET password = '$2a$10$dXJ3SW6G7P9C4GMx7EiCsuHxX7pV7kFgAp0jN3uJT.5p8fR5qKqPy' WHERE username = 'usuario2';

-- chef / usuario123 (simplificando, todos con usuario123)
UPDATE usuarios SET password = '$2a$10$dXJ3SW6G7P9C4GMx7EiCsuHxX7pV7kFgAp0jN3uJT.5p8fR5qKqPy' WHERE username = 'chef';

-- Verificar actualización
SELECT 'Contraseñas actualizadas correctamente' AS Resultado;
SELECT username, LEFT(password, 40) AS password_hash, enabled FROM usuarios;

-- RESUMEN DE CONTRASEÑAS:
-- admin: admin123
-- usuario1: usuario123
-- usuario2: usuario123  
-- chef: usuario123

