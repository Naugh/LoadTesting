import { Component, OnInit } from '@angular/core';
import { DockerService } from '../docker.service';

@Component({
  selector: 'app-docker-init',
  templateUrl: './docker-init.component.html',
  styleUrls: ['./docker-init.component.css']
})
export class DockerInitComponent implements OnInit {

  private slaves:number;

  constructor(private dockerService:DockerService) { }

  ngOnInit() {
  }

  initDocker(){
    this.dockerService.initSlaves(this.slaves).subscribe(
      error => console.error(error)
    )
  }
}
