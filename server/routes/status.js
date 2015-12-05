var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var mysql = require('mysql');
var ws = require('../ws');

var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'mochi',
  password : '',
  database : 'mochi'
});

/* GET : Get all status */
router.get('/', function(req, res, next) {
  connection.query('SELECT * from `Status`', function (error, results, fields) {
    res.send(JSON.stringify({users: results}));
  });
});

/* GET : Set status */
router.get('/add', function(req, res, next) {
  connection.query('INSERT INTO `Status` (SensorId, Status) VALUES (' + req.query.seid + ',\'' + req.query.status + '\') ON DUPLICATE KEY UPDATE Status = \'' + req.query.status + '\', Updated = CURRENT_TIMESTAMP', function (error, results, fields) {
    //Send notification
    connection.query('SELECT Sensor.SuimokuseiId, Sensor.UserId, User.Order FROM Sensor INNER JOIN User ON Sensor.UserId = User.Id WHERE Sensor.Id = ' + req.query.seid, function (error, results, fields) {
      results["Status"] = req.query.status;
      res.send(JSON.stringify({results: results}));
    });
  });
});

module.exports = router;

