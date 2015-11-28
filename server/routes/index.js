var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var mysql = require('mysql');

var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '',
  database : 'mochi'
});

/* GET : Get all user status */
router.get('/', function(req, res, next) {
  connection.query('SELECT * FROM `User`', function (error, results, fields) {
    res.send(results);
  });
});

/* GET : Get all user status belonging to the hid */
router.get('/hid', function(req, res, next) {
  connection.query('SELECT * FROM `User` WHERE Hid = ' + req.query.hid, function (error, results, fields) {
    res.send(results);
  });
});

/* POST : Update user status */
router.post('/', function(req, res) {
  connection.query('INSERT INTO `User` (Id, Name, Status) VALUES (' + req.query.id + ',\'' + req.query.name + '\',' + '\'still\')', function (error, results, fields) {
    res.sendStatus(200);
  });
});

module.exports = router;
