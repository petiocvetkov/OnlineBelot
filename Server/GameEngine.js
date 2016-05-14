var rooms = require('./data/rooms'),
    errorManager = require('./utils/errorManager');

module.exports = function (websocketServer) {
    var sockets = [],
        activeRooms = [],
        cards = ["7", "8", "9", "10", "J", "Q", "K", "A"],
        paints = ["club", "diamond", "heart", "spade"];

    var socketsContain = function (data) {
        for (var i = 0; i < sockets.length; i++) {
            if (sockets[i].playerName == data.playerName &&
                sockets[i].roomId == data.roomId) {
                    return true;
            }
        }

        return false;
    };

    var addNewSocket = function(socketData) {
        rooms.findOne({_id: socketData.roomId}, function (err, room) {
            if (err) {
                websocketServer.send(socketData.socket, errorManager.getError("Failed due to room error."));
                websocketServer.close(socketData.socket, handleSocketClose, "server");
                return;
            }

            if (!room) {
                websocketServer.send(socketData.socket, errorManager.getError("Wrong room id."));
                websocketServer.close(socketData.socket, handleSocketClose, "server");
                return;
            }

            if (room.players.indexOf(socketData.playerName) < 0) {
                room.players.push(socketData.playerName);
                room.save(function (err) {
                    if (err) {
                        console.log(err);
                        websocketServer.send(socketData.socket, errorManager.getError(err));
                    }
                });
            }
        });

        if (!socketsContain(socketData)) {
            sockets.push(socketData);
        }
    };

    function randomIntFromInterval(min,max)
    {
        return Math.floor(Math.random()*(max-min+1)+min);
    }

    var dealCards = function functionName(cardsLeft) {
        var playerCards = [];
        for (var p = 0; p < 8; p++) {
            var cardIndex = randomIntFromInterval(0, cardsLeft.length - 1);
            playerCards.push(cardsLeft[cardIndex]);
            cardsLeft.splice(cardIndex, 1);
        }

        return playerCards;
    };

    var getSocketsByRoomId = function (roomId) {
        var result = [];
        for (var i = 0; i < sockets.length; i++) {
            if (sockets[i].roomId == roomId) {
                result.push(sockets[i]);
            }
        }

        return result;
    };

    var findActiveRoomIndexById = function (roomId) {
        for (var i = 0; i < activeRooms.length; i++) {
            if (activeRooms[i].roomId == roomId) {
                return i;
            }
        }

        return -1;
    };

    var getRoundWinner = function (tableCards, trump) {
        var roundPaint = tableCards[0].paint;
        var highestCard = tableCards[0];

        for (var i = 0; i < tableCards.length; i++) {
            if (tableCards[i].paint.valueOf() == roundPaint.valueOf()) {
                if (cards.indexOf(tableCards[i].card) > cards.indexOf(highestCard.card)) {
                    highestCard = tableCards[i];
                }
            } else if (tableCards[i].paint.valueOf() == trump.valueOf()) {
                if (highestCard.paint.valueOf() == trump.valueOf()) {
                    if (cards.indexOf(tableCards[i].card) > cards.indexOf(highestCard.card)) {
                        highestCard = tableCards[i];
                    }
                } else {
                    highestCard = tableCards[i];
                }
            }
        }

        return highestCard;
    };

    var getRoomPlayState = function (room, messageData) {
        if (messageData.bid) {
            //IMPLEMENT LOGIC ABOUT BIDDING IF !DEAD
        } if (messageData.play) {
            var playData = {
                player: messageData.playerName,
                card: messageData.play.card,
                paint: messageData.play.paint
            };

            room.tableCards.push(playData);
            if (room.tableCards.length == 4) {
                room.roundWinner = getRoundWinner(room.tableCards, room.trump).player;
                room.tableCards = [];
            }
        }

        return room;
    };

    var getPlayerActiveRoomData = function (messageData) {
        var index = findActiveRoomIndexById(messageData.roomId);
        var playerData = {
            team: undefined,
            name: messageData.playerName.valueOf()
        };

        var room;
        if (index >= 0) {
            room = activeRooms[index];
        }

        if (!room) {
            room = {
                roomId: messageData.roomId.valueOf(),
                players: [],
                gameStarted: false,
                tableCards: [],
                trump: "spade",
                roundWinner: ""
            };


            playerData.team = 1;
            room.players.push(playerData);
            console.log(playerData.socket);
            activeRooms.push(room);
        } else {
            var isInRoom = false;
            for (var i = 0; i < room.players.length; i++) {
                if (room.players[i].name.valueOf() == playerData.name.valueOf()) {
                    isInRoom = true;
                }
            }

            if(!isInRoom) {
                var team = room.players.length < 2 ? 1 : 2;
                playerData.team = team;
                room.players.push(playerData);
            }
        }

        return {
            room: room,
            index: index
        };
    };

    var gameStep = function (messageData) {
        var roomData = getPlayerActiveRoomData(messageData),
            room = roomData.room,
            roomIndex = roomData.index,
            playerSockets = getSocketsByRoomId(room.roomId);

        if (!room.gameStarted && room.players.length == 4) {
            var cardsLeft = [];
            room.gameStarted = true;

            for (var i = 0; i < cards.length; i++) {
                for (var j = 0; j < paints.length; j++) {
                    cardsLeft.push({
                        card: cards[i],
                        paint: paints[j]
                    });
                }
            }

            for (var p = 0; p < playerSockets.length; p++) {
                console.log("Dealing cards to player: " + playerSockets[p].playerName);
                var playerCards = dealCards(cardsLeft);
                websocketServer.send(playerSockets[p].socket, JSON.stringify(playerCards));
            }

            return;
        }
        else if (room.gameStarted) {
            room = getRoomPlayState(room, messageData);
            activeRooms[roomIndex] = room;
        }

        for (var k = 0; k < playerSockets.length; k++) {
            websocketServer.send(playerSockets[k].socket, JSON.stringify(room));
        }
    };

    var handleSocketMessage = function (websocket, data) {
        try {
            data = JSON.parse(data);
        } catch (e) {
            websocketServer.send(websocket, "Invalid json!");
            return;
        }

        var socketData = {
            socket: websocket,
            playerName: data.playerName,
            roomId: data.roomId,
            play: data.play,
            bid: data.bid
        };

        addNewSocket(socketData);
        socketData.message = data.message;
        gameStep(socketData);
    };

    var handleSocketClose = function (websocket, reason) {
        sockets.splice(sockets.indexOf(websocket), 1);
        if (reason == "server") {
            return;
        }

        var activeRoomIndex = findActiveRoomIndexById(websocket.roomId);
        activeRooms.splice(activeRoomIndex, 1);

        var roomData;
        for (var i = 0; i < sockets.length; i++) {
            if (!websocket.belotSocketData) {
                console.log("Socket belot data is undefined...");
                return;
            }

            roomData = websocket.belotSocketData;

            if(sockets[i].playerName == roomData.creator) {
                break;
            }
        }

        if (!roomData) {
            return;
        }

        rooms.findOne({_id: websocket.belotSocketData.roomId}, function (err, room) {
            if (err) {
                console.log(err);
            }

            if (!room) {
                console.log("Can't delete room, it does not exist.");
            }

            if (room.players.length === 1) {
                room.remove(function (err) {
                    if (err) {
                        console.log("Could not save room due to error:");
                        console.log(err);
                    }
                });
                console.log("Room " + roomData.roomId + " deleted.");
            } else {
                var index = room.players.indexOf(roomData.playerName);
                room.players.splice(index, 1);
                console.log("Player " + roomData.playerName + " removed from room " + roomData.roomId + ".");
                room.save(function (error) {
                    if (err) {
                        console.log("Could not save room due to error:");
                        console.log(err);
                    }
                });
            }
        });
    };

    return {
        start: function () {
            websocketServer.accept(function (websocket) {
                console.log("Connection accepted...");
                websocketServer.socketSettings.onMessage(websocket, handleSocketMessage);
            });
        }
    };
};
