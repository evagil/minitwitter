-- Datos de prueba para tests de integración
TRUNCATE TABLE users;
TRUNCATE TABLE tweets;

INSERT INTO users (id, username) VALUES (1, 'usuarioUno'), (2, 'usuarioDos');
INSERT INTO tweets (id, user_id, texto) VALUES (1, 1, 'Texto original');