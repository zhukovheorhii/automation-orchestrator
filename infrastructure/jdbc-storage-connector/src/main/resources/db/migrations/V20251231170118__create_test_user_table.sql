CREATE TABLE IF NOT EXISTS test_user
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    VARCHAR NOT NULL,
    email      VARCHAR NOT NULL,
    password   VARCHAR NOT NULL,
    is_locked  BOOLEAN NOT NULL DEFAULT FALSE,
    locked_at  TIMESTAMP WITH TIME ZONE
);
