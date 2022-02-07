CREATE TABLE tag2post (
  id INT AUTO_INCREMENT NOT NULL,
   post_id INT NOT NULL,
   tag_id INT NOT NULL,
   CONSTRAINT pk_tag2post PRIMARY KEY (id)
);