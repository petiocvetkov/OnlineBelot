var rooms = require('../data/rooms');

module.exports = {
    create: function (req, res, next) {
        if (!req.body.name) {
            res.status(400);
            return res.send({reason: "Bad room!"});
        }

        var room = {
            name: req.body.name,
            creator: req.body.creator,
            players: [req.body.creator]
        };

        rooms.create(room, function (err, room) {
            if (err) {
                res.status(400);
                return res.send({reason: err.toString()});
            }

            return res.send(room);
        });
    },

    get: function (req, res, next) {
        rooms.find(req.body, function (err, rooms) {
            if (err) {
                res.status(400);
                return res.send({reason: "Bad room!"});
            }

            return res.send(rooms);
        });
    },

    join: function (req, res, next) {
        rooms.findOne({_id: req.body.id}, function (err, room) {
            if (err) {
                res.status(400);
                return res.send({reason: err.toString()});
            }

            if (!room) {
                res.status(400);
                return res.send({reason: "Room does not exist!"});
            }

            for (var i = 0; i < room.players.length; i++) {
                if (room.players[i] == req.body.player) {
                    res.status(400);
                    return res.send({reason: "User is already in that room!"});
                }
            }

            room.players.push(req.body.player);
            room.save(function (err) {
                if (err) {
                    res.status(400);
                    res.send({reason: "Could not update room data!"});
                }
            });

            return res.send(room);
        });
    }
};
