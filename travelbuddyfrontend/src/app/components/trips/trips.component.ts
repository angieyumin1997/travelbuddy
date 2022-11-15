import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as moment from 'moment';
import { filter, switchMap, tap } from 'rxjs';
import { placesAutoCompleteService } from 'src/app/services/placesautocomplete.service';
import { TripService } from 'src/app/services/trip.service';
import { Trip } from '../models';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.css']
})
export class TripsComponent implements OnInit {
  minDate!: Date;
  trips!: Trip[]
  formTrip!: FormGroup  
  filteredSearch !: Array<String>

  constructor( private fbTrip: FormBuilder,private tripSvc:TripService, private placesSvc : placesAutoCompleteService) { }

  ngOnInit(): void {
    this.getAllTrips()
    this.minDate = new Date();
    this.formTrip = this.createTripForm()
    
    this.formTrip.controls['location'].valueChanges.pipe(
      tap(searchTerm => console.log(searchTerm)),
      filter((searchTerm:string) => searchTerm.length >= 3),
      tap(searchTerm => console.log(searchTerm)),
      switchMap((searchTerm:string) => this.placesSvc.placesAutoComplete(searchTerm))
  ).subscribe((response: Array<String>) => this.filteredSearch = response)
  
  }

  getAllTrips(){
    this.tripSvc.getAllTrips().then(result=>{
      this.trips = result
    })
  }

  private createTripForm(): FormGroup {
    return this.fbTrip.group({
      start: this.fbTrip.control<string>(''),
      end: this.fbTrip.control<string>(''),
      location: this.fbTrip.control<string>('',[ Validators.required]),
    })
  }

  processTripForm(){
    const trip: Trip = this.formTrip.value as Trip
    if(trip.start!='' && trip.end!=''){
      trip.start = moment(trip.start).local().format('MM/DD/YYYY')
      trip.end = moment(trip.end).local().format('MM/DD/YYYY')
    }
    this.tripSvc.filterTrips(trip.location, trip.start, trip.end) 
    .then(result => {
      this.trips = result
    })
    .catch(error => {
      console.info(error)
    });
  }
}
