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
var http_1 = require("@angular/http");
require("rxjs/add/operator/toPromise");
var abstract_service_1 = require("./abstract.service");
var AccountService = (function (_super) {
    __extends(AccountService, _super);
    function AccountService(http) {
        var _this = _super.call(this) || this;
        _this.http = http;
        _this.headers = new http_1.Headers({ 'Content-Type': 'application/json' });
        _this.accountsUrl = '/services/api/account';
        _this.bankStatementsUrl = '/services/api/bank/statement';
        return _this;
    }
    AccountService.prototype.getAccounts = function () {
        return this.http.get(this.accountsUrl, { headers: this.headers })
            .toPromise()
            .then(function (response) {
            return response.json();
        })
            .catch(this.handleError);
    };
    AccountService.prototype.getAccount = function (id) {
        var url = this.accountsUrl + "/" + id;
        return this.http.get(url, { headers: this.headers })
            .toPromise()
            .then(function (response) { return response.json().data; })
            .catch(this.handleError);
    };
    AccountService.prototype.delete = function (id) {
        var url = this.accountsUrl + "/" + id;
        return this.http.delete(url, { headers: this.headers })
            .toPromise()
            .then(function () { return null; })
            .catch(this.handleError);
    };
    AccountService.prototype.create = function (name, parserType) {
        return this.http
            .post(this.accountsUrl, JSON.stringify({ name: name, parserType: parserType }), { headers: this.headers })
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    AccountService.prototype.uploadBankStatement = function (accountId, event, successCallback, errorCallback) {
        var _this = this;
        var fileList = event.target.files;
        if (fileList.length > 0) {
            var file = fileList[0];
            var formData = new FormData();
            formData.append('statement', file, file.name);
            var headers = new http_1.Headers();
            headers.append('Content-Type', 'multipart/form-data');
            headers.append('Accept', 'application/json');
            var options = new http_1.RequestOptions({ headers: headers });
            this.http.post(this.bankStatementsUrl + "/upload/" + accountId, formData, options)
                .toPromise()
                .then(function (res) { return successCallback(res.json()); })
                .catch(function (error) {
                errorCallback(error);
                return _this.handleError(error);
            });
        }
    };
    return AccountService;
}(abstract_service_1.AbstractService));
AccountService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [http_1.Http])
], AccountService);
exports.AccountService = AccountService;
//# sourceMappingURL=account.service.js.map