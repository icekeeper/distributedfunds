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
var moment = require("moment");
var ReportsComponent = (function () {
    function ReportsComponent(accountService, router) {
        this.accountService = accountService;
        this.router = router;
    }
    ReportsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.accountService.getAccounts()
            .then(function (accounts) { return _this.accounts = accounts; });
        $('#period-report-panel').find('.input-group.date').datepicker({
            format: "dd.mm.yyyy",
            todayHighlight: true
        });
        $('#per-month-report-panel').find('.input-group.date').datepicker({
            format: "mm.yyyy",
            startView: 1,
            minViewMode: 1,
            todayHighlight: true
        });
    };
    ReportsComponent.prototype.ngAfterViewChecked = function () {
        if (this.accounts != null) {
            $('.selectpicker').selectpicker({
                style: 'btn-info',
                size: 4
            });
        }
    };
    ReportsComponent.prototype.showPeriodReport = function (from, to, accountIdsSelect) {
        var accountIds = $(accountIdsSelect).val();
        console.log('showing report for accounts: ' + accountIds);
        if (!from || !to)
            return;
        from = moment(from, 'DD.MM.YYYY').format('YYYY-MM-DD');
        to = moment(to, 'DD.MM.YYYY').format('YYYY-MM-DD');
        if (!accountIds) {
            accountIds = [];
        }
        this.router.navigate(['/reports/period', from, to], { queryParams: { accountIds: accountIds } })
            .catch(function (error) { return console.error('An error occurred', error); });
    };
    ReportsComponent.prototype.showPerMonthReport = function (from, to, accountIdsSelect) {
        var accountIds = $(accountIdsSelect).val();
        console.log('showing report for accounts: ' + accountIds);
        if (!from || !to)
            return;
        from = moment(from, 'MM.YYYY').format('YYYY-MM-DD');
        to = moment(to, 'MM.YYYY').format('YYYY-MM-DD');
        if (!accountIds) {
            accountIds = [];
        }
        this.router.navigate(['/reports/per-month', from, to], { queryParams: { accountIds: accountIds } })
            .catch(function (error) { return console.error('An error occurred', error); });
    };
    return ReportsComponent;
}());
ReportsComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'reports',
        templateUrl: './html/reports.component.html',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [account_service_1.AccountService,
        router_1.Router])
], ReportsComponent);
exports.ReportsComponent = ReportsComponent;
//# sourceMappingURL=reports.component.js.map