var mongoose = require('mongoose');

module.exports.init = function () {
    var roomSchema = mongoose.Schema({
        name: {type: String, required: true},
        players: [String],
        creator: {type: String, required: true}
    });

    var Room = mongoose.model('Room', roomSchema);
};
