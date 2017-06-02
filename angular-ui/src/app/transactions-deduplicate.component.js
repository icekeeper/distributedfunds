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
var alert_component_1 = require("./alert.component");
var TransactionsDeduplicateComponent = (function () {
    function TransactionsDeduplicateComponent(transactionsService) {
        this.transactionsService = transactionsService;
        this.allTransactions = null;
        this.duplicates = null;
        this.from = null;
        this.to = null;
    }
    TransactionsDeduplicateComponent.prototype.setTransactions = function (transactions, from, to, alert) {
        var _this = this;
        this.allTransactions = [];
        this.duplicates = [];
        transactions.forEach(function (tr) {
            _this.allTransactions.push(tr.origin);
            _this.allTransactions = _this.allTransactions.concat(tr.duplicates);
            _this.duplicates = _this.duplicates.concat(tr.duplicates);
        });
        this.from = from;
        this.to = to;
        this.alert = alert;
    };
    TransactionsDeduplicateComponent.prototype.deduplicateTransactions = function () {
        var _this = this;
        if (this.allTransactions != null) {
            var transactions_1 = this.duplicates;
            this.transactionsService.deduplicateTransactions(transactions_1, function (data) { return _this.alert.showAlert(alert_component_1.AlertComponent.SUCCESS, 'Removed ' + transactions_1.length + ' duplicates', 3000); }, function (error) {
                console.log(error);
                _this.alert.showAlert(alert_component_1.AlertComponent.DANGER, 'Failed to remove duplicates', 3000);
            });
            this.clear();
        }
    };
    TransactionsDeduplicateComponent.prototype.clear = function () {
        this.allTransactions = null;
        this.duplicates = null;
        this.from = null;
        this.to = null;
    };
    return TransactionsDeduplicateComponent;
}());
TransactionsDeduplicateComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'transactions-deduplicate',
        templateUrl: './html/transactions-deduplicate.component.html',
    }),
    __metadata("design:paramtypes", [transactions_service_1.TransactionsService])
], TransactionsDeduplicateComponent);
exports.TransactionsDeduplicateComponent = TransactionsDeduplicateComponent;
//# sourceMappingURL=transactions-deduplicate.component.js.map