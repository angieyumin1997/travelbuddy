import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, Observable } from "rxjs";
import { Trip } from "../components/models";

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
@Injectable({
    providedIn: 'root'
  })
export class TripService {
    constructor(private http: HttpClient) { }

    insert(trip: Trip) {
        return firstValueFrom(
            this.http.post('api/addtrip', trip)
        )
    }

    getUserTrips(username : String) :Promise <Trip[]>{
      return firstValueFrom(
        this.http.get<Trip[]>(`api/trip/${username}`)
      )
    }

    delete(tripId: number) {
      return firstValueFrom(
          this.http.delete(`api/deletetrip/${tripId}`)
      )
    }

    getAllTrips(): Promise<Trip[]> {
      return firstValueFrom(
          this.http.get<Trip[]>(`api/alltrips`)
      )
    }

    filterTrips(location :string, start:string, end:string) :Promise <Trip[]>{
      const params = new HttpParams()
        .set('location', location)
        .set('start', start)
        .set('end', end)

      return firstValueFrom(
          this.http.get<Trip[]>("api/filtertrips",{ params })
      )
    }

}