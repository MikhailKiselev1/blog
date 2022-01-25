CREATE TABLE global_setting (
  id INT NOT NULL,
   code VARCHAR(255) NULL,
   multiuser_mode INT NULL,
   post_premoderation INT NULL,
   statistics_is_public INT NULL,
   CONSTRAINT pk_global_setting PRIMARY KEY (id)
);