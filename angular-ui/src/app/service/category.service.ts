import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";
import {ExtendedCategory} from "../model/extended.category";
import {AbstractService} from "./abstract.service";

@Injectable()
export class CategoriesService extends AbstractService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private categoriesUrl = '/services/api/category';  // URL to web api

    constructor(private http: Http) {
        super();
    }

    getCategories(): Promise<ExtendedCategory[]> {
        return this.http.get(this.categoriesUrl, {headers: this.headers})
            .toPromise()
            .then(response => {
                return response.json() as ExtendedCategory[];
            })
            .catch(this.handleError);
    }

    create(name: string): Promise<ExtendedCategory> {
        const url = `${this.categoriesUrl}`;
        return this.http.post(url, JSON.stringify({name: name, alternatives: []}), {headers: this.headers})
            .toPromise()
            .then(res => res.json())
            .catch(this.handleError);
    }

    edit(id: number, name: string): Promise<ExtendedCategory> {
        const url = `${this.categoriesUrl}/${id}/rename/${name}`;
        return this.http.post(url, {headers: this.headers})
            .toPromise()
            .then(res => res.json())
            .catch(this.handleError);
    }

    delete(id: number): Promise<void> {
        const url = `${this.categoriesUrl}/${id}`;
        return this.http.delete(url, {headers: this.headers})
            .toPromise()
            .then(() => null)
            .catch(this.handleError);
    }

    union(name: string, categoryIds: number[]): Promise<ExtendedCategory> {
        const url = `${this.categoriesUrl}/union`;
        return this.http.post(url, JSON.stringify({name: name, categoryIds: categoryIds}), {headers: this.headers})
            .toPromise()
            .then(res => res.json())
            .catch(this.handleError);
    }
}