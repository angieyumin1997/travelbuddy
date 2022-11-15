import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, Output } from "@angular/core";
import { firstValueFrom, Observable, Subject } from "rxjs";
import { User } from "../components/models";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUser(username : string): Promise<User> {
    return firstValueFrom(
      this.http.get<User>(`/api/user/${username}`)
    )
  }

  updateUser(userDetails: User) {
    return firstValueFrom(
      this.http.post('api/edituser', userDetails
      )
    )
  }
}