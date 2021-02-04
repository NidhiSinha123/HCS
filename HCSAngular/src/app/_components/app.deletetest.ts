import {Component, OnInit} from '@angular/core';
import { HcsService } from '../_service/app.hcsservice';
import { CenterModel } from '../_model/app.centermodel';
import { TestModel } from '../_model/app.testmodel';
import { Router } from '@angular/router';

@Component({
    selector: 'deletetest',
    templateUrl:'../_html/app.deletetest.html'
})

export class DeleteTestComponent implements OnInit{
    centerList:CenterModel[]=[];
    centerId:any;
    testList:TestModel[]=null;
    testId:any;
    orderName:any;
    searchCenter:any='';
    orderTestName:any;
    searchTest:any='';

    constructor(private service:HcsService, private router:Router){    
    }

    ngOnInit()  {
        if(!(sessionStorage.getItem('userRole') === "ROLE_Admin")){
            this.router.navigate(['forbidden']);
        }
        this.centerId=null;
        this.testList=null;
        this.testId=null;
        this.service.getCenters().subscribe((centerList:CenterModel[]) => this.centerList = centerList);
    }

    selectCenter(center:CenterModel){
        if(this.centerId == null)
        {
            this.centerId=center.centerId;
            this.centerList=[];
            this.centerList.push(center);
            this.service.getTests(center.centerId).subscribe((testList:TestModel[]) => this.testList = testList);
        }   
    }

    changeCenter(){
        this.ngOnInit();
    }

    delete(test:TestModel){
        this.testId=test.testId;
        this.testList = [];
        this.testList.push(test);
    }

    confirmDelete(){
        this.service.deleteTest(this.centerId,this.testId).subscribe(
        (data:string)=>{alert(data);
        this.service.getTests(this.centerId).subscribe((testList:TestModel[]) => this.testList = testList);
        this.testId=null;
        }
        ,error => alert("Please refresh the page.")
         
        );
        // this.testId=null;
        // this.testList = [];
        // this.service.getTests(this.centerId).subscribe((testList:TestModel[]) => this.testList = testList);
        
    }

    sortName() {
        if (this.orderName != 1) {
          this.centerList.sort((left, right) => left.centerName.localeCompare(right.centerName));
          this.orderName = 1;
        }
        else if (this.orderName == 1){
          this.centerList.sort((right, left) => left.centerName.localeCompare(right.centerName));
          this.orderName = 0;
        }
      }

      searchCenterFx(){
    
        if(!this.searchCenter){
            this.centerId=null;
            this.testList=null;
            this.testId=null;
            this.service.getCenters().subscribe((centerList:CenterModel[]) => this.centerList = centerList);
    
        }
        else{
          
        for(let i=this.centerList.length-1;i>=0 ;i--){
          if(this.searchCenter != this.centerList[i].centerName){
            this.centerList.splice(i,1);
          }
        }
      }
    }

    sortTestName() {
        if (this.orderTestName != 1) {
          this.testList.sort((left, right) => left.testName.localeCompare(right.testName));
          this.orderTestName = 1;
        }
        else if (this.orderTestName == 1){
          this.testList.sort((right, left) => left.testName.localeCompare(right.testName));
          this.orderTestName = 0;
        }
      }

      searchTestFx(){
    
        if(!this.searchTest){
            this.testId=null;
            this.service.getTests(this.centerId).subscribe((testList:TestModel[]) => this.testList = testList);
           
        }
        else{
          
        for(let i=this.testList.length-1;i>=0 ;i--){
          if(this.searchTest != this.testList[i].testName){
            this.testList.splice(i,1);
          }
        }
      }
    }
    

}