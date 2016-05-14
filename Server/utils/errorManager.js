module.exports = {
    getError: function (arg) {
        return JSON.stringify({
            reason: arg
        });
    }
};
