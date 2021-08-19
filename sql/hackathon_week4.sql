CREATE TABLE `conygre`.`investors` (
                                       `id` INT NOT NULL AUTO_INCREMENT,
                                       `name` VARCHAR(45) NOT NULL,
                                       `email` VARCHAR(45) NOT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);

DROP TABLE conygre.transactions;

CREATE TABLE `conygre`.`transactions`(
                                         `id` INT NOT NULL AUTO_INCREMENT,
                                         `investor_id` INT REFERENCES investors(id),
                                         `type` VARCHAR(45) NOT NULL,
                                         `price` DECIMAL(10, 2) NOT NULL,
                                         `amount` INT NOT NULL,
                                         `time_stamp` TIMESTAMP,
                                         `symbol` VARCHAR(45) NOT NULL,
                                         PRIMARY KEY (`id`)
);
use conygre;

insert into investors values(1, "Jung Rae Jang", "jrj@funmail.com");
insert into investors values(2, "Lorna Sanders", "lorna@funmail.com");
insert into investors values(3, "Shrina Parikh", "shrina@funmail.com");


insert into transactions values(1, 1, "Stock", 40.56, 10, TIMESTAMP("2017-01-22",  "13:10:11"), "ITA");
insert into transactions values(2, 1, "Stock", 10.56, 10, TIMESTAMP("2017-01-24",  "13:10:11"), "AMD");
insert into transactions values(3, 1, "Cash", 120000.34, 0, TIMESTAMP("2017-07-23",  "13:10:11"), "Citibank Savings");
insert into transactions values(4, 1, "Cash", 16999.34, 0, TIMESTAMP("2017-07-23",  "13:10:11"), "Citibank Checkings");

insert into transactions values(5, 2, "Stock", 132.34, 10, TIMESTAMP("2017-07-23",  "13:10:11"), "AMD");
insert into transactions values(6, 2, "Stock", 566.78, 12, TIMESTAMP("2017-09-23",  "13:10:11"), "NVD");
insert into transactions values(7, 2, "Stock", 378.78, 100, TIMESTAMP("2017-11-01",  "13:10:11"), "AMG");
insert into transactions values(8, 2, "Stock", 7.98, 1000, TIMESTAMP("2018-01-24",  "13:10:11"), "GGL");

insert into transactions values(9, 2, "Cash", 2300.89, 0, TIMESTAMP("2021-08-19","3:34:10"),"Citibank Savings");
insert into transactions values(10, 2, "Cash", 34000.89, 0, TIMESTAMP("2021-08-19","3:34:10"),"Citibank Checkings");
insert into transactions values(11, 2, "Cash", 45000.89, 0, TIMESTAMP("2021-08-19","3:34:10"),"Fidelity Cash");
insert into transactions values(12, 3, "Cash", 2600000.89, 0, TIMESTAMP("2021-08-19","3:34:10"),"Citibank Savings");
insert into transactions values(13, 3, "Cash", 3800.89, 0, TIMESTAMP("2021-08-19","3:34:10"),"Citibank Checkings");







-- select * from transactions;

