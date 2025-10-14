-- Script de creación de base de datos para MiniTwitter
CREATE DATABASE IF NOT EXISTS minitwitter;
USE minitwitter;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(25) NOT NULL UNIQUE
);

-- Tabla de tweets
CREATE TABLE IF NOT EXISTS tweets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    texto VARCHAR(280) NOT NULL,
    tweet_original_id INT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tweet_original_id) REFERENCES tweets(id) ON DELETE SET NULL
);

-- Trigger: No permitir retweet propio
DELIMITER //
CREATE TRIGGER before_insert_tweet
BEFORE INSERT ON tweets
FOR EACH ROW
BEGIN
    DECLARE original_user INT;
    IF NEW.tweet_original_id IS NOT NULL THEN
        SELECT user_id INTO original_user FROM tweets WHERE id = NEW.tweet_original_id;
        IF original_user = NEW.user_id THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No puedes hacer retweet de tu propio tweet';
        END IF;
    END IF;
END;//
DELIMITER ;

-- Procedimiento: Eliminar usuario y sus tweets
DELIMITER //
CREATE PROCEDURE eliminar_usuario(IN userId INT)
BEGIN
    DELETE FROM users WHERE id = userId;
END;//
DELIMITER ;

-- Procedimiento: Crear usuario (verifica unicidad)
DELIMITER //
CREATE PROCEDURE crear_usuario(IN nombre VARCHAR(25))
BEGIN
    IF EXISTS (SELECT 1 FROM users WHERE username = nombre) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El nombre de usuario ya existe';
    ELSE
        INSERT INTO users(username) VALUES (nombre);
    END IF;
END;//
DELIMITER ;

-- Procedimiento: Crear tweet
DELIMITER //
CREATE PROCEDURE crear_tweet(IN userId INT, IN texto VARCHAR(280))
BEGIN
    INSERT INTO tweets(user_id, texto) VALUES (userId, texto);
END;//
DELIMITER ;

-- Procedimiento: Crear retweet
DELIMITER //
CREATE PROCEDURE crear_retweet(IN userId INT, IN tweetOriginalId INT)
BEGIN
    INSERT INTO tweets(user_id, texto, tweet_original_id)
    SELECT userId, texto, tweetOriginalId FROM tweets WHERE id = tweetOriginalId;
END;//
DELIMITER ;
