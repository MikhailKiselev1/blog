CREATE TABLE posts (
  id INT NOT NULL,
   is_active BIT(1) NULL,
   moderation_status INT NULL,
   moderator_id INT NULL,
   user_id INT NULL,
   time datetime NULL,
   tittle VARCHAR(255) NULL,
   text VARCHAR(255) NULL,
   view_count INT NULL,
   CONSTRAINT pk_posts PRIMARY KEY (id)
);

ALTER TABLE posts ADD CONSTRAINT FK_POSTS_ON_MODERATOR FOREIGN KEY (moderator_id) REFERENCES users (id);

ALTER TABLE posts ADD CONSTRAINT FK_POSTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);