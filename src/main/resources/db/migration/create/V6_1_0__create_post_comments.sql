CREATE TABLE post_comments (
  id INT NOT NULL,
   parent_id ID NULL,
   post_id INT NULL,
   user_id INT NULL,
   time datetime NULL,
   text VARCHAR(255) NULL,
   CONSTRAINT pk_post_comments PRIMARY KEY (id)
);

ALTER TABLE post_comments ADD CONSTRAINT FK_POST_COMMENTS_ON_PARENT FOREIGN KEY (parent_id) REFERENCES post_comments (id);

ALTER TABLE post_comments ADD CONSTRAINT FK_POST_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE post_comments ADD CONSTRAINT FK_POST_COMMENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);