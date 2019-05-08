import { Component, OnInit } from '@angular/core';
import { DockerService } from '../docker.service';
import { JMeterResult } from "../j-meter-result.model";

@Component({
  selector: 'app-docker-init',
  templateUrl: './docker-init.component.html',
  styleUrls: ['./docker-init.component.css']
})
export class DockerInitComponent implements OnInit {


  constructor(private dockerService: DockerService) { }

  ngOnInit() {
    console.log(this.startControl());
  }

  public slaves: number | string = 1;
  private file: String;
  private testName: String;
  private test: JMeterResult;
  public spinner: boolean = false;
  private fileLoaded: boolean = false;
  public fileWrongExtension: boolean = false;

  //Chart variables
  public lineChartLabels: Array<any> = [];
  public lineChartOptions: any = {
    responsive: true
  };
  public lineChartData: Array<any> = [{
    data: [],
    label: ''
  }];
  public dataChart: Array<any>;
  public lineChartColors: Array<any> = [
    {
      backgroundColor: 'rgba(148,159,177,0.2)',
      borderColor: 'rgba(148,159,177,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    }
  ];
  public lineChartLegend: boolean = true;
  public lineChartType: string = 'line';


  initDocker() {

  }

  handleFileSelect(evt) {
    var files = evt.target.files;
    var file = files[0];
    console.log(file.name);

    this.fileWrongExtension = file.name.split('.').pop() != 'jmx'

    if (files && file && !this.fileWrongExtension) {
      this.dockerService.uploadFile(file).subscribe(
        error => console.error(error)
      )
      this.fileLoaded = true;
    }
  }

  startControl() {
    return this.spinner || !this.fileLoaded || this.slaves == '';
  }

  startTest() {
    this.spinner = true;
    this.dockerService.initSlaves(+this.slaves).subscribe(
      response => this.getResults(response)
    )
  }

  getResults(response) {
    if (response) {
      let jmeterResult: JMeterResult = {
        name: this.testName
      }
      this.dockerService.getResultFile().subscribe(
        response => this.test = response,
        error => console.log(error),
        () => this.resultComplete()
      )
    }
  }

  resultComplete() {
    console.log("RESULT");
    console.log(this.test);
    this.dataChart = [];
    this.lineChartLabels.length = 0;
    for (var i = 0; i < this.test.results.length; i++) {
      this.dataChart.push(this.test.results[i].latency);
      this.lineChartLabels.push(i);
    }
    this.lineChartData = [{
      data: this.dataChart,
      label: 'test'
    }]
    this.spinner = false;
  }

  public chartClicked(e: any): void {
    console.log(e);
  }

  public chartHovered(e: any): void {
    console.log(e);
  }
}
