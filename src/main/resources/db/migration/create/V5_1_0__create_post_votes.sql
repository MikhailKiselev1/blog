CREATE TABLE post_votes (
  id INT NOT NULL,
   time datetime NULL,
   value SMALLINT NULL,
   post_id INT NULL,
   CONSTRAINT pk_post_votes PRIMARY KEY (id)
);

ALTER TABLE post_votes ADD CONSTRAINT FK_POST_VOTES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);