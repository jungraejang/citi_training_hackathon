CREATE TABLE `conygre`.`investors` (
                                       `id` INT NOT NULL AUTO_INCREMENT,
                                       `name` VARCHAR(45) NOT NULL,
                                       `email` VARCHAR(45) NOT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);


CREATE TABLE `conygre`.`transactions`(
                                         `id` INT NOT NULL AUTO_INCREMENT,
                                         `investor_id` INT REFERENCES investors(id),
                                         `type` VARCHAR(45) NOT NULL,
                                         `price` DECIMAL (10, 2) NOT NULL,
                                         `amount` INT NOT NULL,
                                         `time_stamp` TIMESTAMP,
                                         `symbol` VARCHAR(45) NOT NULL,
                                          PRIMARY KEY (`id`)
);
