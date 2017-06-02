import {Injectable} from "@angular/core";
import {AbstractService} from "./abstract.service";
import {Headers, Http} from "@angular/http";
import {Transaction} from "../model/transaction";

@Injectable()
export class TransactionsService extends AbstractService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private transactionsUrl = '/services/api/transactions';

    constructor(private http: Http) {
        super();
    }

    getTransactions(from: string, to: string, accountIds: number[]): Promise<Transaction[]> {
        let url = `${this.transactionsUrl}?`;
        if (from != null) {
            url += `from=${from}&`;
        }
        if (to != null) {
            url += `to=${to}&`;
        }
        if (accountIds != null && accountIds.length > 0) {
            for (let accountId of accountIds) {
                url += `account_ids=${accountId}&`;
            }
        }
        return this.http.get(url, {headers: this.headers})
            .toPromise()
            .then(res => res.json())
            .catch(this.handleError);
    }

    deduplicateTransactions(transactions: Transaction[],
                            successCallback: (data: any) => void,
                            errorCallback: (data: any) => void): void {
        let transactionIds: number[] = [];
        transactions.forEach(tr => transactionIds.push(tr.id));
        this.http.post(`${this.transactionsUrl}/remove-batch`, transactionIds, {headers: this.headers})
            .toPromise()
            .then(res => successCallback(res.json()))
            .catch(error => {
                errorCallback(error);
                return this.handleError(error);
            })
    }

}