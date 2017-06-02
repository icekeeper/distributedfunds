import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {AbstractService} from "./abstract.service";
import {PerMonthReport} from "../model/per.month.report";

@Injectable()
export class PerMonthReportService extends AbstractService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private reportUrl = '/services/api/reports/per-month';  // URL to web api

    constructor(private http: Http) {
        super();
    }

    getReport(from: string, to: string, accountIds: string[]): Promise<PerMonthReport> {
        return this.http
            .post(
                this.reportUrl,
                JSON.stringify({
                    from: from,
                    to: to,
                    accountIds: accountIds
                }),
                {headers: this.headers}
            )
            .toPromise()
            .then(response => {
                console.log(response.json());
                return response.json() as PerMonthReport;
            })
            .catch(this.handleError);
    }

}