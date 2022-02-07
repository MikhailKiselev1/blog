CREATE TABLE captcha_codes (
  id INT AUTO_INCREMENT NOT NULL,
   time datetime NOT NULL,
   code TINYTEXT NOT NULL,
   secret_code TINYTEXT NOT NULL,
   CONSTRAINT pk_captcha_codes PRIMARY KEY (id)
);