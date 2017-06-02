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
var store_service_1 = require("./service/store.service");
var category_service_1 = require("./service/category.service");
var StoresEditorComponent = (function () {
    function StoresEditorComponent(storesService, categoriesService) {
        this.storesService = storesService;
        this.categoriesService = categoriesService;
    }
    StoresEditorComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.storesService.getStores()
            .then(function (stores) {
            _this.stores = stores;
            console.log(stores);
            _this.storeShowEditForm = [];
            for (var i = 0; i < _this.stores.length; i++) {
                _this.storeShowEditForm.push(false);
            }
        });
        this.categoriesService.getCategories()
            .then(function (categories) { return _this.categories = categories; });
    };
    StoresEditorComponent.prototype.ngAfterViewChecked = function () {
        if (this.categories != null) {
            $('.selectpicker').selectpicker({
                style: 'btn-info',
                size: 4
            });
        }
    };
    StoresEditorComponent.prototype.changeCategory = function (store) {
        return this.storesService.changeCategory(store.id, store.category.id);
    };
    return StoresEditorComponent;
}());
StoresEditorComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'category-editor',
        templateUrl: './html/stores-editor.component.html',
        styleUrls: ['./css/store-editor.component.css']
    }),
    __metadata("design:paramtypes", [store_service_1.StoresService,
        category_service_1.CategoriesService])
], StoresEditorComponent);
exports.StoresEditorComponent = StoresEditorComponent;
//# sourceMappingURL=stores-editor.component.js.map