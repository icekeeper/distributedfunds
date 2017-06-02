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
var per_month_report_service_1 = require("./service/per.month.report.service");
var PerMonthReportComponent = (function () {
    function PerMonthReportComponent(reportService, accountService, route) {
        this.reportService = reportService;
        this.accountService = accountService;
        this.route = route;
        this.accounts = [];
    }
    PerMonthReportComponent.prototype.ngOnInit = function () {
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
        var component = this;
        $('#chart-mode').bootstrapSwitch({
            onText: 'line',
            offText: 'stream',
            labelText: 'mode',
            onColor: 'primary',
            offColor: 'success',
            size: 'small',
            onSwitchChange: function () {
                component.showReport();
            }
        });
        $('#chart-type').bootstrapSwitch({
            onText: 'percentage',
            offText: 'absolute',
            labelText: 'type',
            onColor: 'primary',
            offColor: 'success',
            size: 'small',
            onSwitchChange: function () {
                component.showReport();
            }
        });
    };
    PerMonthReportComponent.prototype.showReport = function () {
        if (this.chart != null) {
            this.chart.destroy();
        }
        this.chart = this.getChartGenerator()(this, this.getChartMode());
    };
    PerMonthReportComponent.prototype.getChartMode = function () {
        return $('#chart-mode').bootstrapSwitch('state') ? 'line' : 'stream';
    };
    PerMonthReportComponent.prototype.getChartGenerator = function () {
        return $('#chart-type').bootstrapSwitch('state')
            ? this.generatePercentageReport
            : this.generateAbsoluteReport;
    };
    PerMonthReportComponent.prototype.generatePercentageReport = function (component, mode) {
        return c3.generate({
            bindto: '#chart',
            data: {
                xFormat: '%Y-%m-%d',
                json: component.report.percentageChart,
                keys: {
                    x: 'date',
                    value: component.report.percentageChartLabels
                },
                types: mode === 'stream' ? component.generateTypes() : undefined,
                groups: mode === 'stream' ? component.generateGroups() : undefined
            },
            size: {
                height: 750
            },
            axis: {
                x: {
                    type: 'timeseries',
                    tick: {
                        format: '%m.%Y'
                    }
                }
            },
            tooltip: {
                grouped: false,
                format: {
                    value: function (value, ratio, id) {
                        return value + "%";
                    }
                }
            }
        });
    };
    PerMonthReportComponent.prototype.generateAbsoluteReport = function (component, mode) {
        return c3.generate({
            bindto: '#chart',
            data: {
                xFormat: '%Y-%m-%d',
                json: component.report.absoluteChart,
                keys: {
                    x: 'date',
                    value: component.report.absoluteChartLabels
                },
                types: mode === 'stream' ? component.generateTypes() : undefined,
                groups: mode === 'stream' ? component.generateGroups() : undefined
            },
            size: {
                height: 750
            },
            axis: {
                x: {
                    type: 'timeseries',
                    tick: {
                        format: '%m.%Y'
                    }
                }
            },
            tooltip: {
                grouped: false,
                format: {
                    value: function (value, ratio, id) {
                        return d3.format(",")(value);
                    }
                }
            }
        });
    };
    PerMonthReportComponent.prototype.generateTypes = function () {
        var types = {};
        this.report.percentageChartLabels.forEach(function (l) {
            types[l] = 'area';
        });
        return types;
    };
    PerMonthReportComponent.prototype.generateGroups = function () {
        var groups = [];
        this.report.percentageChartLabels.forEach(function (l) {
            groups.push(l);
        });
        return [groups];
    };
    PerMonthReportComponent.prototype.ngOnDestroy = function () {
        this.sub.unsubscribe();
    };
    return PerMonthReportComponent;
}());
PerMonthReportComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'reports',
        templateUrl: './html/per-month-report.component.html',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [per_month_report_service_1.PerMonthReportService,
        account_service_1.AccountService,
        router_1.ActivatedRoute])
], PerMonthReportComponent);
exports.PerMonthReportComponent = PerMonthReportComponent;
//# sourceMappingURL=per-month-report.component.js.map