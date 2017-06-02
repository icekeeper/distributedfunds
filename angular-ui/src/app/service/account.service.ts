import {Injectable} from "@angular/core";
import {Headers, Http, RequestOptions} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {Account} from "../model/account";
import {AbstractService} from "./abstract.service";

@Injectable()
export class AccountService extends AbstractService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private accountsUrl = '/services/api/account';
    private bankStatementsUrl = '/services/api/bank/statement';

    constructor(private http: Http) {
        super();
    }

    getAccounts(): Promise<Account[]> {
        return this.http.get(this.accountsUrl, {headers: this.headers})
            .toPromise()
            .then(response => {
                return response.json() as Account[];
            })
            .catch(this.handleError);
    }

    getAccount(id: number): Promise<Account> {
        const url = `${this.accountsUrl}/${id}`;
        return this.http.get(url, {headers: this.headers})
            .toPromise()
            .then(response => response.json().data as Account)
            .catch(this.handleError);
    }

    delete(id: number): Promise<void> {
        const url = `${this.accountsUrl}/${id}`;
        return this.http.delete(url, {headers: this.headers})
            .toPromise()
            .then(() => null)
            .catch(this.handleError);
    }

    create(name: string, parserType: string): Promise<Account> {
        return this.http
            .post(this.accountsUrl, JSON.stringify({name: name, parserType: parserType}), {headers: this.headers})
            .toPromise()
            .then(res => res.json())
            .catch(this.handleError);
    }

    uploadBankStatement(accountId: number,
                        event: any,
                        successCallback: (data: any) => void,
                        errorCallback: (data: any) => void): void {
        let fileList: FileList = event.target.files;
        if (fileList.length > 0) {
            let file: File = fileList[0];
            let formData: FormData = new FormData();
            formData.append('statement', file, file.name);
            let headers = new Headers();
            headers.append('Content-Type', 'multipart/form-data');
            headers.append('Accept', 'application/json');
            let options = new RequestOptions({headers: headers});
            this.http.post(`${this.bankStatementsUrl}/upload/${accountId}`, formData, options)
                .toPromise()
                .then(res => successCallback(res.json()))
                .catch(error => {
                    errorCallback(error);
                    return this.handleError(error);
                })
        }
    }

}