CREATE TABLE tag2post (
  id INT NOT NULL,
   post_id INT NULL,
   tag_id INT NULL,
   CONSTRAINT pk_tag2post PRIMARY KEY (id)
);