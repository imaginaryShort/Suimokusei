var ws = require('websocket.io');
var server = ws.listen(5000, function () {
    console.log('Server running');
});

var clients = {};
// クライアントからの接続イベントを処理
server.on('connection', function(client) {
    console.log('connection start');
 
    // クライアントからのメッセージ受信イベントを処理
    client.on('message', function(request) {
        //送られてきたデータをただ送り返す
        w = JSON.parse(request);
        console.log(w.UUID);
        clients[w.UUID] = client;
        clients[w.UUID].send("Hello from nodejs");
    });
 
    // クライアントが切断したときの処理
    client.on('disconnect', function(){
        console.log('connection disconnect');
    });
 
    // 通信がクローズしたときの処理
    client.on('close', function(){
        console.log('connection close');
    });
 
    // エラーが発生した場合
    client.on('error', function(err){
        console.log(err);
        console.log(err.stack);
    });
});
