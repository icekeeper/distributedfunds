"use strict";
var AbstractService = (function () {
    function AbstractService() {
    }
    AbstractService.prototype.handleError = function (error) {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    };
    return AbstractService;
}());
exports.AbstractService = AbstractService;
//# sourceMappingURL=abstract.service.js.map