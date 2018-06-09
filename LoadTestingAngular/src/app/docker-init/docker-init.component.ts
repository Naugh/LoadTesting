import { Component, OnInit } from '@angular/core';
import { DockerService } from '../docker.service';
import {JMeterResult} from "../j-meter-result.model";

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

  private slaves: number|string = 1;
  private file: String;
  private testName: String;
  private test: JMeterResult;
  private spinner:boolean = false;
  private fileLoaded:boolean = false;
  private fileWrongExtension:boolean = false;

  //Chart variables
  private lineChartLabels:Array<any> = [];
  private lineChartOptions:any = {
    responsive: true
  };
  private lineChartData:Array<any>=[{
    data: [],
    label: ''
  }];
  private dataChart:Array<any>;
  public lineChartColors:Array<any> = [
    {
      backgroundColor: 'rgba(148,159,177,0.2)',
      borderColor: 'rgba(148,159,177,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    }
  ];
  public lineChartLegend:boolean = true;
  public lineChartType:string = 'line';


  initDocker() {
    this.dockerService.initSlaves(+this.slaves).subscribe(
      error => console.error(error)
    )
  }

  handleFileSelect(evt) {
    var files = evt.target.files;
    var file = files[0];
    console.log(file.name);
    //vigilar extension que sea de jmeter!!
    this.fileWrongExtension = file.name.split('.').pop() != 'jmx'

    if (files && file && !this.fileWrongExtension) {
      this.dockerService.uploadFile(file).subscribe(
        error => console.error(error)
      )
      this.fileLoaded = true;
    }
  }

  startControl(){
    return this.spinner || !this.fileLoaded || this.slaves == '';
  }

  startTest() {
    this.spinner=true;
    let  jmeterResult: JMeterResult = {
      name:  this.testName
    }
    this.dockerService.getResultFile().subscribe(
      response => this.test = response,
      error => console.log(error),
      () => this.resultComplete()
    )

  }

  resultComplete(){
    console.log("RESULT");
    console.log(this.test);
    this.dataChart = [];
    this.lineChartLabels.length = 0;
    for(var i = 0; i < this.test.results.length; i++){
      this.dataChart.push(this.test.results[i].latency);
      this.lineChartLabels.push(i);
    }
    this.lineChartData = [{
      data: this.dataChart,
      label: 'test'
    }]
    this.spinner=false;
  }

  public chartClicked(e:any):void {
    console.log(e);
  }
 
  public chartHovered(e:any):void {
    console.log(e);
  }
}
