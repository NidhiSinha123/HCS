import {Component, OnInit} from '@angular/core';
import {TestModel} from '../_model/app.testmodel';
import {HcsService} from '../_service/app.hcsservice';
@Component({
    selector: 'gettests',
    templateUrl: '../_html/app.gettests.html'
})
export class GetTestsComponent implements OnInit{
    
    testList:TestModel[]=[];

    constructor(private service:HcsService){    
    }
    
    ngOnInit(){
     //   this.service.getTests().subscribe((testList:TestModel[]) => this.testList = testList);
    }

}