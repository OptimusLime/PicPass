var net = require('net');
var HOST = '0.0.0.0';
var PORT = 14000;

net.createServer(function(sock) {
    console.log('CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);
    sock.on('data', function(data) {

        console.log('DATA ' + sock.remoteAddress + ': ' + data);
        sock.write('You said "' + data + '"');

    });
    sock.on('close', function(data) {
        console.log('CLOSED: ' + sock.remoteAddress +' '+ sock.remotePort);
    });
    sock.on('error', function(error){
	console.log('Error received: ' + error);    
    });	
}).listen(PORT, HOST);

console.log('Server listening on ' + HOST +':'+ PORT);
