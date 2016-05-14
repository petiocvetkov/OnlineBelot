var express = require("express"),
    app = require('./config/express'),
    config = require('./config/config.js'),
    http = require('http');

app.server = http.createServer();

require('./config/mongoose')(config);
require('./config/passport')();
require('./config/router')(app);
var websocketServer = require('./config/websocket_server')(app.server);
require('./GameEngine')(websocketServer).start();

app.server.on('request', app);
app.server.listen(config.port);
console.log("Server is runnig on port: " + config.port);
