"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require("@angular/core");
var platform_browser_1 = require("@angular/platform-browser");
var forms_1 = require("@angular/forms");
var http_1 = require("@angular/http");
var app_routing_module_1 = require("./app-routing.module");
var app_component_1 = require("./app.component");
var account_service_1 = require("./service/account.service");
var home_component_1 = require("./home.component");
var reports_component_1 = require("./reports.component");
var period_report_component_1 = require("./period-report.component");
var period_report_service_1 = require("./service/period.report.service");
var category_editor_component_1 = require("./category-editor.component");
var category_service_1 = require("./service/category.service");
var stores_editor_component_1 = require("./stores-editor.component");
var store_service_1 = require("./service/store.service");
var alert_component_1 = require("./alert.component");
var transactions_deduplicate_component_1 = require("./transactions-deduplicate.component");
var transactions_service_1 = require("./service/transactions.service");
var transactions_component_1 = require("./transactions.component");
var category_union_modal_component_1 = require("./category.union.modal.component");
var per_month_report_component_1 = require("./per-month-report.component");
var per_month_report_service_1 = require("./service/per.month.report.service");
var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    core_1.NgModule({
        imports: [
            platform_browser_1.BrowserModule,
            forms_1.FormsModule,
            http_1.HttpModule,
            // InMemoryWebApiModule.forRoot(InMemoryDataService),
            app_routing_module_1.AppRoutingModule
        ],
        declarations: [
            app_component_1.AppComponent,
            home_component_1.HomeComponent,
            reports_component_1.ReportsComponent,
            period_report_component_1.PeriodReportComponent,
            per_month_report_component_1.PerMonthReportComponent,
            category_editor_component_1.CategoryEditorComponent,
            stores_editor_component_1.StoresEditorComponent,
            transactions_component_1.TransactionsComponent,
            alert_component_1.AlertComponent,
            transactions_deduplicate_component_1.TransactionsDeduplicateComponent,
            category_union_modal_component_1.CategoryUnionComponent
        ],
        providers: [
            account_service_1.AccountService,
            period_report_service_1.PeriodReportService,
            per_month_report_service_1.PerMonthReportService,
            category_service_1.CategoriesService,
            store_service_1.StoresService,
            transactions_service_1.TransactionsService
        ],
        bootstrap: [app_component_1.AppComponent]
    })
], AppModule);
exports.AppModule = AppModule;
//# sourceMappingURL=app.module.js.map