-- ============================================
-- SCRIPT DE INSERCIÓN DE DATOS
-- Aplicación: Recetas Seguras
-- Datos de prueba para desarrollo y testing
-- ============================================

USE recetas_db;

-- ============================================
-- 1. INSERTAR ROLES
-- ============================================

INSERT INTO roles (nombre) VALUES 
('ROLE_USER'),
('ROLE_ADMIN');

-- ============================================
-- 2. INSERTAR USUARIOS
-- Contraseñas encriptadas con BCrypt (fuerza 12)
-- IMPORTANTE: En producción, las contraseñas deben ser más seguras
-- ============================================

-- Usuario: admin | Contraseña: admin123
-- BCrypt hash de "admin123"
INSERT INTO usuarios (username, password, nombre_completo, email, enabled) VALUES
('admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5gyFUHPKuoOKe', 'Administrador del Sistema', 'admin@recetas.com', TRUE);

-- Usuario: usuario1 | Contraseña: usuario123
-- BCrypt hash de "usuario123"
INSERT INTO usuarios (username, password, nombre_completo, email, enabled) VALUES
('usuario1', '$2a$12$GZZKkL9x3.aOmM.5QiXzEOjBfkD7.GGVzn8JQqo8F7TnGLcDWXFfi', 'Carlos Pérez López', 'carlos.perez@email.com', TRUE);

-- Usuario: usuario2 | Contraseña: usuario456
-- BCrypt hash de "usuario456"
INSERT INTO usuarios (username, password, nombre_completo, email, enabled) VALUES
('usuario2', '$2a$12$xZkPBq9xM4dEMLfIjnVYs.5cDKXfNJM3xIMYJwkL2qYmG8Lc.U9Ee', 'María González Silva', 'maria.gonzalez@email.com', TRUE);

-- Usuario adicional: chef | Contraseña: chef2025
-- BCrypt hash de "chef2025"
INSERT INTO usuarios (username, password, nombre_completo, email, enabled) VALUES
('chef', '$2a$12$FvQYLKPv4KJwQYQXxmT0ZO3xN8qUUZjC7zPGXF3vXZqXQRxZXy8O6', 'Juan Ramírez Chef', 'chef.juan@recetas.com', TRUE);

-- ============================================
-- 3. ASIGNAR ROLES A USUARIOS
-- ============================================

-- admin tiene rol de ADMIN y USER
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES
(1, 1), -- admin -> ROLE_USER
(1, 2); -- admin -> ROLE_ADMIN

-- usuario1 tiene rol de USER
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES
(2, 1); -- usuario1 -> ROLE_USER

-- usuario2 tiene rol de USER
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES
(3, 1); -- usuario2 -> ROLE_USER

-- chef tiene rol de USER
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES
(4, 1); -- chef -> ROLE_USER

-- ============================================
-- 4. INSERTAR RECETAS
-- ============================================

-- RECETA 1: Paella Valenciana
INSERT INTO recetas (
    nombre, tipo_cocina, pais_origen, dificultad, tiempo_coccion, 
    ingredientes, instrucciones, foto_url, descripcion, porciones, 
    popular, reciente
) VALUES (
    'Paella Valenciana',
    'Española',
    'España',
    'Intermedio',
    60,
    '- 400g de arroz bomba
- 1 pollo troceado
- 200g de judías verdes
- 200g de garrofón
- 2 tomates maduros
- Azafrán
- Aceite de oliva
- Sal
- Agua o caldo',
    '1. Calentar aceite en la paellera
2. Sofreír el pollo hasta dorar
3. Añadir las judías verdes y el garrofón
4. Agregar el tomate rallado
5. Añadir el arroz y distribuir uniformemente
6. Agregar el caldo con azafrán
7. Cocinar a fuego medio-alto durante 20 minutos
8. Dejar reposar 5 minutos antes de servir',
    'https://images.unsplash.com/photo-1534080564583-6be75777b70a?w=500',
    'La auténtica paella valenciana, un plato tradicional lleno de sabor',
    4,
    TRUE,
    FALSE
);

