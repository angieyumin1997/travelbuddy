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
export class RegisterService {
  constructor(private http: HttpClient) { }

  @Output()
  onAuthenticateUser = new Subject<null>()

  @Output()
  onLogOut = new Subject<null>()

  insert(reg: User) {
    const form = new FormData
    form.set('name', reg.name)
    form.set('username', reg.username)
    form.set('password', reg.password)
    form.set('gender', reg.gender)
    form.set('dob', reg.dob)
    form.set('image', reg.image)
    form.set('introduction', reg.introduction)
    form.set('interests', reg.interests as string)

    form.set('country', reg.country)
    form.set('state', reg.state||"")
    form.set('city', reg.city||"")
    return firstValueFrom(
      this.http.post('api/auth/registration', form)
    )
  }

  signIn(username: string, password: string): Observable<any> {
    return this.http.post('/api/auth/signin', {
      username,
      password
    }, httpOptions)
  }

  authenticate(): Promise<any> {
    return firstValueFrom
      (this.http.get<any>('/api/auth/user')
    )
  }
}