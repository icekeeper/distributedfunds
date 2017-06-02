import {AfterViewChecked, Component, OnInit} from "@angular/core";
import {Store} from "./model/store";
import {StoresService} from "./service/store.service";
import {ExtendedCategory} from "./model/extended.category";
import {CategoriesService} from "./service/category.service";

@Component({
    moduleId: module.id,
    selector: 'category-editor',
    templateUrl: './html/stores-editor.component.html',
    styleUrls: ['./css/store-editor.component.css']
})
export class StoresEditorComponent implements OnInit, AfterViewChecked {
    stores: Store[];
    storeShowEditForm: boolean[];
    categories: ExtendedCategory[];

    constructor(private storesService: StoresService,
                private categoriesService: CategoriesService) {
    }

    ngOnInit(): void {
        this.storesService.getStores()
            .then(stores => {
                this.stores = stores;
                console.log(stores);
                this.storeShowEditForm = [];
                for (let i = 0; i < this.stores.length; i++) {
                    this.storeShowEditForm.push(false);
                }
            });
        this.categoriesService.getCategories()
            .then(categories => this.categories = categories);
    }

    ngAfterViewChecked(): void {
        if (this.categories != null) {
            $('.selectpicker').selectpicker({
                style: 'btn-info',
                size: 4
            });
        }
    }

    changeCategory(store: Store): Promise<Store> {
        return this.storesService.changeCategory(store.id, store.category.id);
    }

}