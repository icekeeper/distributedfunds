"use strict";
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var abstract_service_1 = require("./abstract.service");
var http_1 = require("@angular/http");
var TransactionsService = (function (_super) {
    __extends(TransactionsService, _super);
    function TransactionsService(http) {
        var _this = _super.call(this) || this;
        _this.http = http;
        _this.headers = new http_1.Headers({ 'Content-Type': 'application/json' });
        _this.transactionsUrl = '/services/api/transactions';
        return _this;
    }
    TransactionsService.prototype.getTransactions = function (from, to, accountIds) {
        var url = this.transactionsUrl + "?";
        if (from != null) {
            url += "from=" + from + "&";
        }
        if (to != null) {
            url += "to=" + to + "&";
        }
        if (accountIds != null && accountIds.length > 0) {
            for (var _i = 0, accountIds_1 = accountIds; _i < accountIds_1.length; _i++) {
                var accountId = accountIds_1[_i];
                url += "account_ids=" + accountId + "&";
            }
        }
        return this.http.get(url, { headers: this.headers })
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    TransactionsService.prototype.deduplicateTransactions = function (transactions, successCallback, errorCallback) {
        var _this = this;
        var transactionIds = [];
        transactions.forEach(function (tr) { return transactionIds.push(tr.id); });
        this.http.post(this.transactionsUrl + "/remove-batch", transactionIds, { headers: this.headers })
            .toPromise()
            .then(function (res) { return successCallback(res.json()); })
            .catch(function (error) {
            errorCallback(error);
            return _this.handleError(error);
        });
    };
    return TransactionsService;
}(abstract_service_1.AbstractService));
TransactionsService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [http_1.Http])
], TransactionsService);
exports.TransactionsService = TransactionsService;
//# sourceMappingURL=transactions.service.js.map