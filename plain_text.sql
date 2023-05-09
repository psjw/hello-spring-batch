CREATE TABLE `plain_text`
(
    `id`   int(10) NOT NULL AUTO_INCREMENT,
    `text` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci




insert into `plain_text`(`id`,`text`)
values (1,'apple');

insert into `plain_text`(`id`, `text`)
values (2, 'banana');

insert into `plain_text`(`id`, `text`)
values (3, 'carrot');

insert into `plain_text`(`id`, `text`)
values (4, 'dessert');

insert into `plain_text`(`id`, `text`)
values (5, 'egg');


insert into `plain_text`(`id`, `text`)
values (6, 'fish');


insert into `plain_text`(`id`, `text`)
values (7, 'goose');
