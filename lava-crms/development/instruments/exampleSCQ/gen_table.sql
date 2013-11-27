use pedi;

DROP TABLE IF EXISTS `scq`;
CREATE TABLE IF NOT EXISTS `scq` (
  `instr_id` INT NOT NULL ,
  `scq_date` date NULL DEFAULT null ,
  `scq_name` varchar(20) NULL DEFAULT null ,
  `scq1` smallint NULL DEFAULT null ,
  `scq2` smallint NULL DEFAULT null ,
  `scq3` smallint NULL DEFAULT null ,
  `scq4` smallint NULL DEFAULT null ,
  `scq5` smallint NULL DEFAULT null ,
  `scq6` smallint NULL DEFAULT null ,
  `scq7` smallint NULL DEFAULT null ,
  `scq8` smallint NULL DEFAULT null ,
  `scq9` smallint NULL DEFAULT null ,
  `scq10` smallint NULL DEFAULT null ,
  `scq11` smallint NULL DEFAULT null ,
  `scq12` smallint NULL DEFAULT null ,
  `scq13` smallint NULL DEFAULT null ,
  `scq14` smallint NULL DEFAULT null ,
  `scq15` smallint NULL DEFAULT null ,
  `scq16` smallint NULL DEFAULT null ,
  `scq17` smallint NULL DEFAULT null ,
  `scq18` smallint NULL DEFAULT null ,
  `scq19` smallint NULL DEFAULT null ,
  `scq20` smallint NULL DEFAULT null ,
  `scq21` smallint NULL DEFAULT null ,
  `scq22` smallint NULL DEFAULT null ,
  `scq23` smallint NULL DEFAULT null ,
  `scq24` smallint NULL DEFAULT null ,
  `scq25` smallint NULL DEFAULT null ,
  `scq26` smallint NULL DEFAULT null ,
  `scq27` smallint NULL DEFAULT null ,
  `scq28` smallint NULL DEFAULT null ,
  `scq29` smallint NULL DEFAULT null ,
  `scq30` smallint NULL DEFAULT null ,
  `scq31` smallint NULL DEFAULT null ,
  `scq32` smallint NULL DEFAULT null ,
  `scq33` smallint NULL DEFAULT null ,
  `scq34` smallint NULL DEFAULT null ,
  `scq35` smallint NULL DEFAULT null ,
  `scq36` smallint NULL DEFAULT null ,
  `scq37` smallint NULL DEFAULT null ,
  `scq38` smallint NULL DEFAULT null ,
  `scq39` smallint NULL DEFAULT null ,
  `scq40` smallint NULL DEFAULT null ,
  `scq_social` smallint NULL DEFAULT null ,
  `scq_comm` smallint NULL DEFAULT null ,
  `scq_patterns` smallint NULL DEFAULT null ,
  `scq_total` smallint NULL DEFAULT null ,
  PRIMARY KEY (`instr_id`) )
ENGINE = InnoDB;

ALTER TABLE `scq` ADD CONSTRAINT `scq__instr_id`
 FOREIGN KEY (`instr_id` )
 REFERENCES `instrumenttracking` (`InstrID`)
 ON DELETE NO ACTION
 ON UPDATE NO ACTION, ADD INDEX `scq__instr_id` (`instr_id` ASC);

INSERT IGNORE into instrument(instrname, tablename, formname, hasversion) values('SCQ','scq','LavaWebOnly',0);
