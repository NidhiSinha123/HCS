import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent }  from './app.component';
import{FormsModule} from '@angular/forms';
import {GetCentersComponent} from './_components/app.getcenterscomponent';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {GetTestsComponent} from './_components/app.gettestscomponent';
import { AddAppointmentComponent } from './_components/app.addappointment';
import { Routes, RouterModule } from '@angular/router'
import {UserHomeComponent} from './_components/app.userhome';
import {ViewAppointment} from './_components/app.viewappoinment'
import { AdminHomeComponent } from './_components/app.adminhome';
import { ApproveAppointmentComponent } from './_components/app.approveappointment';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {NgxPaginationModule} from 'ngx-pagination'; 
import { AddCenterComponent } from './_components/app.addcenter';
import { AddTestComponent } from './_components/app.addtest';
import { DeleteCenterComponent } from './_components/app.deletecenter';
import {ConfirmationPopoverModule} from 'angular-confirmation-popover';
import { DeleteTestComponent } from './_components/app.deletetest';
import { Error404Component } from './_components/app.error404';
import { Error403Component } from './_components/app.error403';
import { RegistrationComponent } from './_components/app.registration';
import { LoginComponent } from './_components/app.login';
import { HomeComponent } from './_components/app.home';
import { LogoutComponent } from './_components/app.logout';
import { AboutComponent } from './_components/app.about';
import { BasicAuthHtppInterceptorService } from './_service/app.basicauthservice';
import { SmartTableModule, of} from 'smart-table-ng';

const myRoute: Routes = [
    { path: '', redirectTo:'home', pathMatch:'full'},
    { path: 'adminhome', component: AdminHomeComponent},
    { path: 'userhome', component: UserHomeComponent},
    { path: 'home', component: HomeComponent},
    { path: 'addappointment', component:AddAppointmentComponent},
    { path: 'viewappointment', component: ViewAppointment},
    { path: 'approveappointment', component:ApproveAppointmentComponent},
    { path: 'addcenter', component: AddCenterComponent },
    { path: 'addtest', component :AddTestComponent},
    { path: 'deletecenter', component: DeleteCenterComponent},
    { path: 'deletetest', component:DeleteTestComponent},
    { path: 'forbidden', component:Error403Component}, 
    { path: 'register', component:RegistrationComponent},
    { path: 'login' ,component:LoginComponent},
    { path: 'logout', component:LogoutComponent},
    { path: 'about', component:AboutComponent},
    { path: '**', component:Error404Component}
    
  
]

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        BrowserAnimationsModule,
        RouterModule.forRoot(myRoute),
        NgxPaginationModule,
        SmartTableModule ,
        ConfirmationPopoverModule.forRoot({confirmButtonType:'danger'})
        
    ],
    declarations: [
        AppComponent, GetCentersComponent,GetTestsComponent, 
        AddAppointmentComponent, ViewAppointment,
        ApproveAppointmentComponent,
        AdminHomeComponent, UserHomeComponent, HomeComponent,
        AddCenterComponent, AddTestComponent,DeleteCenterComponent,DeleteTestComponent,
        Error404Component, Error403Component,RegistrationComponent,
        LoginComponent,LogoutComponent, AboutComponent
   	],
    providers: [ {  
        provide:HTTP_INTERCEPTORS, useClass:BasicAuthHtppInterceptorService, multi:true 
      }],
    bootstrap: [AppComponent]
})

export class AppModule { }