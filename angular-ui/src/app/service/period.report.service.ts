import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {PeriodReport} from "../model/period.report";
import {AbstractService} from "./abstract.service";

@Injectable()
export class PeriodReportService extends AbstractService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private reportUrl = '/services/api/reports/period';  // URL to web api

    constructor(private http: Http) {
        super();
    }

    getReport(from: string, to: string, accountIds: string[]): Promise<PeriodReport> {
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
                return response.json() as PeriodReport;
            })
            .catch(this.handleError);
    }

}