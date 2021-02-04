import { Component, OnInit, ElementRef, ViewChild } from "@angular/core";
import { HcsService } from "../_service/app.hcsservice";
import { CenterModel } from "../_model/app.centermodel";
import { TestModel } from "../_model/app.testmodel";
import {FileUploader} from 'ng2-file-upload'
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Router } from "@angular/router";
import { text } from "@angular/core/src/render3/instructions";

@Component({
    selector:'addtest',
    templateUrl:'../_html/app.addtest.html'


})

export class AddTestComponent implements OnInit
{
    centerList:CenterModel[]=[];
    model:TestModel={testId:null,testName:null};
    errorMessage: any;
    myFiles:string [] = [];
    sMsg:string = '';
    center:any={};
    

    constructor(private service:HcsService, private myhttp:HttpClient, private router:Router){
        
    }
    ngOnInit(): void {
      if(!(sessionStorage.getItem('userRole') === "ROLE_Admin")){
        this.router.navigate(['forbidden']);
    }
        this.service.getCenters().subscribe((centerListS:CenterModel[]) => this.centerList = centerListS);
        console.log(this.centerList);
    }
    addTest(centerId:any):any
    {
         //console.log(this.model.testName)
        //alert("centerId"+centerId)
       // alert(centerId);
       if(centerId!=undefined && centerId!=null && this.model.testName!=null)
        this.service.addTest(centerId,this.model).subscribe((data:any)=>{alert("Test added successfully");
        location.reload();
        
    },error => this.errorMessage= error.error);
       
    }
    


    getFileDetails (e) {
      //console.log (e.target.files);

      for (var i = 0; i < e.target.files.length; i++) { 
        this.myFiles.push(e.target.files[i]);
      }
    }

  uploadFiles(centerId: any) {

  if ((this.center.centerId != undefined) && (this.myFiles.length > 0)) {
      const frmData = new FormData();

      for (var i = 0; i < this.myFiles.length; i++) {
        frmData.append("file", this.myFiles[i]);
      }

      this.myhttp.post('http://localhost:9123/uploadtest?centerId=' + this.center.centerId, frmData,{responseType:'text'}).subscribe(
        data => {
          // SHOW A MESSAGE RECEIVED FROM THE WEB API.
          this.sMsg = data as string;
          console.log(this.sMsg);
          alert("Added successfully!")
          location.reload();
        }
        ,
        (err: HttpErrorResponse) => {
          console.log(err.message);    // Show error, if any.
        }
      );
    }

    else {
      if(this.center.centerId == undefined){
        this.errorMessage = "select center";
      }
      else{
      this.errorMessage = "choose a file first to upload";
    }
  }
  
  }

   
}