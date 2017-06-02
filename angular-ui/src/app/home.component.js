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
var alert_component_1 = require("./alert.component");
var HomeComponent = (function () {
    function HomeComponent(accountService) {
        this.accountService = accountService;
        this.accounts = [];
    }
    HomeComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.accountService.getAccounts()
            .then(function (accounts) { return _this.accounts = accounts; });
        $('.selectpicker').selectpicker({
            style: 'btn-info',
            size: 4
        });
    };
    HomeComponent.prototype.create = function (accountName, parserType) {
        var _this = this;
        this.accountService.create(accountName, parserType)
            .then(function (account) { return _this.accounts.push(account); });
    };
    HomeComponent.prototype.delete = function (accountId, index) {
        var _this = this;
        this.accountService.delete(accountId)
            .then(function (_) { return _this.accounts.splice(index, 1); });
    };
    HomeComponent.prototype.showStatementUploadDialog = function (fileInput) {
        $(fileInput).click();
    };
    HomeComponent.prototype.uploadBankStatement = function (fileInput, accountId, event, alert, trDeduplicate) {
        this.accountService.uploadBankStatement(accountId, event, function (data) {
            if (data.result === 'SUCCESS') {
                if (data.duplicates.length == 0) {
                    alert.showAlert(alert_component_1.AlertComponent.SUCCESS, 'Bank statement uploaded successfully', 3000);
                }
                else {
                    alert.showAlert(alert_component_1.AlertComponent.INFO, 'Bank statement uploaded successfully, but duplicates found', 5000);
                    trDeduplicate.setTransactions(data.duplicates, data.from, data.to, alert);
                }
            }
            else if (data.result === 'FAILED') {
                alert.showAlert(alert_component_1.AlertComponent.DANGER, 'Failed to upload bank statement file due to exception', 3000);
                console.log(data);
            }
            else if (data.result === 'NO_FILE_CHOSEN') {
                alert.showAlert(alert_component_1.AlertComponent.WARNING, 'No file was selected', 3000);
            }
            $(fileInput).val('');
        }, function (error) {
            alert.showAlert(alert_component_1.AlertComponent.DANGER, 'Failed to upload bank statement file due to exception', 3000);
            console.log(error);
            $(fileInput).val('');
        });
    };
    return HomeComponent;
}());
HomeComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'home',
        templateUrl: './html/home.component.html',
        styleUrls: ['./css/home.component.css']
    }),
    __metadata("design:paramtypes", [account_service_1.AccountService])
], HomeComponent);
exports.HomeComponent = HomeComponent;
//# sourceMappingURL=home.component.js.map