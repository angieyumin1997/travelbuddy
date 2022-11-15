import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { filter, switchMap, tap } from 'rxjs';
import { LocalService } from 'src/app/services/local.service';
import { placesAutoCompleteService } from 'src/app/services/placesautocomplete.service';
import { Local } from '../models';

@Component({
  selector: 'app-locals',
  templateUrl: './locals.component.html',
  styleUrls: ['./locals.component.css']
})
export class LocalsComponent implements OnInit {
  form!: FormGroup
  locals !: Local[]
  filteredSearch !:Array<String>
  localActivitiesLabels = ["coffee/drinks", "show around", "advice", "share my home"]
  constructor(private localSvc : LocalService, private fb: FormBuilder, private placesSvc: placesAutoCompleteService) { }

  ngOnInit(): void {
    this.localSvc.getAllLocals().then(result=>{
      this.locals = result
    }).catch(error => {
      console.error('>>>>>>>:',error)
    });
    this.form = this.createUserForm()
    this.form.controls['location'].valueChanges.pipe(
      tap(searchTerm => console.log(searchTerm)),
      filter((searchTerm:string) => searchTerm.length >= 3),
      tap(searchTerm => console.log(searchTerm)),
      switchMap((searchTerm:string) => this.placesSvc.placesAutoComplete(searchTerm))
  ).subscribe((response: Array<String>) => this.filteredSearch = response)
  }

  private createUserForm(): FormGroup {
    return this.fb.group({
      location: this.fb.control<string>('', [Validators.required]),
      activities: this.fb.control<string>(''),
    })
  }

  processUserForm(){
    const local: Local = this.form.value as Local
    this.localSvc.filterLocals(local.location,local.activities).then(result=>{
      this.locals = result
    }).catch(error => {
      console.error('>>>>>>>:',error)
    });
  }

}
