import { Component, OnInit } from "@angular/core";
import { HcsService } from "../_service/app.hcsservice";
import { Router } from '@angular/router';
import { UserModel } from "../_model/app.usermodel";

@Component({
  selector: 'login',
  templateUrl: '../_html/app.login.html'
})

export class LoginComponent implements OnInit {
  model: any = [];
  useremail: any;
  password: any;
  invalidLogin = false;
  message:string;

  constructor(private router: Router,
    private hcsservice: HcsService) { }

  ngOnInit() {
  }

  checkLogin() {
    console.log("Inside login.ts checkLogin.. email: " + this.useremail + " password: " + this.password)
    this.hcsservice.authenticate(this.useremail, this.password).subscribe(
      userData => {
        sessionStorage.setItem('username', this.useremail);
        let tokenStr = 'Bearer ' + userData.token;
        sessionStorage.setItem('token', tokenStr);
        this.hcsservice.getUser(this.useremail).subscribe((data: UserModel) => {
          this.model = data;
          sessionStorage.setItem('userRole', data.userRole);
          sessionStorage.setItem('userId', data.userId);
          sessionStorage.setItem('userName', data.userName)
          this.checkRoles();
        });
      },
      error => {
        alert("Invalid Credentials  ")
      });
    }


  checkRoles(){

    if (sessionStorage.getItem('userRole') === "ROLE_Customer") {
      this.router.navigate(['/userhome']).then(()=>{window.location.reload();});
    } 
    else if(sessionStorage.getItem('userRole') === "ROLE_Admin"){
      this.router.navigate(['/adminhome']).then(()=>{window.location.reload();});
    }
    
  }
}