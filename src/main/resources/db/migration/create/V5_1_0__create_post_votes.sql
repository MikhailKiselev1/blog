CREATE TABLE post_votes (
  id INT AUTO_INCREMENT NOT NULL,
   user_id INT NOT NULL,
   post_id INT NOT NULL,
   time datetime NOT NULL,
   value TINYINT NOT NULL,
   CONSTRAINT pk_post_votes PRIMARY KEY (id)
);

ALTER TABLE post_votes ADD CONSTRAINT FK_POST_VOTES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);