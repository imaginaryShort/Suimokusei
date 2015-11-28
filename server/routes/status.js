var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var bodyParser = require('body-parser');

var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '',
  database : 'mochi'
});

/* GET home page. */
router.get('/', function(req, res, next) {
  connection.query('SELECT * FROM `User`', function (error, results, fields) {
    res.send(results);
  });
});

/* POST home page. */
router.post('/', function(req, res) {
  connection.query('INSERT INTO `User` (Id, Name, Status) VALUES (' + req.query.id + ',\'' + req.query.name + '\',' + '\'still\')', function (error, results, fields) {
    res.sendStatus(200);
  });
});


module.exports = router;
