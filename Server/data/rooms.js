var Room = require('mongoose').model('Room');

module.exports = {
    create: function (room, callback) {
        Room.create(room, callback);
    },

    find: function (args, callback) {
        Room.find(args, callback);
    },

    findOne: function (args, callback) {
        Room.findOne(args, callback);
    }
};
