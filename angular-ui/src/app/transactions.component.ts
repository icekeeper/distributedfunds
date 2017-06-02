import {AfterViewChecked, Component, ElementRef, OnInit} from "@angular/core";
import {Transaction} from "./model/transaction";
import {TransactionsService} from "./service/transactions.service";
import {AccountService} from "./service/account.service";
import {Account} from "./model/account";
import {AlertComponent} from "./alert.component";
import moment = require("moment");

@Component({
    moduleId: module.id,
    selector: 'transactions',
    templateUrl: './html/transactions.component.html',
    styleUrls: ['./css/store-editor.component.css']
})
export class TransactionsComponent implements OnInit, AfterViewChecked {
    transactions: Transaction[];
    accounts: Account[];

    constructor(private transactionsService: TransactionsService,
                private accountService: AccountService) {
    }

    ngOnInit(): void {
        this.accountService.getAccounts()
            .then(accounts => this.accounts = accounts);
        $('#transactions-panel').find('.input-group.date').datepicker({
            format: "dd.mm.yyyy",
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

    showTransactions(from: string, to: string, accountIdsSelect: ElementRef, alert: AlertComponent): void {
        let accountIds = $(accountIdsSelect).val()
        if (from != "") {
            from = moment(from, 'DD.MM.YYYY').format('YYYY-MM-DD');
        } else {
            from = null;
        }
        if (to != "") {
            to = moment(to, 'DD.MM.YYYY').format('YYYY-MM-DD');
        } else {
            to = null;
        }
        this.transactionsService.getTransactions(from, to, accountIds)
            .then(transactions => {
                this.transactions = transactions;
                if (transactions.length == 0) {
                    alert.showAlert(AlertComponent.WARNING, 'No transactions found for specified period', 3000);
                }
            });
    }

}