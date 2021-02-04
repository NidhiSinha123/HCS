import {Component, OnInit} from '@angular/core';
import { CenterModel } from '../_model/app.centermodel';
import {HcsService} from '../_service/app.hcsservice';
@Component({
    selector: 'getcenters',
    templateUrl: '../_html/app.getcenters.html'
})
export class GetCentersComponent implements OnInit{
    
    centerList:CenterModel[]=[];

    constructor(private service:HcsService){
        
    }
    
    ngOnInit(){
        this.service.getCenters().subscribe((centerList:CenterModel[]) => this.centerList = centerList);
    }

}