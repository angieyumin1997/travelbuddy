import { HttpClient, HttpParams } from "@angular/common/http"
import { Injectable } from "@angular/core"
import { firstValueFrom } from "rxjs"

@Injectable({
    providedIn: 'root'
})
export class placesAutoCompleteService {

    constructor(private http: HttpClient) { }

    placesAutoComplete(input: string): Promise<Array<String>> {
        const params = new HttpParams()
            .set('input', input)
        return firstValueFrom(
            this.http.get<Array<String>>("/api/googleplacesautocomplete", { params })
        )
    }
}