-- RECETA 2: Tacos al Pastor
INSERT INTO recetas (
    nombre, tipo_cocina, pais_origen, dificultad, tiempo_coccion,
    ingredientes, instrucciones, foto_url, descripcion, porciones,
    popular, reciente
) VALUES (
    'Tacos al Pastor',
    'Mexicana',
    'México',
    'Fácil',
    30,
    '- 500g de carne de cerdo
- Piña natural
- Cebolla
- Cilantro
- Tortillas de maíz
- Chiles guajillo
- Achiote
- Jugo de naranja
- Ajo
- Comino
- Sal y pimienta',
    '1. Preparar la marinada con chiles, achiote, jugo de naranja, ajo y especias
2. Marinar la carne durante 4 horas mínimo
3. Cortar la carne en trozos pequeños
4. Asar en plancha con piña
5. Calentar las tortillas
6. Servir en tortillas con cebolla, cilantro y piña',
    'https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=500',
    'Deliciosos tacos mexicanos con carne adobada y piña',
    6,
    TRUE,
    TRUE
);

-- RECETA 3: Pasta Carbonara
INSERT INTO recetas (
    nombre, tipo_cocina, pais_origen, dificultad, tiempo_coccion,
    ingredientes, instrucciones, foto_url, descripcion, porciones,
    popular, reciente
) VALUES (
    'Pasta Carbonara',
    'Italiana',
    'Italia',
    'Fácil',
    20,
    '- 400g de espagueti
- 200g de panceta o guanciale
- 4 huevos
- 100g de queso parmesano rallado
- Pimienta negra
- Sal',
    '1. Cocinar la pasta en agua con sal
2. Mientras, dorar la panceta en una sartén
3. Batir los huevos con el queso parmesano
4. Escurrir la pasta reservando agua de cocción
5. Mezclar la pasta con la panceta
6. Retirar del fuego y añadir la mezcla de huevo
7. Mezclar rápidamente hasta crear una crema
8. Añadir pimienta negra molida
9. Servir inmediatamente',
    'https://images.unsplash.com/photo-1612874742237-6526221588e3?w=500',
    'La auténtica carbonara italiana, cremosa y deliciosa',
    4,
    TRUE,
    FALSE
);

-- RECETA 4: Pad Thai
INSERT INTO recetas (
    nombre, tipo_cocina, pais_origen, dificultad, tiempo_coccion,
    ingredientes, instrucciones, foto_url, descripcion, porciones,
    popular, reciente
) VALUES (
    'Pad Thai',
    'Tailandesa',
    'Tailandia',
    'Intermedio',
    25,
    '- 200g de fideos de arroz
- 200g de camarones
- 2 huevos
- Brotes de soja
- Cacahuetes tostados
- Cebolleta
- Salsa de pescado
- Salsa de tamarindo
- Azúcar de palma
- Lima
- Chile en polvo',
    '1. Remojar los fideos en agua tibia
2. Preparar la salsa mezclando tamarindo, pescado y azúcar
3. Saltear los camarones en wok
4. Hacer huevo revuelto y reservar
5. Añadir fideos escurridos al wok
6. Agregar la salsa y mezclar
7. Añadir brotes de soja y cebolleta
8. Servir con cacahuetes, lima y chile',
    'https://images.unsplash.com/photo-1559314809-0d155014e29e?w=500',
    'El plato más famoso de Tailandia, dulce, salado y picante',
    2,
    FALSE,
    TRUE
);

-- RECETA 5: Ramen Japonés
INSERT INTO recetas (
    nombre, tipo_cocina, pais_origen, dificultad, tiempo_coccion,
    ingredientes, instrucciones, foto_url, descripcion, porciones,
    popular, reciente
) VALUES (
    'Ramen Japonés',
    'Japonesa',
    'Japón',
    'Difícil',
    120,
    '- Fideos ramen
- Caldo de huesos de cerdo
- Chashu (cerdo braseado)
- Huevo marinado
- Cebolleta
- Alga nori
- Brotes de bambú
- Pasta de miso
- Salsa de soja
- Aceite de sésamo',
    '1. Preparar el caldo hirviendo huesos durante 8 horas
2. Preparar el chashu marinando y cocinando cerdo a baja temperatura
3. Marinar huevos cocidos en salsa de soja
4. Cocinar los fideos según instrucciones
5. Calentar el caldo y añadir miso
6. Servir fideos en bol
7. Añadir caldo caliente
8. Decorar con chashu, huevo, cebolleta y nori',
    'https://images.unsplash.com/photo-1557872943-16a5ac26437e?w=500',
    'Auténtico ramen japonés con caldo rico y profundo',
    2,
    TRUE,
    TRUE
);

