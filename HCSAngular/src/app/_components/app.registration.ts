import { Component, OnInit } from "@angular/core";
import { HcsService } from "../_service/app.hcsservice";
import { Router } from "@angular/router";
import { UserModel } from "../_model/app.usermodel";

@Component({
    selector:'register',
    templateUrl:'../_html/app.registration.html'


})
export class RegistrationComponent implements OnInit
{
    model:any={};
    newUser:any={userName:"", userPassword:"", contactNo:"", userEmail:"", age:"", gender:""};


    constructor(private service:HcsService, private router:Router){ 
    }

    ngOnInit(){
        if(sessionStorage.getItem('token')){
            if(sessionStorage.getItem('userRole') === "ROLE_Customer"){
                this.router.navigate(['/userhome'])
            }
            else if(sessionStorage.getItem('userRole') === "ROLE_Admin"){
                this.router.navigate(['/adminhome'])
            }
        }
    }

    register(){
        this.newUser.userEmail = this.model.userEmail;
        this.newUser.userName = this.model.userName;
        this.newUser.userPassword = this.model.userPassword;
        this.newUser.contactNo = this.model.contactNo;
        this.newUser.age = this.model.age;
        this.newUser.gender = this.model.gender;
        this.service.register(this.newUser). subscribe (
           (data: UserModel)=>{alert("Registration successful. Please login, "+data.userName)
           this.router.navigate(['/login']);
           }, 
           error => alert(error.error)
           );
    }
   
    
}