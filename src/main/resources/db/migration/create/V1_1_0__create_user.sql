CREATE TABLE users (
  id INT AUTO_INCREMENT NOT NULL,
   is_moderator TINYINT NOT NULL,
   reg_time datetime NOT NULL,
   name VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   code VARCHAR(255) NULL,
   photo TEXT NULL,
   CONSTRAINT pk_users PRIMARY KEY (id)
);