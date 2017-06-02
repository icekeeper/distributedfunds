import {AfterViewChecked, Component, ElementRef, OnInit} from "@angular/core";
import {Account} from "./model/account";
import {AccountService} from "./service/account.service";
import {Router} from "@angular/router";
import moment = require("moment");


@Component({
    moduleId: module.id,
    selector: 'reports',
    templateUrl: './html/reports.component.html',
    styleUrls: []
})
export class ReportsComponent implements OnInit, AfterViewChecked {
    accounts: Account[];

    constructor(private accountService: AccountService,
                private router: Router) {
    }

    ngOnInit(): void {
        this.accountService.getAccounts()
            .then(accounts => this.accounts = accounts);
        $('#period-report-panel').find('.input-group.date').datepicker({
            format: "dd.mm.yyyy",
            todayHighlight: true
        });
        $('#per-month-report-panel').find('.input-group.date').datepicker({
            format: "mm.yyyy",
            startView: 1,
            minViewMode: 1,
            todayHighlight: true
        });
    }

    ngAfterViewChecked(): void {
        if (this.accounts != null) {
            $('.selectpicker').selectpicker({
                style: 'btn-info',
                size: 4
            });
        }
    }

    showPeriodReport(from: string, to: string, accountIdsSelect: ElementRef): void {
        let accountIds = $(accountIdsSelect).val();
        console.log('showing report for accounts: ' + accountIds);
        if (!from || !to) return;
        from = moment(from, 'DD.MM.YYYY').format('YYYY-MM-DD');
        to = moment(to, 'DD.MM.YYYY').format('YYYY-MM-DD');
        if (!accountIds) {
            accountIds = [];
        }
        this.router.navigate(['/reports/period', from, to], {queryParams: {accountIds: accountIds}})
            .catch(error => console.error('An error occurred', error));
    }

    showPerMonthReport(from: string, to: string, accountIdsSelect: ElementRef): void {
        let accountIds = $(accountIdsSelect).val();
        console.log('showing report for accounts: ' + accountIds);
        if (!from || !to) return;
        from = moment(from, 'MM.YYYY').format('YYYY-MM-DD');
        to = moment(to, 'MM.YYYY').format('YYYY-MM-DD');
        if (!accountIds) {
            accountIds = [];
        }
        this.router.navigate(['/reports/per-month', from, to], {queryParams: {accountIds: accountIds}})
            .catch(error => console.error('An error occurred', error));
    }
}
