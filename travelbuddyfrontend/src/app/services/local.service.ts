import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, Observable } from "rxjs";
import { Local } from "../components/models";

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
@Injectable({
    providedIn: 'root'
  })
export class LocalService {
    constructor(private http: HttpClient) { }

    insert(local: Local) {
        return firstValueFrom(
            this.http.post('api/addlocal', local)
        )
    }

    getUserLocal(username : String) :Promise <Local>{
      return firstValueFrom(
        this.http.get<Local>(`api/local/${username}`)
      )
    }

    update(local: Local) {
      return firstValueFrom(
          this.http.post('api/updatelocal', local)
      )
  }

  delete(username :string) {
    return firstValueFrom(
        this.http.delete(`api/deletelocal/${username}`)
    )
  }

  getAllLocals(): Promise<Local[]> {
    return firstValueFrom(
        this.http.get<Local[]>(`api/alllocals`)
    )
  }

  filterLocals(location :string, activity?:string) :Promise <Local[]>{
    const params = new HttpParams()
      .set('location', location)
      .set('activity', activity||"")
    return firstValueFrom(
        this.http.get<Local[]>("api/filterlocal",{ params })
    )
  }
}