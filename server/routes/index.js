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

/* GET : Get all user status */
router.get('/', function(req, res, next) {
  ws.sendMessage(13261369, "{\"uuid\" : 123}");
  connection.query('SELECT Status.SensorId, Status.Status, Sensor.Id, Sensor.Name, Sensor.UserId, Sensor.SuimokuseiId FROM Status INNER JOIN Sensor ON Status.SensorId = Sensor.Id', function (error, results, fields) {
    res.send(JSON.stringify({users: results}));
  });
});


/* GET : Set suimokusei */
router.get('/add', function(req, res, next) {
  connection.query('INSERT INTO `Suimokusei` (`Id`, `Name`) VALUES (' + req.query.id + ', \"' + req.query.name + '\")', function (error, results, fields) {
    res.send(JSON.stringify({results: results}));
  });
});





///* GET : Get all user status belonging to the hid */
//router.get('/hid', function(req, res, next) {
//  connection.query('SELECT Status.UserId, User.HomeId, User.Name, User.Order, Status.Status, Status.Updated FROM Status INNER JOIN User ON Status.UserId = User.Id WHERE HomeId = ' + req.query.hid, function (error, results, fields) {
//    res.send(JSON.stringify({users: results}));
//  });
//});
//
///* GET : Update status */
//router.get('/add', function(req, res) {
//  connection.query('INSERT INTO `Status` (UserId, Status) VALUES (' + req.query.id + ',\'' + req.query.status + '\') ON DUPLICATE KEY UPDATE Status = \'' + req.query.status + '\', Updated = CURRENT_TIMESTAMP', function (error, results, fields) {
//    res.sendStatus(200);
//  });
//});

module.exports = router;
