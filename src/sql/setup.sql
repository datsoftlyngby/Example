DROP DATABASE IF EXISTS example;
CREATE DATABASE example;

DROP USER IF EXISTS example@localhost;
CREATE USER example@localhost;
GRANT ALL PRIVILEGES ON example.* TO example@localhost;

DROP DATABASE IF EXISTS exampletest;
CREATE DATABASE exampletest;

DROP USER IF EXISTS exampletest@localhost;
CREATE USER exampletest@localhost;
GRANT ALL PRIVILEGES ON exampletest.* TO exampletest@localhost;
