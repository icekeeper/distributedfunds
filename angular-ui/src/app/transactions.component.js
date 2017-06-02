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
var transactions_service_1 = require("./service/transactions.service");
var account_service_1 = require("./service/account.service");
var alert_component_1 = require("./alert.component");
var moment = require("moment");
var TransactionsComponent = (function () {
    function TransactionsComponent(transactionsService, accountService) {
        this.transactionsService = transactionsService;
        this.accountService = accountService;
    }
    TransactionsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.accountService.getAccounts()
            .then(function (accounts) { return _this.accounts = accounts; });
        $('#transactions-panel').find('.input-group.date').datepicker({
            format: "dd.mm.yyyy",
            todayHighlight: true
        });
    };
    TransactionsComponent.prototype.ngAfterViewChecked = function () {
        if (this.accounts != null) {
            $('.selectpicker').selectpicker({
                style: 'btn-info',
                size: 4
            });
        }
    };
    TransactionsComponent.prototype.showTransactions = function (from, to, accountIdsSelect, alert) {
        var _this = this;
        var accountIds = $(accountIdsSelect).val();
        if (from != "") {
            from = moment(from, 'DD.MM.YYYY').format('YYYY-MM-DD');
        }
        else {
            from = null;
        }
        if (to != "") {
            to = moment(to, 'DD.MM.YYYY').format('YYYY-MM-DD');
        }
        else {
            to = null;
        }
        this.transactionsService.getTransactions(from, to, accountIds)
            .then(function (transactions) {
            _this.transactions = transactions;
            if (transactions.length == 0) {
                alert.showAlert(alert_component_1.AlertComponent.WARNING, 'No transactions found for specified period', 3000);
            }
        });
    };
    return TransactionsComponent;
}());
TransactionsComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'transactions',
        templateUrl: './html/transactions.component.html',
        styleUrls: ['./css/store-editor.component.css']
    }),
    __metadata("design:paramtypes", [transactions_service_1.TransactionsService,
        account_service_1.AccountService])
], TransactionsComponent);
exports.TransactionsComponent = TransactionsComponent;
//# sourceMappingURL=transactions.component.js.map