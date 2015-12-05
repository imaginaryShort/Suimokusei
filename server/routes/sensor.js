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

/* GET : Get all sensor */
router.get('/', function(req, res, next) {
  connection.query('SELECT * from `Sensor`', function (error, results, fields) {
    res.send(JSON.stringify({users: results}));
  });
});

/* GET : Set sensor */
router.get('/add', function(req, res, next) {
  connection.query('INSERT INTO `Sensor` (`Id`, `Name`, `UserId`, `SuimokuseiId`) VALUES (' + req.query.id + ', \"' + req.query.name + '\",' + req.query.uid + ',' + req.query.sid + ')', function (error, results, fields) {
    res.send(JSON.stringify({results: results}));
  });
});

module.exports = router;
