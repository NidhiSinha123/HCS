import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';


@Component({
    selector: 'adminhome',
    templateUrl: '../_html/app.adminhome.html'
})
export class AdminHomeComponent implements OnInit{
    ngOnInit(){
       if(!(sessionStorage.getItem('userRole') === "ROLE_Admin")){
           this.router.navigate(['forbidden']);
       }
    }

    constructor(private router:Router){

    }

}