import {Component, OnInit} from '@angular/core';
import { HcsService } from '../_service/app.hcsservice';
import { CenterModel } from '../_model/app.centermodel';
import { AppointmentModel } from '../_model/app.appointmentmodel';
import { Router } from '@angular/router';

@Component({
    selector: 'approveappointment',
    templateUrl:'../_html/app.approveappointment.html'
})

export class ApproveAppointmentComponent implements OnInit{
    centerList:CenterModel[]=[];
    centerId:any;
    appointmentList:AppointmentModel[]=null;
    appointmentId:any;
    buttonStatus:string='0';
    orderName:any;
    searchCenter:any='';
    orderUser:any;
    searchUser:any='';

    constructor(private service:HcsService, private router:Router){    
    }

    ngOnInit()  {
        if(!(sessionStorage.getItem('userRole') === "ROLE_Admin")){
            this.router.navigate(['forbidden']);
        }
        this.centerId=null;
        this.appointmentList=null;
        this.appointmentId=null;
        this.service.getCenters().subscribe((centerList:CenterModel[]) => this.centerList = centerList);
    }

    selectCenter(center:CenterModel){
        if(this.centerId == null)
        {
            this.centerId=center.centerId;
            this.centerList=[];
            this.centerList.push(center);
            this.service.getAppointments(center.centerId).subscribe((appointmentList:AppointmentModel[]) => this.appointmentList = appointmentList);
        }   
    }

    changeCenter(){
        this.ngOnInit();
    }

    approve(appointment:AppointmentModel){
        this.appointmentId=appointment.appointmentId;
        this.appointmentList = [];
        this.appointmentList.push(appointment);
        this.buttonStatus='1';
    }

    confirmApprove(){
        this.service.approveAppointment(this.appointmentId).subscribe(
         (data:boolean) => {alert("Approved Successfully.");
         this.service.getAppointments(this.centerId).subscribe((appointmentList:AppointmentModel[]) => this.appointmentList = appointmentList);
        }
     //    ,error => alert(error.error)
         ,error => alert("Please refresh the page.")
        );
        this.appointmentId=null;
        this.buttonStatus='0';
        this.appointmentList = [];
        this.service.getAppointments(this.centerId).subscribe((appointmentList:AppointmentModel[]) => this.appointmentList = appointmentList);
    }

    reject(appointment:AppointmentModel){
        this.appointmentId=appointment.appointmentId;
        this.appointmentList = [];
        this.appointmentList.push(appointment);
        this.buttonStatus='2';
    }

    confirmReject(){
        this.service.reject(this.appointmentId).subscribe(
         (data:any) => {
         alert("Rejected successfully.");
         this.service.getAppointments(this.centerId).subscribe((appointmentList:AppointmentModel[]) => this.appointmentList = appointmentList);
        }
     //    ,error => alert(error.error)
         ,error => alert("Please refresh the page.")
        );
        this.appointmentId=null;
        this.buttonStatus='0';
        this.appointmentList = [];
        this.service.getAppointments(this.centerId).subscribe((appointmentList:AppointmentModel[]) => this.appointmentList = appointmentList);
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
            this.appointmentList=null;
            this.appointmentId=null;
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

}