var net = require('net');

var client = net.connect({port: 14000},
    function() { //'connect' listener
  console.log('client connected');
  client.write('world!\r\n');
});

client.on('data', function(data) {
  console.log(data.toString());
  client.end();
});

client.on('end', function() {
  console.log('client disconnected');
});

client.on('error', function(err)
{
	console.log(err.toString());
});
