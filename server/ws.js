var ws = require('websocket.io');

var ws_server = ws.listen(5000, function () {
    console.log('Websocket server running');
});

var clients = {};
ws_server.on('connection', function(client) {
    console.log('connection start');
 
    client.on('message', function(request) {
        w = JSON.parse(request);
        console.log(w.UUID);
        clients[w.UUID] = client;
    });
 
    client.on('disconnect', function(){
        console.log('connection disconnect');
    });
 
    client.on('close', function(){
        console.log('connection close');
    });
 
    client.on('error', function(err){
        console.log(err);
        console.log(err.stack);
    });
});

ws_server.sendMessage = function(uuid, message){
  if(uuid in clients){
    try {
      clients[uuid].send(message);
    } catch(e) {
      console.log(e);
    }
  }
}

module.exports = ws_server;
