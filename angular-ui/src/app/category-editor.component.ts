import {Component, OnInit} from "@angular/core";
import {ExtendedCategory} from "./model/extended.category";
import {CategoriesService} from "./service/category.service";
import {CategoryUnionComponent} from "./category.union.modal.component";

@Component({
    moduleId: module.id,
    selector: 'category-editor',
    templateUrl: './html/category-editor.component.html',
    styleUrls: ['./css/category-editor.component.css']
})
export class CategoryEditorComponent implements OnInit {
    categories: ExtendedCategory[];

    private createModal: any;

    private editingCategory: ExtendedCategory;
    private editModal: any;

    private deletingCategory: ExtendedCategory;
    private deleteModal: any;

    private unionModal: any;

    constructor(private categoriesService: CategoriesService) {
    }

    ngOnInit(): void {
        this.categoriesService.getCategories()
            .then(categories => this.categories = categories);
        $('body').on('click', '.show-stores', function (e) {
            e.preventDefault();
            var $this = $(this);
            if ($this.prop('closed') === false) {
                $this.text('Show stores »');
                $this.prop('closed', true);
            } else {
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
    }

    createCategory(name: string): void {
        this.categoriesService.create(name)
            .then(category => {
                this.insertLocalCategory(category);
                this.createModal.modal('hide');
            });
    }

    openCreateDialog(): void {
        this.createModal.modal('show');
    }

    edit(name: string): void {
        this.categoriesService.edit(this.editingCategory.id, name)
            .then(c => {
                this.deleteLocalCategory(this.editingCategory);
                this.insertLocalCategory(c);
                this.editModal.modal('hide');
            });
    }

    openEditDialog(category: ExtendedCategory): void {
        this.editingCategory = category;
        this.editModal.find('.category-edit-name').val(category.name);
        this.editModal.modal('show');
    }

    delete(): void {
        this.categoriesService.delete(this.deletingCategory.id)
            .then(() => {
                this.deleteLocalCategory(this.deletingCategory);
                this.deleteModal.modal('hide');
            });
    }

    openDeleteDialog(category: ExtendedCategory): void {
        this.deletingCategory = category;
        this.deleteModal.find('.category-delete-name').text(category.name);
        this.deleteModal.modal('show');
    }

    openUnionDialog(unionModal: CategoryUnionComponent): void {
        unionModal.openDialog(this.categories, this);
    }

    unionReplace(oldCategoryIds: number[], newCategory: ExtendedCategory): void {
        this.categories = this.categories.filter(c => {
            return oldCategoryIds.indexOf(c.id) == -1;
        });
        this.insertLocalCategory(newCategory);
    }

    /*
     Util functions
     */
    private insertLocalCategory(category: ExtendedCategory): void {
        let pos = 0;
        for (; pos < this.categories.length; pos++) {
            if (this.categories[pos].name >= category.name) {
                break;
            }
        }
        this.categories.splice(pos, 0, category)
    }

    private deleteLocalCategory(category: ExtendedCategory) {
        this.categories = this.categories.filter(c => c !== category);
    }


}