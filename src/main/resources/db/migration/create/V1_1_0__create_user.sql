CREATE TABLE users (
  id INT NOT NULL,
   is_moderator BIT(1) NULL,
   reg_time datetime NULL,
   name VARCHAR(255) NULL,
   email VARCHAR(255) NULL,
   password VARCHAR(255) NULL,
   code VARCHAR(255) NULL,
   photo VARCHAR(255) NULL,
   user_id INT NULL,
   CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT FK_USERS_ON_USER FOREIGN KEY (user_id) REFERENCES post_votes (id);