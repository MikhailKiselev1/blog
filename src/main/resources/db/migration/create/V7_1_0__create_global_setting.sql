CREATE TABLE global_settings (
  id INT AUTO_INCREMENT NOT NULL,
   code VARCHAR(255) NOT NULL,
   name VARCHAR(255) NOT NULL,
   value VARCHAR(255) NOT NULL,
   CONSTRAINT pk_global_settings PRIMARY KEY (id)
);