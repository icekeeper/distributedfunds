import {Component} from "@angular/core";
import Timer = NodeJS.Timer;

@Component({
    moduleId: module.id,
    selector: 'alert',
    template: '<div *ngIf="show" id="{{id}}" class="alert alert-{{type}}" role="alert">{{text}}</div>'
})
export class AlertComponent {

    public static SUCCESS = 'success';
    public static INFO = 'info';
    public static WARNING = 'warning';
    public static DANGER = 'danger';

    type: string;
    text: string;
    show: boolean = false;

    private id: string;
    private timer: Timer = null;


    showAlert(type: string, text: string, time: number): void {
        this.type = type;
        this.text = text;

        if (this.timer != null) {
            clearTimeout(this.timer);
        }

        let alert = this;
        this.timer = setTimeout(function () {
            alert.show = false;
        }, time);
        this.show = true;
    }

}