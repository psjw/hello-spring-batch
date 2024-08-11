drop table `house`.`plain_text`;
CREATE TABLE `house`.`plain_text` (
                                      `id` INT NOT NULL AUTO_INCREMENT,
                                      `text` VARCHAR(100) NOT NULL,
                                      PRIMARY KEY (`id`));

INSERT INTO plain_text values (1, 'apple');
INSERT INTO plain_text values (2, 'banana');
INSERT INTO plain_text values (3, 'carrot');
INSERT INTO plain_text values (4, 'dessert');
INSERT INTO plain_text values (5, 'egg');
INSERT INTO plain_text values (6, 'fish');
INSERT INTO plain_text values (7, 'goose');

drop TABLE `result_text`;
CREATE TABLE `result_text` (
                               `id` int(11) NOT NULL AUTO_INCREMENT,
                               `text` varchar(100) NOT NULL,
                               PRIMARY KEY (`id`));