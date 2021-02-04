import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { CenterModel } from '../_model/app.centermodel';
import { AppointmentModel } from '../_model/app.appointmentmodel';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserModel } from '../_model/app.usermodel';

export class User{
    constructor(
      public status:string,
       ) {}   
  }

  export class JwtResponse{
    constructor(
      public jwttoken:string,
       ) {}
    
  }

@Injectable({
    providedIn: 'root'
})
export class HcsService {

    userObject:any;

    constructor(private myhttp: HttpClient) { }

    getCenters() {
        let headers = new HttpHeaders();
        headers = headers.set('Authorization', sessionStorage.getItem('token'));
        return this.myhttp.get("http://localhost:9123/getCenters",  { headers: headers });
    }

    getTests(centerId: any) {
        let params = new HttpParams();
        params = params.append('centerId', centerId);
        return this.myhttp.get('http://localhost:9123/getTests', { params: params });
    }

    addAppointment(appointment: any) {

        return this.myhttp.post('http://localhost:9123/addAppointment', appointment);
    }

    getUserAppointments(userId: any) {
        let params = new HttpParams();
        params = params.append('userId', userId);
        return this.myhttp.get('http://localhost:9123/viewAppointments', { params: params });
    }

    getAppointments(centerId: any) {
        let params = new HttpParams();
        params = params.append('centerId', centerId);
        return this.myhttp.get('http://localhost:9123/pendingAppointments', { params: params });
    }

    approveAppointment(appointmentId: any) {
        let params = new HttpParams();
        params = params.append('appointmentId', appointmentId);
        return this.myhttp.post("http://localhost:9123/approveAppointment?appointmentId" + appointmentId, params);
    }

    addCenter(data: any) {
        return this.myhttp.post("http://localhost:9123/addCenter",data);
    }

    addTest(centerId:any,data:any)
    {
        let params = new HttpParams();
        params = params.append('centerId', centerId);
        params = params.append('testName', data.testName);
        return this.myhttp.post("http://localhost:9123/addTest",params);
    }

    deleteCenter(centerId: any) {
        // let params: URLSearchParams = new URLSearchParams();
        // params.set('appid', StaticSettings.API_KEY);
        return this.myhttp.delete("http://localhost:9123/removeCenter?centerId="+centerId,{responseType: 'text'});
     }

     deleteTest(centerId:any,testId:any)
    {
        return this.myhttp.delete("http://localhost:9123/removeTest?centerId="+centerId+"&testId="+testId,{responseType: 'text'});
    }

    download(userId:any): Observable<Blob>{
        return this.myhttp.get("http://localhost:9123/download?userId="+userId, {'responseType':"blob"});
    }

    getUser(useremail:any){
        return this.myhttp.get("http://localhost:9123/finduser?userEmail="+useremail);
    }


    /*
    Author: Jayesh Gaur
    Description: Service method to call /authenticate. Checks login details and returns a token is successful.
    */
    authenticate(username:string, password:string) {
        console.log("Inside service authenticate.. email: "+username+" password: "+password);
        const reqbody={userEmail: username, password:password};
        console.log(JSON.stringify(reqbody))
        
        return this.myhttp.post<any>('http://localhost:9123/authenticate', {userEmail: username, password:password});
      }

    isUserLoggedIn() {
        let user = sessionStorage.getItem('username')
        console.log(!(user === null))
        return !(user === null)
    }

    logOut() {
        sessionStorage.removeItem('username');
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('userId');
        sessionStorage.removeItem('userRole');
        sessionStorage.removeItem('userName');
    }

    register(newUser: any){
        return this.myhttp.post('http://localhost:9123/register', newUser);
   }

//    reject(appointmentId:any){
//     return this.myhttp.delete('http://localhost:9123/rejectappointment?appointmentId='+appointmentId);
// }

reject(appointmentId: any) {
    let params = new HttpParams();
    params = params.append('appointmentId', appointmentId);
    return this.myhttp.post("http://localhost:9123/rejectappointment?appointmentId" + appointmentId, params,{responseType: 'text'});
}

    
}