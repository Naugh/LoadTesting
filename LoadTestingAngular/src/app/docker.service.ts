import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';
import { environment } from '../environments/environment';
import { formatDate } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class DockerService {



  constructor(private http: Http) { }
    
  initSlaves(n:number){
    console.log("CALLING TO " + environment.API+"init");
    let h = new Headers({
      'Content-Type': 'application/json'
    }); 

    let options = new RequestOptions({
      headers: h
    });
    return this.http.post(environment.API+"init", n, options)
    .map(response => response.json())
    .catch(error => this.handleError(error));
  }

  uploadFile(file:File){

    let formdata: FormData = new FormData();
    formdata.append('file', file, file.name);


    /*let h = new Headers({
      'Content-Type': 'application/x-www-form-urlencoded'
    }); 
*/
    let h = new Headers({
     // 'Content-Type': 'multipart/form-data',
      'Accept': 'application/json'
    }); 

    let options = new RequestOptions({
      headers: h
    });

    return this.http.post(environment.API+"file", formdata, options)
    //.map(response => response.json())
    .catch(error => this.handleError(error));
  }
 /* addPerson(person: Person) {
  return this.http.post(URL, person)
    .map(response => response.json())
    .catch(error => this.handleError(error));
}
*/

  private handleError(error: any) {
  console.error(error);
  return Observable.throw("Server error (" + error.status + "): " + error.text())
  }
}
