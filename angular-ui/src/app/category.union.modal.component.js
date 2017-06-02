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
var category_service_1 = require("./service/category.service");
var CategoryUnionComponent = (function () {
    function CategoryUnionComponent(categoriesService) {
        this.categoriesService = categoriesService;
    }
    CategoryUnionComponent.prototype.ngOnInit = function () {
        this.modal = $('.category-union-modal');
    };
    CategoryUnionComponent.prototype.ngAfterViewChecked = function () {
        if (this.categories != null && !this.selectCreated) {
            this.selectCreated = true;
            $('.selectpicker').selectpicker({
                style: 'btn-info',
                size: 4
            });
        }
        if (this.selectNeedRender) {
            this.selectNeedRender = false;
            $('.selectpicker').selectpicker('refresh');
            $('.category-union-name').val('');
        }
    };
    CategoryUnionComponent.prototype.openDialog = function (categories, categoryEditor) {
        this.categories = categories;
        this.categoryEditor = categoryEditor;
        this.modal.modal('show');
        this.selectNeedRender = true;
    };
    CategoryUnionComponent.prototype.union = function (name, categoryIdsSelect) {
        var _this = this;
        var rawCategoryIds = $(categoryIdsSelect).val();
        if (rawCategoryIds == null || rawCategoryIds.length < 2) {
            return;
        }
        var categoryIds = [];
        rawCategoryIds.forEach(function (id) { return categoryIds.push(+id); });
        this.categoriesService.union(name, categoryIds)
            .then(function (category) {
            _this.categoryEditor.unionReplace(categoryIds, category);
            _this.modal.modal('hide');
        });
    };
    return CategoryUnionComponent;
}());
CategoryUnionComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'category-union-modal',
        templateUrl: './html/category.union.modal.html',
    }),
    __metadata("design:paramtypes", [category_service_1.CategoriesService])
], CategoryUnionComponent);
exports.CategoryUnionComponent = CategoryUnionComponent;
//# sourceMappingURL=category.union.modal.component.js.map