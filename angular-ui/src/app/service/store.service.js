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
var abstract_service_1 = require("./abstract.service");
var StoresService = (function (_super) {
    __extends(StoresService, _super);
    function StoresService(http) {
        var _this = _super.call(this) || this;
        _this.http = http;
        _this.headers = new http_1.Headers({ 'Content-Type': 'application/json' });
        _this.storesUrl = '/services/api/store'; // URL to web api
        return _this;
    }
    StoresService.prototype.getStores = function () {
        return this.http.get(this.storesUrl, { headers: this.headers })
            .toPromise()
            .then(function (response) {
            return response.json();
        })
            .catch(this.handleError);
    };
    StoresService.prototype.changeCategory = function (storeId, categoryId) {
        var url = this.storesUrl + "/" + storeId + "/category/" + categoryId;
        return this.http.post(url, { headers: this.headers })
            .toPromise()
            .then(function (res) {
            return res.json();
        })
            .catch(this.handleError);
    };
    return StoresService;
}(abstract_service_1.AbstractService));
StoresService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [http_1.Http])
], StoresService);
exports.StoresService = StoresService;
//# sourceMappingURL=store.service.js.map