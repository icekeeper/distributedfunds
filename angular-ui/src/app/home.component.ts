import {Component, ElementRef, OnInit} from "@angular/core";
import {Account} from "./model/account";
import {AccountService} from "./service/account.service";
import {AlertComponent} from "./alert.component";
import {TransactionsDeduplicateComponent} from "./transactions-deduplicate.component";
import {DuplicateTransactions} from "./model/duplicate.transactions";

@Component({
    moduleId: module.id,
    selector: 'home',
    templateUrl: './html/home.component.html',
    styleUrls: ['./css/home.component.css']
})
export class HomeComponent implements OnInit {
    accounts: Account[] = [];

    constructor(private accountService: AccountService) {
    }

    ngOnInit(): void {
        this.accountService.getAccounts()
            .then(accounts => this.accounts = accounts);
        $('.selectpicker').selectpicker({
            style: 'btn-info',
            size: 4
        });
    }

    create(accountName: string, parserType: string): void {
        this.accountService.create(accountName, parserType)
            .then(account => this.accounts.push(account));
    }

    delete(accountId: number, index: number): void {
        this.accountService.delete(accountId)
            .then(_ => this.accounts.splice(index, 1))
    }

    showStatementUploadDialog(fileInput: ElementRef): void {
        $(fileInput).click();
    }

    uploadBankStatement(fileInput: ElementRef, accountId: number, event: any, alert: AlertComponent, trDeduplicate: TransactionsDeduplicateComponent): void {
        this.accountService.uploadBankStatement(
            accountId,
            event,
            data => {
                if (data.result === 'SUCCESS') {
                    if (data.duplicates.length == 0) {
                        alert.showAlert(AlertComponent.SUCCESS, 'Bank statement uploaded successfully', 3000);
                    } else {
                        alert.showAlert(AlertComponent.INFO, 'Bank statement uploaded successfully, but duplicates found', 5000);
                        trDeduplicate.setTransactions(
                            data.duplicates as DuplicateTransactions[],
                            data.from,
                            data.to,
                            alert
                        );
                    }
                } else if (data.result === 'FAILED') {
                    alert.showAlert(AlertComponent.DANGER, 'Failed to upload bank statement file due to exception', 3000);
                    console.log(data);
                } else if (data.result === 'NO_FILE_CHOSEN') {
                    alert.showAlert(AlertComponent.WARNING, 'No file was selected', 3000);
                }
                $(fileInput).val('');
            },
            error => {
                alert.showAlert(AlertComponent.DANGER, 'Failed to upload bank statement file due to exception', 3000);
                console.log(error);
                $(fileInput).val('');
            }
        );
    }
}
