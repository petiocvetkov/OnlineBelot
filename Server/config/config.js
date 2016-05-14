module.exports = {
    db: "mongodb://azis:azisebog@ds023042.mlab.com:23042/tuesbelotdb"
,
    port: process.env.PORT || 3000,
    websocket: {
        port: 5000
    }
};
