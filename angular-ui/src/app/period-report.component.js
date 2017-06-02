"use strict";
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
var account_service_1 = require("./service/account.service");
var router_1 = require("@angular/router");
var period_report_service_1 = require("./service/period.report.service");
var PeriodReportComponent = (function () {
    function PeriodReportComponent(reportService, accountService, route) {
        this.reportService = reportService;
        this.accountService = accountService;
        this.route = route;
        this.accounts = [];
    }
    PeriodReportComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.sub = this.route.params.subscribe(function (params) {
            _this.from = params['from'];
            _this.to = params['to'];
        });
        this.sub = this.route.queryParams.subscribe(function (params) {
            _this.accountIds = params['accountIds'].split(',');
            _this.accountIds = _this.accountIds.filter(function (id) { return id != null && id != ''; });
        });
        this.accountService.getAccounts()
            .then(function (accounts) { return _this.accounts = accounts; });
        this.reportService.getReport(this.from, this.to, this.accountIds)
            .then(function (report) {
            _this.report = report;
            _this.showReport();
        });
    };
    PeriodReportComponent.prototype.showReport = function () {
        var charData = [];
        var categoryCache = [];
        this.report.categoryReports.forEach(function (cReport) {
            charData.push([cReport.category, cReport.amount]);
            categoryCache[cReport.category] = cReport.id;
        });
        var chart = c3.generate({
            bindto: '#chart',
            data: {
                columns: charData,
                type: 'donut',
                onclick: function (e) {
                    location.hash = "#" + 'link-' + categoryCache[e.name];
                }
            },
            legend: {
                position: 'right'
            }
        });
    };
    PeriodReportComponent.prototype.ngOnDestroy = function () {
        this.sub.unsubscribe();
    };
    return PeriodReportComponent;
}());
PeriodReportComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'reports',
        templateUrl: './html/period-report.component.html',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [period_report_service_1.PeriodReportService,
        account_service_1.AccountService,
        router_1.ActivatedRoute])
], PeriodReportComponent);
exports.PeriodReportComponent = PeriodReportComponent;
//# sourceMappingURL=period-report.component.js.map