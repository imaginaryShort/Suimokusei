var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var mysql = require('mysql');

var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'mochi',
  password : '',
  database : 'mochi'
});

/* GET : Get all user status */
router.get('/', function(req, res, next) {
  connection.query('SELECT * FROM `User`', function (error, results, fields) {
    res.send(JSON.stringify({users: results}));
  });
});

/* GET : Get all user status belonging to the hid */
router.get('/hid', function(req, res, next) {
  connection.query('SELECT * FROM `User` WHERE Hid = ' + req.query.hid, function (error, results, fields) {
    res.send(JSON.stringify({users: results}));
  });
});

/* GET : Update user status */
router.get('/add', function(req, res) {
  connection.query('INSERT INTO `User` (Id, Name, Status) VALUES (' + req.query.id + ',\'' + req.query.name + '\',' + '\'' + req.query.status + '\') ON DUPLICATE KEY UPDATE Status = ' + req.query.status, function (error, results, fields) {
    res.sendStatus(200);
  });
});

module.exports = router;
