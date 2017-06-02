import {Component} from "@angular/core";
import {TransactionsService} from "./service/transactions.service";
import {AlertComponent} from "./alert.component";
import {DuplicateTransactions} from "./model/duplicate.transactions";
import {Transaction} from "./model/transaction";

@Component({
    moduleId: module.id,
    selector: 'transactions-deduplicate',
    templateUrl: './html/transactions-deduplicate.component.html',
})
export class TransactionsDeduplicateComponent {
    allTransactions: Transaction[] = null;
    duplicates: Transaction[] = null;

    private from: string = null;
    private to: string = null;
    private alert: AlertComponent;

    constructor(private transactionsService: TransactionsService) {
    }


    setTransactions(transactions: DuplicateTransactions[],
                    from: string,
                    to: string,
                    alert: AlertComponent): void {
        this.allTransactions = [];
        this.duplicates = [];
        transactions.forEach(tr => {
            this.allTransactions.push(tr.origin);
            this.allTransactions = this.allTransactions.concat(tr.duplicates);
            this.duplicates = this.duplicates.concat(tr.duplicates)

        });
        this.from = from;
        this.to = to;
        this.alert = alert;
    }

    deduplicateTransactions(): void {
        if (this.allTransactions != null) {
            let transactions = this.duplicates;
            this.transactionsService.deduplicateTransactions(
                transactions,
                data => this.alert.showAlert(AlertComponent.SUCCESS, 'Removed ' + transactions.length + ' duplicates', 3000),
                error => {
                    console.log(error);
                    this.alert.showAlert(AlertComponent.DANGER, 'Failed to remove duplicates', 3000);
                }
            );
            this.clear();
        }
    }

    clear(): void {
        this.allTransactions = null;
        this.duplicates = null;
        this.from = null;
        this.to = null;
    }

}