var express = require('express'),
    app = express(),
    bodyParser = require('body-parser'),
    cookieParser = require('cookie-parser'),
    passport = require('passport'),
    session = require('express-session');

module.exports = (function(app) {
    app.use(express.static(__dirname + "/"));
    app.use(cookieParser());
    app.use(bodyParser.json());
    app.use(
        session({
            secret: 'keyboard cat',
            saveUninitialized: true,
            resave: true
        }));
    app.use(passport.initialize());
    app.use(passport.session());

    return app;
})(app);
