import {Component, OnDestroy, OnInit} from "@angular/core";
import {Account} from "./model/account";
import {AccountService} from "./service/account.service";
import {ActivatedRoute} from "@angular/router";
import {PerMonthReportService} from "./service/per.month.report.service";
import {PerMonthReport} from "./model/per.month.report";

declare var c3: any;
declare var d3: any;

@Component({
    moduleId: module.id,
    selector: 'reports',
    templateUrl: './html/per-month-report.component.html',
    styleUrls: []
})
export class PerMonthReportComponent implements OnInit, OnDestroy {
    accounts: Account[] = [];
    report: PerMonthReport;

    private from: string;
    private to: string;
    private accountIds: string[];
    private chart: any;

    private sub: any;

    constructor(private reportService: PerMonthReportService,
                private accountService: AccountService,
                private route: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.sub = this.route.params.subscribe(params => {
            this.from = params['from'];
            this.to = params['to'];
        });
        this.sub = this.route.queryParams.subscribe(params => {
            this.accountIds = params['accountIds'].split(',');
            this.accountIds = this.accountIds.filter(id => id != null && id != '');
        });
        this.accountService.getAccounts()
            .then(accounts => this.accounts = accounts);
        this.reportService.getReport(this.from, this.to, this.accountIds)
            .then(report => {
                this.report = report;
                this.showReport();
            });
        let component = this;
        $('#chart-mode').bootstrapSwitch({
            onText: 'line',
            offText: 'stream',
            labelText: 'mode',

            onColor: 'primary',
            offColor: 'success',

            size: 'small',

            onSwitchChange: function () {
                component.showReport()
            }
        });
        $('#chart-type').bootstrapSwitch({
            onText: 'percentage',
            offText: 'absolute',
            labelText: 'type',

            onColor: 'primary',
            offColor: 'success',

            size: 'small',

            onSwitchChange: function () {
                component.showReport()
            }
        });
    }

    showReport(): void {
        if (this.chart != null) {
            this.chart.destroy();
        }
        this.chart = this.getChartGenerator()(this, this.getChartMode());
    }

    getChartMode(): string {
        return $('#chart-mode').bootstrapSwitch('state') ? 'line' : 'stream';
    }

    getChartGenerator(): (component: PerMonthReportComponent, mode: string) => any {
        return $('#chart-type').bootstrapSwitch('state')
            ? this.generatePercentageReport
            : this.generateAbsoluteReport;
    }

    generatePercentageReport(component: PerMonthReportComponent, mode: string): void {
        return c3.generate({
            bindto: '#chart',
            data: {
                xFormat: '%Y-%m-%d',
                json: component.report.percentageChart,
                keys: {
                    x: 'date',
                    value: component.report.percentageChartLabels
                },
                types: mode === 'stream' ? component.generateTypes() : undefined,
                groups: mode === 'stream' ? component.generateGroups() : undefined
            },
            size: {
                height: 750
            },
            axis: {
                x: {
                    type: 'timeseries',
                    tick: {
                        format: '%m.%Y'
                    }
                }
            },
            tooltip: {
                grouped: false,
                format: {
                    value: function (value: any, ratio: any, id: any) {
                        return value + "%";
                    }
                }
            }
        });
    }

    generateAbsoluteReport(component: PerMonthReportComponent, mode: string): void {
        return c3.generate({
            bindto: '#chart',
            data: {
                xFormat: '%Y-%m-%d',
                json: component.report.absoluteChart,
                keys: {
                    x: 'date',
                    value: component.report.absoluteChartLabels
                },
                types: mode === 'stream' ? component.generateTypes() : undefined,
                groups: mode === 'stream' ? component.generateGroups() : undefined
            },
            size: {
                height: 750
            },
            axis: {
                x: {
                    type: 'timeseries',
                    tick: {
                        format: '%m.%Y'
                    }
                }
            },
            tooltip: {
                grouped: false,
                format: {
                    value: function (value: any, ratio: any, id: any) {
                        return d3.format(",")(value);
                    }
                }
            }
        });
    }

    generateTypes(): any {
        let types = {};
        this.report.percentageChartLabels.forEach(function (l) {
            types[l] = 'area';
        });
        return types;
    }

    generateGroups(): any {
        let groups: any[] = [];
        this.report.percentageChartLabels.forEach(function (l) {
            groups.push(l);
        });
        return [groups];
    }

    ngOnDestroy(): void {
        this.sub.unsubscribe();
    }
}
