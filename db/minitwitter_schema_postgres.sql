-- Schema para PostgreSQL (equivalente a MiniTwitter)
-- Ejecutar dentro de la base minitwitter

-- Extensiones útiles (opcional)
-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Tablas
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(25) NOT NULL UNIQUE
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_users_username_ci
    ON users ((LOWER(username)));

CREATE TABLE IF NOT EXISTS tweets (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    texto VARCHAR(280) NOT NULL,
    tweet_original_id INT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tweets_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_tweets_original
        FOREIGN KEY (tweet_original_id) REFERENCES tweets(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_tweets_user_id ON tweets(user_id);
CREATE INDEX IF NOT EXISTS idx_tweets_original_id ON tweets(tweet_original_id);

-- Trigger: No permitir retweet propio
-- En PostgreSQL usamos función trigger en plpgsql
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM pg_trigger t
    JOIN pg_class c ON c.oid = t.tgrelid
    WHERE t.tgname = 'before_insert_tweet' AND c.relname = 'tweets'
  ) THEN
    -- Si existe, lo eliminamos
    DROP TRIGGER IF EXISTS before_insert_tweet ON tweets;
  END IF;
END $$;

CREATE OR REPLACE FUNCTION fn_before_insert_tweet()
RETURNS TRIGGER AS $$
DECLARE
  original_user INT;
BEGIN
  IF NEW.tweet_original_id IS NOT NULL THEN
    SELECT user_id INTO original_user FROM tweets WHERE id = NEW.tweet_original_id;
    IF original_user = NEW.user_id THEN
      RAISE EXCEPTION 'No puedes hacer retweet de tu propio tweet';
    END IF;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_insert_tweet
BEFORE INSERT ON tweets
FOR EACH ROW
EXECUTE FUNCTION fn_before_insert_tweet();

-- Procedimientos/funciones
-- PostgreSQL recomienda funciones para estas operaciones simples

CREATE OR REPLACE FUNCTION eliminar_usuario(userId INT)
RETURNS VOID AS $$
BEGIN
  DELETE FROM users WHERE id = userId;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION crear_usuario(nombre VARCHAR)
RETURNS VOID AS $$
BEGIN
  IF EXISTS (SELECT 1 FROM users WHERE LOWER(username) = LOWER(nombre)) THEN
    RAISE EXCEPTION 'El nombre de usuario ya existe';
  ELSE
    INSERT INTO users(username) VALUES (TRIM(nombre));
  END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION crear_tweet(userId INT, texto VARCHAR)
RETURNS VOID AS $$
BEGIN
  INSERT INTO tweets(user_id, texto) VALUES (userId, texto);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION crear_retweet(userId INT, tweetOriginalId INT)
RETURNS VOID AS $$
BEGIN
  INSERT INTO tweets(user_id, texto, tweet_original_id)
  SELECT userId, texto, tweetOriginalId FROM tweets WHERE id = tweetOriginalId;
END;
$$ LANGUAGE plpgsql;


