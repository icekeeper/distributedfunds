"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require("@angular/core");
var router_1 = require("@angular/router");
var home_component_1 = require("./home.component");
var reports_component_1 = require("./reports.component");
var period_report_component_1 = require("./period-report.component");
var category_editor_component_1 = require("./category-editor.component");
var stores_editor_component_1 = require("./stores-editor.component");
var transactions_component_1 = require("./transactions.component");
var per_month_report_component_1 = require("./per-month-report.component");
var routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: home_component_1.HomeComponent },
    { path: 'reports', component: reports_component_1.ReportsComponent },
    { path: 'reports/period/:from/:to', component: period_report_component_1.PeriodReportComponent },
    { path: 'reports/per-month/:from/:to', component: per_month_report_component_1.PerMonthReportComponent },
    { path: 'categories', component: category_editor_component_1.CategoryEditorComponent },
    { path: 'stores', component: stores_editor_component_1.StoresEditorComponent },
    { path: 'transactions', component: transactions_component_1.TransactionsComponent }
];
var AppRoutingModule = (function () {
    function AppRoutingModule() {
    }
    return AppRoutingModule;
}());
AppRoutingModule = __decorate([
    core_1.NgModule({
        imports: [router_1.RouterModule.forRoot(routes)],
        exports: [router_1.RouterModule]
    })
], AppRoutingModule);
exports.AppRoutingModule = AppRoutingModule;
//# sourceMappingURL=app-routing.module.js.map