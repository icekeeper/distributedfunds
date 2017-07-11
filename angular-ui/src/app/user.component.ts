import {Component, OnInit} from "@angular/core";
import {UserService} from "./core.service/user.core.service";
import {User} from "./core.model/user";

@Component({
    moduleId: module.id,
    selector: 'user',
    templateUrl: './html/user.component.html',
    styleUrls: ['./css/user.component.css']
})
export class UserComponent implements OnInit {
    user: User;

    constructor(private userService: UserService) {
    }

    ngOnInit(): void {
        this.userService.getCurrent()
            .then(user => this.user = user);
    }
}