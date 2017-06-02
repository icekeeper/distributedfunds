import {AfterViewChecked, Component, ElementRef, OnInit} from "@angular/core";
import {ExtendedCategory} from "./model/extended.category";
import {CategoriesService} from "./service/category.service";
import {CategoryEditorComponent} from "./category-editor.component";

@Component({
    moduleId: module.id,
    selector: 'category-union-modal',
    templateUrl: './html/category.union.modal.html',
})
export class CategoryUnionComponent implements OnInit, AfterViewChecked {
    categories: ExtendedCategory[];

    private modal: any;
    private categoryEditor: CategoryEditorComponent;
    private selectCreated: boolean;
    private selectNeedRender: boolean;

    constructor(private categoriesService: CategoriesService) {
    }

    ngOnInit(): void {
        this.modal = $('.category-union-modal');
    }

    ngAfterViewChecked(): void {
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
    }

    openDialog(categories: ExtendedCategory[],
               categoryEditor: CategoryEditorComponent): void {
        this.categories = categories;
        this.categoryEditor = categoryEditor;
        this.modal.modal('show');
        this.selectNeedRender = true;
    }

    union(name: string, categoryIdsSelect: ElementRef): void {
        let rawCategoryIds: string[] = $(categoryIdsSelect).val();
        if (rawCategoryIds == null || rawCategoryIds.length < 2) {
            return;
        }
        let categoryIds: number[] = [];
        rawCategoryIds.forEach(id => categoryIds.push(+id));
        this.categoriesService.union(name, categoryIds)
            .then(category => {
                this.categoryEditor.unionReplace(categoryIds, category);
                this.modal.modal('hide');
            });
    }

}