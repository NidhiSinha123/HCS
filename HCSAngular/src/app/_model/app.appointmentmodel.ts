import { CenterModel } from "./app.centermodel";
import { TestModel } from "./app.testmodel";
import { UserModel } from "./app.usermodel";

export class AppointmentModel{
    appointmentId:any;
    center:CenterModel;
    test:TestModel;
    user:UserModel;
    appointmentStatus:any;
    dateTime:any;
}