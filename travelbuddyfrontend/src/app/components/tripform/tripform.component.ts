import { outputAst } from '@angular/compiler';
import { Component, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import * as moment from 'moment';
import { filter, map, Subject, switchMap, tap } from 'rxjs';
import { placesAutoCompleteService } from 'src/app/services/placesautocomplete.service';
import { TripService } from 'src/app/services/trip.service';
import { Trip } from '../models';

@Component({
  selector: 'app-tripform',
  templateUrl: './tripform.component.html',
  styleUrls: ['./tripform.component.css']
})
export class TripformComponent implements OnInit {
  typesOfShoes = ['sports','walking']

  @Output()
  onNewTrip = new Subject()

  @Output()
  cancelAddTrip = new Subject()

  minDate!: Date;
  tripType = ["Explore around cities", "Adventure Travel", "Road Trip"]
  successTripMsg !:String
  failTripMsg !: String  
  formTrip!: FormGroup  
  filteredSearch!: Array<String>
  chatListControl = new FormControl('');
  constructor(  private fbTrip: FormBuilder, private tripSvc : TripService, private placesSvc : placesAutoCompleteService ) { }

  ngOnInit(): void {
    this.minDate = new Date();
    this.formTrip = this.createTripForm()
    this.formTrip.controls['location'].valueChanges.pipe(
      tap(searchTerm => console.log(searchTerm)),
      filter((searchTerm:string) => searchTerm.length >= 3),
      tap(searchTerm => console.log(searchTerm)),
      switchMap((searchTerm:string) => this.placesSvc.placesAutoComplete(searchTerm))
  ).subscribe((response: Array<String>) => this.filteredSearch = response)
  }

  private createTripForm(): FormGroup {
    return this.fbTrip.group({
      start: this.fbTrip.control<string>('', [ Validators.required]),
      end: this.fbTrip.control<string>('',[ Validators.required]),
      location: this.fbTrip.control<string>('',[ Validators.required]),
      type: this.fbTrip.control<string>('',[ Validators.required]),
      description: this.fbTrip.control<string>('',[ Validators.required])
    })
  }

  processTripForm(){
    this.failTripMsg = ""
    const trip: Trip = this.formTrip.value as Trip
    trip.start = moment(trip.start).local().format('MM/DD/YYYY')
    trip.end = moment(trip.end).local().format('MM/DD/YYYY')
    console.info('>>> trip: ', trip)
    this.tripSvc.insert(trip) 
    .then(result => {
      this.successTripMsg = "Trip is added successfully"
      this.onNewTrip.next(null)

    })
    .catch(error => {
      this.failTripMsg = "Error adding trip"
    });
  }

  cancelTripForm(){
    this.cancelAddTrip.next(null)
  }


}
