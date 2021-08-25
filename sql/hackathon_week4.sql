CREATE DATABASE conygre;
CREATE TABLE `conygre`.`investors` (
                                       `id` INT NOT NULL AUTO_INCREMENT,
                                       `name` VARCHAR(45) NOT NULL,
                                       `email` VARCHAR(45) NOT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);

-- DROP TABLE conygre.transactions;

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
insert into transactions values(7, 2, "Stock", 378.78, 100, TIMESTAMP("2017-11-01",  "13:10:11"), "AMG");

insert into transactions values(9, 2, "Cash", 2300.89, 0, TIMESTAMP("2021-08-19","3:34:10"),"Citibank Savings");
insert into transactions values(10, 2, "Cash", 34000.89, 0, TIMESTAMP("2021-08-19","3:34:10"),"Citibank Checkings");
insert into transactions values(11, 2, "Cash", 45000.89, 0, TIMESTAMP("2021-08-19","3:34:10"),"Fidelity Cash");
insert into transactions values(12, 3, "Cash", 2600000.89, 0, TIMESTAMP("2021-08-19","3:34:10"),"Citibank Savings");
insert into transactions values(13, 3, "Cash", 3800.89, 0, TIMESTAMP("2021-08-19","3:34:10"),"Citibank Checkings");

insert into transactions value(14,1,"Cash",50000,0,TIMESTAMP("2021-02-23",  "13:10:11"),"Citibank Savings");
insert into transactions value(15,1,"Cash",10000,0,TIMESTAMP("2021-06-23",  "13:10:11"),"Citibank Savings");
insert into transactions value(16,1,"Cash",12000,0,TIMESTAMP("2021-08-12",  "13:10:11"),"Citibank Savings");
insert into transactions value(17,1,"Cash",15000,0,TIMESTAMP("2021-08-23",  "13:10:11"),"Citibank Savings");

insert into transactions values(18,1,"Stock",55.5,30,TIMESTAMP("2020-09-12",  "13:10:11"), "WDC");
insert into transactions values(19,1,"Stock",12.33,15,TIMESTAMP("2019-06-12",  "13:10:11"), "PENN");
insert into transactions values(20,1,"Stock",14.09,40,TIMESTAMP("2020-04-20",  "13:10:11"), "MU");
insert into transactions values(21,1,"Stock",88.02,5,TIMESTAMP("2018-02-01",  "13:10:11"), "NRG");

insert into transactions values(22,1,"Stock",55.5,30,TIMESTAMP("2018-09-20",  "13:10:11"), "GPS");
insert into transactions values(23,1,"Stock",12.33,15,TIMESTAMP("2019-12-12",  "13:10:11"), "NUE");
insert into transactions values(24,1,"Stock",14.09,40,TIMESTAMP("2020-04-20",  "13:10:11"), "PFE");
insert into transactions values(25,1,"Stock",88.02,5,TIMESTAMP("2018-02-01",  "13:10:11"), "CPB");
insert into transactions values(26,1,"Stock",55.5,30,TIMESTAMP("2020-09-12",  "13:10:11"), "BIIB");



-- select * from transactions;



