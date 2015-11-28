DROP TABLE IF EXISTS User;
CREATE TABLE User(
  `Id`        INTEGER DEFAULT 0 PRIMARY KEY,
  `Name`      TEXT DEFAULT '',
  `Hid`       INTEGER DEFAULT 0,
  `Status`    ENUM('', 'still', 'walk', 'run', 'bicycle', 'sleep', 'meal') DEFAULT '',
  `Updated`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
