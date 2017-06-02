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
var CategoryEditorComponent = (function () {
    function CategoryEditorComponent(categoriesService) {
        this.categoriesService = categoriesService;
    }
    CategoryEditorComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.categoriesService.getCategories()
            .then(function (categories) { return _this.categories = categories; });
        $('body').on('click', '.show-stores', function (e) {
            e.preventDefault();
            var $this = $(this);
            if ($this.prop('closed') === false) {
                $this.text('Show stores »');
                $this.prop('closed', true);
            }
            else {
                $this.text('Hide stores «');
                $this.prop('closed', false);
            }
            var collapse = $this.parent().find('.stores');
            collapse.collapse('toggle');
        });
        this.createModal = $('.category-create-modal');
        this.editModal = $('.category-edit-modal');
        this.deleteModal = $('.category-delete-modal');
        this.unionModal = $('.category-union-modal');
    };
    CategoryEditorComponent.prototype.createCategory = function (name) {
        var _this = this;
        this.categoriesService.create(name)
            .then(function (category) {
            _this.insertLocalCategory(category);
            _this.createModal.modal('hide');
        });
    };
    CategoryEditorComponent.prototype.openCreateDialog = function () {
        this.createModal.modal('show');
    };
    CategoryEditorComponent.prototype.edit = function (name) {
        var _this = this;
        this.categoriesService.edit(this.editingCategory.id, name)
            .then(function (c) {
            _this.deleteLocalCategory(_this.editingCategory);
            _this.insertLocalCategory(c);
            _this.editModal.modal('hide');
        });
    };
    CategoryEditorComponent.prototype.openEditDialog = function (category) {
        this.editingCategory = category;
        this.editModal.find('.category-edit-name').val(category.name);
        this.editModal.modal('show');
    };
    CategoryEditorComponent.prototype.delete = function () {
        var _this = this;
        this.categoriesService.delete(this.deletingCategory.id)
            .then(function () {
            _this.deleteLocalCategory(_this.deletingCategory);
            _this.deleteModal.modal('hide');
        });
    };
    CategoryEditorComponent.prototype.openDeleteDialog = function (category) {
        this.deletingCategory = category;
        this.deleteModal.find('.category-delete-name').text(category.name);
        this.deleteModal.modal('show');
    };
    CategoryEditorComponent.prototype.openUnionDialog = function (unionModal) {
        unionModal.openDialog(this.categories, this);
    };
    CategoryEditorComponent.prototype.unionReplace = function (oldCategoryIds, newCategory) {
        this.categories = this.categories.filter(function (c) {
            return oldCategoryIds.indexOf(c.id) == -1;
        });
        this.insertLocalCategory(newCategory);
    };
    /*
     Util functions
     */
    CategoryEditorComponent.prototype.insertLocalCategory = function (category) {
        var pos = 0;
        for (; pos < this.categories.length; pos++) {
            if (this.categories[pos].name >= category.name) {
                break;
            }
        }
        this.categories.splice(pos, 0, category);
    };
    CategoryEditorComponent.prototype.deleteLocalCategory = function (category) {
        this.categories = this.categories.filter(function (c) { return c !== category; });
    };
    return CategoryEditorComponent;
}());
CategoryEditorComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'category-editor',
        templateUrl: './html/category-editor.component.html',
        styleUrls: ['./css/category-editor.component.css']
    }),
    __metadata("design:paramtypes", [category_service_1.CategoriesService])
], CategoryEditorComponent);
exports.CategoryEditorComponent = CategoryEditorComponent;
//# sourceMappingURL=category-editor.component.js.map