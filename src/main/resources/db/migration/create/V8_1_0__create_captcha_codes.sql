CREATE TABLE captcha_codes (
  id INT NOT NULL,
   time datetime NULL,
   code SMALLINT NULL,
   secret_code SMALLINT NULL,
   CONSTRAINT pk_captcha_codes PRIMARY KEY (id)
);