DROP TABLE IF EXISTS `preference` ;

CREATE  TABLE IF NOT EXISTS `preference` (
  `preference_id` INT NOT NULL AUTO_INCREMENT ,
  `user_id` INT(10) NULL ,
  `context` VARCHAR(255) NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `description` VARCHAR(1000) NULL ,
  `value` VARCHAR(255) NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`preference_id`) ,
  INDEX `preference__user_id` (`user_id` ASC) ,
  CONSTRAINT `preference__user_id`
    FOREIGN KEY (`user_id` )
    REFERENCES `authuser` (`UID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

