DROP TABLE IF EXISTS Status;
CREATE TABLE Status(
  `UserId`        INTEGER DEFAULT 0,
  `Status`    ENUM('', 'still', 'walk', 'run', 'bicycle', 'sleep', 'meal', 'refrigerator') DEFAULT '',
  `Updated`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`UserId`)
);

DROP TABLE IF EXISTS User;
CREATE TABLE User(
  `Id`        INTEGER AUTO_INCREMENT,
  `Name`      TEXT,
  `HomeId`       INTEGER DEFAULT 0,
  `Updated`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`Id`)
);

DROP TABLE IF EXISTS Home;
CREATE TABLE Home(
  `Id`        INTEGER AUTO_INCREMENT,
  `Name`      TEXT,
   PRIMARY KEY (`Id`)
);
