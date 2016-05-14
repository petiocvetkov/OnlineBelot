var passport = require('passport');

module.exports = {
    login: function(req, res, next) {
        var auth = passport.authenticate('local', function(err, user) {
            if (err) return next(err);
            if (!user) {
                res.status(400);
                res.send({reason: "User not found!"});
            }

            req.logIn(user, function(err) {
                if (err) return next(err);
                res.status(200);
                res.send(user);
            });
        });

        auth(req, res, next);
    },
    logout: function(req, res, next) {
        req.logout();
        res.status(200);
        res.send();
    },
    isAuthenticated: function(req, res, next) {
        if (!req.isAuthenticated()) {
            res.status(400);
            res.send({reason: "User is not logged in."});
        }
        else {
            next();
        }
    }
};
