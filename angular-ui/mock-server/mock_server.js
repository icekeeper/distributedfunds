var http = require('http');
var fs = require('fs');

http.createServer(function (req, res) {
  console.log('Requested path is ' + req.url);
  if (req.url.includes('..')) {
    console.log('Bad url');
    res.end('Bad url');
    return;
  }
  var dataFile = (req.url.startsWith('/') ? '.' : '') + req.url + '.json';
  if (!fs.existsSync(dataFile) || !fs.lstatSync(dataFile).isFile()) {
    console.log('No corresponding file found');
    res.end('No data');
    return;
  }
  console.log('Returning content of ' + dataFile);
  res.writeHead(200, {'Content-Type': 'application/json'});
  var data = fs.readFileSync(dataFile);
  res.end(data);
}).listen(8090);
