CREATE TABLE posts (
  id INT AUTO_INCREMENT NOT NULL,
   is_active TINYINT NOT NULL,
   moderation_status VARCHAR(255) NOT NULL,
   moderator_id INT NULL,
   user_id INT NOT NULL,
   time datetime NOT NULL,
   title VARCHAR(255) NOT NULL,
   text TEXT NOT NULL,
   view_count INT NOT NULL,
   CONSTRAINT pk_posts PRIMARY KEY (id)
);

ALTER TABLE posts ADD CONSTRAINT FK_POSTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);