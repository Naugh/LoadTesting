import { Component, OnInit } from '@angular/core';
import { DockerService } from '../docker.service';

@Component({
  selector: 'app-docker-init',
  templateUrl: './docker-init.component.html',
  styleUrls: ['./docker-init.component.css']
})
export class DockerInitComponent implements OnInit {

  private slaves: number;
  private file: String;

  constructor(private dockerService:DockerService) { }

  ngOnInit() {
  }

  initDocker(){
    this.dockerService.initSlaves(this.slaves).subscribe(
      error => console.error(error)
    )
  }

  handleFileSelect(evt){
    var files = evt.target.files;
    var file = files[0];
    console.log(file.name);

 

    //vigilar extension que sea de jmeter!!
  
   if (files && file) {
      var reader = new FileReader();
      //reader.onload =this._handleReaderLoaded.bind(this);
      //reader.readAsBinaryString(file);

      this.dockerService.uploadFile(file).subscribe(
        error => console.error(error)
      )
  }
}

_handleReaderLoaded(readerEvt) {
   this.file = readerEvt.target.result;
   //console.log(this.file.name);
 }

/*
  _handleReaderLoaded(readerEvt) {
   var binaryString = readerEvt.target.result;
          this.imgBase64= btoa(binaryString);
  }
*/

/*
  nFileChange(event) {
    let reader = new FileReader();
    if(event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.form.get('avatar').setValue({
          filename: file.name,
          filetype: file.type,
          value: reader.result.split(',')[1]
        })
      };
    }
  }*/



}