-- RECETA 6: Tiramisú
INSERT INTO recetas (
    nombre, tipo_cocina, pais_origen, dificultad, tiempo_coccion,
    ingredientes, instrucciones, foto_url, descripcion, porciones,
    popular, reciente
) VALUES (
    'Tiramisú',
    'Italiana',
    'Italia',
    'Fácil',
    30,
    '- 500g de mascarpone
- 300g de bizcochos de soletilla
- 4 huevos
- 100g de azúcar
- Café expreso fuerte
- Cacao en polvo
- Amaretto (opcional)',
    '1. Preparar café expreso y dejar enfriar
2. Separar yemas y claras
3. Batir yemas con azúcar hasta blanquear
4. Agregar mascarpone y mezclar
5. Batir claras a punto de nieve
6. Incorporar claras a la mezcla de mascarpone
7. Mojar bizcochos en café
8. Hacer capas alternando bizcochos y crema
9. Refrigerar 4 horas
10. Espolvorear cacao antes de servir',
    'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=500',
    'El postre italiano más famoso del mundo',
    8,
    FALSE,
    TRUE
);

-- RECETA 7: Ceviche Peruano
INSERT INTO recetas (
    nombre, tipo_cocina, pais_origen, dificultad, tiempo_coccion,
    ingredientes, instrucciones, foto_url, descripcion, porciones,
    popular, reciente
) VALUES (
    'Ceviche Peruano',
    'Peruana',
    'Perú',
    'Fácil',
    15,
    '- 500g de pescado fresco (corvina o lenguado)
- Limones
- Cebolla morada
- Ají limo
- Cilantro
- Camote cocido
- Maíz tostado
- Sal y pimienta',
    '1. Cortar el pescado en cubos pequeños
2. Cortar cebolla en juliana fina
3. Picar ají y cilantro
4. Mezclar pescado con jugo de limón
5. Dejar marinar 5-10 minutos
6. Añadir cebolla, ají y cilantro
7. Sazonar con sal y pimienta
8. Servir con camote y maíz tostado',
    'https://images.unsplash.com/photo-1583623025817-d180a2221d0a?w=500',
    'Ceviche fresco y vibrante, el orgullo de Perú',
    4,
    TRUE,
    FALSE
);

-- RECETA 8: Croissants Franceses
INSERT INTO recetas (
    nombre, tipo_cocina, pais_origen, dificultad, tiempo_coccion,
    ingredientes, instrucciones, foto_url, descripcion, porciones,
    popular, reciente
) VALUES (
    'Croissants Franceses',
    'Francesa',
    'Francia',
    'Difícil',
    240,
    '- 500g de harina de fuerza
- 80g de azúcar
- 12g de sal
- 10g de levadura fresca
- 300ml de leche
- 250g de mantequilla fría
- Huevo para pintar',
    '1. Preparar masa base con harina, azúcar, sal, levadura y leche
2. Refrigerar 30 minutos
3. Laminar mantequilla entre papeles
4. Hacer el laminado: doblar y estirar 3 veces
5. Refrigerar entre cada doblado
6. Estirar masa final y cortar triángulos
7. Enrollar los croissants
8. Dejar leudar 2 horas
9. Pintar con huevo
10. Hornear a 200°C durante 15-18 minutos',
    'https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=500',
    'Croissants hojaldrados y mantecosos de panadería francesa',
    12,
    FALSE,
    FALSE
);

-- ============================================
-- 5. VERIFICACIÓN DE DATOS INSERTADOS
-- ============================================

SELECT 'Datos insertados exitosamente' AS Mensaje;

SELECT '=== RESUMEN DE DATOS ===' AS Info;
SELECT 'Roles:' AS Tabla, COUNT(*) AS Total FROM roles
UNION ALL
SELECT 'Usuarios:', COUNT(*) FROM usuarios
UNION ALL
SELECT 'Recetas:', COUNT(*) FROM recetas;

SELECT '=== USUARIOS Y CONTRASEÑAS DE PRUEBA ===' AS Info;
SELECT 
    'Usuario' AS Campo,
    'Contraseña' AS Valor
UNION ALL
SELECT 'admin', 'admin123'
UNION ALL
SELECT 'usuario1', 'usuario123'
UNION ALL
SELECT 'usuario2', 'usuario456'
UNION ALL
SELECT 'chef', 'chef2025';

