import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HcsService } from '../_service/app.hcsservice';

@Component({
  selector: 'logout',
  templateUrl: '../_html/app.logout.html',
})
export class LogoutComponent implements OnInit {

  constructor(
    private service:HcsService,
    private router: Router) {

  }

  ngOnInit() {
    this.service.logOut();
    this.router.navigate(['login']);
  }

}