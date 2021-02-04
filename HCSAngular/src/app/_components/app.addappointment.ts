import {Component, OnInit} from '@angular/core';
import { CenterModel } from '../_model/app.centermodel';
import {HcsService} from '../_service/app.hcsservice';
import { TestModel } from '../_model/app.testmodel';
import {AppointmentModel} from '../_model/app.appointmentmodel';
import {Router} from "@angular/router"

@Component({
    selector: 'addappointment',
    templateUrl: '../_html/app.addappointment.html'
})
export class AddAppointmentComponent implements OnInit{
    // p: number = 1;
    centerList:CenterModel[]=[];
    testList:TestModel[]=[];
    appointment:any={ centerId:"", testId:"", dateAndTime:"", userId:""};
    centerId:any;
    testId:any;
    dateAndTime:string;
    errorMessage:any;
    orderName:any;
    orderTestName:any;
    searchCenter:any;
    searchTest:any;

    constructor(private service:HcsService, private router:Router){    
    }
    
    ngOnInit(){
        if(!(sessionStorage.getItem('userRole')==="ROLE_Customer")){
            this.router.navigate(['forbidden'])
        }
        this.centerId=null;
        this.testId=null;
        this.testList=null;
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

    selectTest(test:TestModel){
        this.testId=test.testId;
        this.testList=[];
        this.testList.push(test);
    }

    changeTest(){
        this.testId = null;
        this.service.getTests(this.centerId).subscribe((testList:TestModel[]) => this.testList = testList);
    }

    confirmAppointment(){
        this.appointment.centerId = this.centerId;
        this.appointment.testId = this.testId;
        this.appointment.dateAndTime = this.dateAndTime;
        this.appointment.userId = sessionStorage.getItem('userId');
        this.service.addAppointment(this.appointment).subscribe(
            (data:any)=>{alert("Appointment booked successfully");
            this.router.navigate(['/userhome'])},
            error => this.errorMessage= error.error
            );
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
        this.testId=null;
        this.testList=null;
        this.service.getCenters().subscribe((centerList:CenterModel[]) => this.centerList = centerList);
  
      }
      else{
    for(let i=this.centerList.length-1;i>=0 ;i--){
      if(this.searchCenter != this.centerList[i].centerName){
        this.centerList.splice(i,1);
      }
    }
  }}

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
        this.testList=null;
        this.service.getTests(this.centerId).subscribe((testList:TestModel[]) => this.testList = testList);
   
      }
      else{
    for(let i=this.testList.length-1;i>=0 ;i--){
      if(this.searchTest != this.testList[i].testName){
        this.testList.splice(i,1);
      }
    }
  }}

}