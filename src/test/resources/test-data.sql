-- Datos de prueba para tests de integración
-- Este archivo se carga automáticamente antes de cada test
-- Nota: La limpieza de tablas se realiza en @BeforeEach de cada test

-- Insertar usuarios de prueba
INSERT INTO users (id, username) VALUES 
    (1, 'usuarioUno'),
    (2, 'usuarioDos'),
    (3, 'usuarioTres');

-- Insertar tweets de prueba
INSERT INTO tweets (id, user_id, texto, created_at, tweet_original_id) VALUES 
    (1, 1, 'Primer tweet de usuarioUno', CURRENT_TIMESTAMP, NULL),
    (2, 1, 'Segundo tweet de usuarioUno', CURRENT_TIMESTAMP, NULL),
    (3, 2, 'Primer tweet de usuarioDos', CURRENT_TIMESTAMP, NULL);
