import {Component, OnInit} from "@angular/core";
import {FundService} from "./service/fund.service";
import {UserFund} from "./model/user.fund";

@Component({
    moduleId: module.id,
    selector: 'home',
    templateUrl: './html/funds.component.html',
    styleUrls: ['./css/funds.component.css']
})
export class FundsComponent implements OnInit {
    userFunds: UserFund[]

    constructor(private fundService: FundService) {
    }

    ngOnInit(): void {
        this.fundService.get()
            .then(userFunds => this.userFunds = userFunds);
    }

    getFundClass(balance: number) {
        if (balance > 0) {
            return 'green';
        }
        if (balance < 0) {
            return 'red';
        }
        return 'gray';
    }
}
