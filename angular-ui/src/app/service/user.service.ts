import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {AbstractService} from "./abstract.service";
import {User} from "../model/user";

@Injectable()
export class UserService extends AbstractService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private userUrl = '/api/front/login';

    constructor(private http: Http) {
        super();
    }

    getCurrent(): Promise<User> {
        return this.http.get(this.userUrl, {headers: this.headers})
            .toPromise()
            .then(response => {
                return response.json() as User;
            })
            .catch(this.handleError);
    }

}