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
  connection.query('SELECT Status.UserId, User.HomeId, User.Name, User.Order, Status.Status, Status.Updated FROM Status INNER JOIN User ON Status.UserId = User.Id', function (error, results, fields) {
    res.send(JSON.stringify({users: results}));
  });
});

/* GET : Get all user status belonging to the hid */
router.get('/hid', function(req, res, next) {
  connection.query('SELECT Status.UserId, User.HomeId, User.Name, User.Order, Status.Status, Status.Updated FROM Status INNER JOIN User ON Status.UserId = User.Id WHERE HomeId = ' + req.query.hid, function (error, results, fields) {
    res.send(JSON.stringify({users: results}));
  });
});

/* GET : Update status */
router.get('/add', function(req, res) {
  connection.query('INSERT INTO `Status` (UserId, Status) VALUES (' + req.query.id + ',\'' + req.query.status + '\') ON DUPLICATE KEY UPDATE Status = \'' + req.query.status + '\', Updated = CURRENT_TIMESTAMP', function (error, results, fields) {
    res.sendStatus(200);
  });
});

module.exports = router;
