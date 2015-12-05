DROP TABLE IF EXISTS Home;

DROP TABLE IF EXISTS Suimokusei;
CREATE TABLE Suimokusei(
  `Id`        INTEGER,
  `Name`      TEXT,
   PRIMARY KEY (`Id`)
);

DROP TABLE IF EXISTS User;
CREATE TABLE User(
  `Id`            INTEGER AUTO_INCREMENT,
  `Name`          TEXT,
  `SuimokuseiId`  INTEGER DEFAULT 0,
  `Order`         INTEGER DEFAULT 0,
  `Updated`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE(`SuimokuseiId`, `Order`)
);

DROP TABLE IF EXISTS Sensor;
CREATE TABLE Sensor(
  `Id`            INTEGER DEFAULT 0,
  `Name`          TEXT,
  `UserId`        INTEGER DEFAULT 0,
  `SuimokuseiId`  INTEGER DEFAULT 0,
   PRIMARY KEY (`Id`)
);

DROP TABLE IF EXISTS Status;
CREATE TABLE Status(
  `SensorId`    INTEGER DEFAULT 0,
  `Status`      TEXT,
  `Updated`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`SensorId`)
);
