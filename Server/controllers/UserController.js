var encryption = require('../utils/encryption.js'),
    users = require('../data/users.js');

module.exports = {
    postRegister: function (req, res, next) {
        var newUserData = req.body;
        newUserData.salt = encryption.generateSalt();
        newUserData.hashPass = encryption.generateHashedPassword(newUserData.salt, newUserData.password);
        users.create(newUserData, function(err, user) {
            if (err) {
                if (err.code == 11000) {
                    res.status(400);
                    return res.send({reason: "Failed to register duplicate username: " + newUserData.username});
                }

                console.log('Failed to register new user: ' + err);
                res.status(400);
                return res.send({reason: err.toString()});
            }

            req.logIn(user, function(err) {
                if (err) {
                    res.status(400);
                    return res.send({reason: err.toString()});
                }
                else {
                    res.redirect('/');
                }
            });
        });
    },
};
