var WebSocketServer = require("ws").Server,
    config = require('./config');

module.exports = function (httpServer) {
    var wss = new WebSocketServer({server: httpServer});
    console.log("Websocket server created");

    var logError = function (error) {
        if (error) {
            console.log("Websocket send failed due to error:");
            console.log(error);
        }
    };

    return {
        accept: function (callback) {
            wss.on('connection', callback);
        },
        send: function functionName(websocket, message) {
            websocket.send(message, logError);
        },
        close: function (websocket, callback, reason) {
            callback(websocket, reason);
            websocket.close();
        },
        socketSettings: {
            onMessage: function (websocket, callback) {
                websocket.on('message', function (data) {
                    callback(websocket, data);
                });
            },
            onOpen: function (websocket, callback) {
                websocket.onOpen(callback);
            }
        }
    };
};
