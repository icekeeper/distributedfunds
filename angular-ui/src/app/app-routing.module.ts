import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {UserFundsComponent} from "./user.funds.component";
import {FundsPageComponent} from "./funds.page.component";

const routes: Routes = [
    {path: '', redirectTo: '/funds', pathMatch: 'full'},
    {path: 'funds', component: FundsPageComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
