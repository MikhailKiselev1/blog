INSERT
INTO
  users
  (is_moderator, reg_time, name, email, password)
VALUES
  ('1', NOW(), 'Moderator', 'moderator@blog.ru', '$2a$12$WQVyxkg4z6EJ4uHo5zOhkuMMjydCnf96bSHTuIcu8Q8b9eNyZj8zS');
INSERT
INTO
  users
  (is_moderator, reg_time, name, email, password)
VALUES
  ('0', NOW(), 'User', 'user@blog.ru', '$2a$12$WQVyxkg4z6EJ4uHo5zOhkuMMjydCnf96bSHTuIcu8Q8b9eNyZj8zS');