import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {AppRoutingModule} from "./app-routing.module";
import {AppComponent} from "./app.component";
import {UserService} from "./service/user.service";
import {AlertComponent} from "./alert.component";
import {FundsComponent} from "./funds.component";
import {FundService} from "./service/fund.service";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        AppRoutingModule
    ],
    declarations: [
        AppComponent,

        FundsComponent,

        AlertComponent,
    ],
    providers: [
        UserService,
        FundService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
