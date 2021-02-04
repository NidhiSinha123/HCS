import {Component, OnInit} from '@angular/core';
import { HcsService } from '../_service/app.hcsservice';
import { AppointmentModel } from '../_model/app.appointmentmodel';
import { saveAs } from 'file-saver';
import { Router } from '@angular/router';
import { SmartTable, of } from 'smart-table-ng';

@Component({
    selector: 'viewappointment',
    templateUrl: '../_html/app.viewappointment.html'
})

export class ViewAppointment implements OnInit{

    appointmentList:AppointmentModel[]=[];
    orderName:any;
    orderDate:any;
    searchCenter:any='';

    constructor(private service:HcsService, private router:Router){

    }

    
    ngOnInit(){
        if(!(sessionStorage.getItem('userRole') === "ROLE_Customer")){
          this.router.navigate(['/home']);
        }
        this.getList();
    }

    getList(){   
        this.service.getUserAppointments(sessionStorage.getItem('userId')).subscribe(
            (appointmentList:AppointmentModel[]) => 
            (this.appointmentList = appointmentList),
            error => alert(error.error)
            );
    }

    downloadExcel(){
      this.service.download(sessionStorage.getItem('userId')).subscribe(
        response => {
          var blob = new Blob([response], {type: 'application/xlsx'});
          
          var filename = 'appointments.xlsx';
          saveAs(blob,filename);        
        } ,
      error=>{
        alert("Some error");
      }
      );
    }

    
   

  sortName() {
    if (this.orderName != 1) {
      this.appointmentList.sort((left, right) => left.center.centerName.localeCompare(right.center.centerName));
      this.orderName = 1;
    }
    else if (this.orderName == 1){
      this.appointmentList.sort((right, left) => left.center.centerName.localeCompare(right.center.centerName));
      this.orderName = 0;
    }
  }

  sortDate() {
    if (this.orderDate != 1) {
      this.appointmentList.sort((left, right) => left.dateTime.localeCompare(right.dateTime));
      this.orderDate = 1;
    }
    else if (this.orderDate == 1){
      this.appointmentList.sort((right, left) => left.dateTime.localeCompare(right.dateTime));
      this.orderDate = 0;
    }
  }

  searchCenterFx(){
    
    if(!this.searchCenter){
      this.getList();
    }
    else{
      
    for(let i=this.appointmentList.length-1;i>=0 ;i--){
      if(this.searchCenter != this.appointmentList[i].center.centerName){
        this.appointmentList.splice(i,1);
      }
    }
  }
}


}