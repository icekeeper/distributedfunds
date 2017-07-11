import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {AppRoutingModule} from "./app-routing.module";
import {AppComponent} from "./app.component";
import {UserService} from "./core.service/user.core.service";
import {AlertComponent} from "./alert.component";
import {UserFundsComponent} from "./user.funds.component";
import {FundService} from "./core.service/fund.core.service";
import {UserComponent} from "./user.component";
import {FundsPageComponent} from "./funds.page.component";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        AppRoutingModule
    ],
    declarations: [
        AppComponent,

        FundsPageComponent,

        UserFundsComponent,
        UserComponent,

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
