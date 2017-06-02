"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require("@angular/core");
var AlertComponent = (function () {
    function AlertComponent() {
        this.show = false;
        this.timer = null;
    }
    AlertComponent.prototype.showAlert = function (type, text, time) {
        this.type = type;
        this.text = text;
        if (this.timer != null) {
            clearTimeout(this.timer);
        }
        var alert = this;
        this.timer = setTimeout(function () {
            alert.show = false;
        }, time);
        this.show = true;
    };
    return AlertComponent;
}());
AlertComponent.SUCCESS = 'success';
AlertComponent.INFO = 'info';
AlertComponent.WARNING = 'warning';
AlertComponent.DANGER = 'danger';
AlertComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'alert',
        template: '<div *ngIf="show" id="{{id}}" class="alert alert-{{type}}" role="alert">{{text}}</div>'
    })
], AlertComponent);
exports.AlertComponent = AlertComponent;
//# sourceMappingURL=alert.component.js.map