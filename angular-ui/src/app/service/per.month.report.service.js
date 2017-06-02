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
var PerMonthReportService = (function (_super) {
    __extends(PerMonthReportService, _super);
    function PerMonthReportService(http) {
        var _this = _super.call(this) || this;
        _this.http = http;
        _this.headers = new http_1.Headers({ 'Content-Type': 'application/json' });
        _this.reportUrl = '/services/api/reports/per-month'; // URL to web api
        return _this;
    }
    PerMonthReportService.prototype.getReport = function (from, to, accountIds) {
        return this.http
            .post(this.reportUrl, JSON.stringify({
            from: from,
            to: to,
            accountIds: accountIds
        }), { headers: this.headers })
            .toPromise()
            .then(function (response) {
            console.log(response.json());
            return response.json();
        })
            .catch(this.handleError);
    };
    return PerMonthReportService;
}(abstract_service_1.AbstractService));
PerMonthReportService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [http_1.Http])
], PerMonthReportService);
exports.PerMonthReportService = PerMonthReportService;
//# sourceMappingURL=per.month.report.service.js.map