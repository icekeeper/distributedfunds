import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";
import {AbstractService} from "./abstract.service";
import {Store} from "../model/store";

@Injectable()
export class StoresService extends AbstractService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private storesUrl = '/services/api/store';  // URL to web api

    constructor(private http: Http) {
        super();
    }

    getStores(): Promise<Store[]> {
        return this.http.get(this.storesUrl, {headers: this.headers})
            .toPromise()
            .then(response => {
                return response.json() as Store[];
            })
            .catch(this.handleError);
    }

    changeCategory(storeId: number, categoryId: number): Promise<Store> {
        const url = `${this.storesUrl}/${storeId}/category/${categoryId}`;
        return this.http.post(url, {headers: this.headers})
            .toPromise()
            .then(res => {
                return res.json()
            })
            .catch(this.handleError);
    }
}