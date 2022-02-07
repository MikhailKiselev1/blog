CREATE TABLE posts_comments (
  id INT AUTO_INCREMENT NOT NULL,
   parent_id INT NULL,
   user_id INT NOT NULL,
   post_id INT NOT NULL,
   time datetime NOT NULL,
   text TEXT NOT NULL,
   CONSTRAINT pk_posts_comments PRIMARY KEY (id)
);

ALTER TABLE posts_comments ADD CONSTRAINT FK_POSTS_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE posts_comments ADD CONSTRAINT FK_POSTS_COMMENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);