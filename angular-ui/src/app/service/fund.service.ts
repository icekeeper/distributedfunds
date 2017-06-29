import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {AbstractService} from "./abstract.service";
import {UserFund} from "../model/user.fund";

@Injectable()
export class FundService extends AbstractService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private fundsUrl = '/api/front/user/funds';

    constructor(private http: Http) {
        super();
    }

    get(): Promise<UserFund[]> {
        return this.http.get(this.fundsUrl, {headers: this.headers})
            .toPromise()
            .then(response => {
                return response.json() as UserFund[];
            })
            .catch(this.handleError);
    }

}