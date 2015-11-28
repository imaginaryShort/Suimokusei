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
  connection.query('SELECT * FROM `Home`', function (error, results, fields) {
    res.send(JSON.stringify({homes: results}));
  });
});

/* GET : add new homeid and home */
router.get('/add', function(req, res, next) {
  connection.query('INSERT INTO `Home` (Name) VALUES (' + req.query.name + ')', function (error, results, fields) {
    connection.query('SELECT last_insert_id() FROM `Home`', function (error, results, fields) {
      res.send(JSON.stringify({results: results}));
    });
  });
});

module.exports = router;
