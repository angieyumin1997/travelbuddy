import { outputAst } from '@angular/compiler';
import { Component, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { filter, Subject, switchMap, tap } from 'rxjs';
import { LocalService } from 'src/app/services/local.service';
import { placesAutoCompleteService } from 'src/app/services/placesautocomplete.service';
import { Local } from '../models';

@Component({
  selector: 'app-localform',
  templateUrl: './localform.component.html',
  styleUrls: ['./localform.component.css']
})
export class LocalformComponent implements OnInit {

  @Output()
  onNewLocal = new Subject()

  @Output()
  cancelAddLocal = new Subject()

  @Input()
  userLocal !: Local

  @Input()
  displayedActivities !: Array<any>;

  successLocalMsg !: String
  failLocalMsg !: String
  formLocal!: FormGroup
  isLoggedIn = false;
  localActivitiesLabels = ["coffee/drinks", "show around", "advice", "share my home"]
  activitiesArray!: FormArray
  activities !: string
  filteredSearch !:Array<String>

  constructor(private fbLocal: FormBuilder, private localSvc: LocalService, private placesSvc: placesAutoCompleteService) { }

  ngOnInit(): void {
    this.formLocal = this.createLocalForm(this.localActivitiesLabels, this.displayedActivities, this.userLocal)
    this.formLocal.controls['location'].valueChanges.pipe(
      tap(searchTerm => console.log(searchTerm)),
      filter((searchTerm:string) => searchTerm.length >= 3),
      tap(searchTerm => console.log(searchTerm)),
      switchMap((searchTerm:string) => this.placesSvc.placesAutoComplete(searchTerm))
  ).subscribe((response: Array<String>) => this.filteredSearch = response)
  }

  private createLocalForm(localActivitiesLabels: Array<any>, displayedActivities: Array<any>, userLocal?: Local): FormGroup {
    if(displayedActivities){
    this.activitiesArray = this.fbLocal.array(
      this.localActivitiesLabels.map((nil, i) =>
        this.fbLocal.control<boolean>(displayedActivities.indexOf(localActivitiesLabels[i]) != -1),
        [Validators.required]),
      this.minSelectedCheckboxes(1))
    }else{
      this.activitiesArray = this.fbLocal.array(
        this.localActivitiesLabels.map((nil, i) =>
          this.fbLocal.control<boolean>(false),
          [Validators.required]),
        this.minSelectedCheckboxes(1))
    }
    return this.fbLocal.group({
      location: this.fbLocal.control<string>(userLocal?.location || '', [Validators.required]),
      description: this.fbLocal.control<string>(userLocal?.description || '', [Validators.required]),
      activities: this.activitiesArray
    })
  }

  processLocalForm() {
    this.activities = ""
    const local: Local = this.formLocal.value as Local
    let index: number = 0
    this.formLocal.value.activities.forEach((result: any) => {
      if (result) {

        this.activities = this.activities + this.localActivitiesLabels[index] + ','
      }
      index++
    })
    this.activities = this.activities.slice(0, -1)
    local.activities = this.activities
    if (this.userLocal) {
      this.localSvc.update(local)
        .then(result => {
          this.successLocalMsg = "Successfully updated local"
          this.onNewLocal.next(null)
        })
        .catch(error => {
          this.failLocalMsg = "Error updating local"
        });
    } else {
      this.localSvc.insert(local)
        .then(result => {
          this.successLocalMsg = "Successfully added local"
          this.onNewLocal.next(null)
        })
        .catch(error => {
          this.failLocalMsg = "Error adding local"
        });
    }
  }

  minSelectedCheckboxes(min = 1) {
    const myValidator: ValidatorFn = (control: AbstractControl) => {
      const formArray = control as FormArray;
      const totalSelected = formArray.controls
        .map((control) => control.value)
        .reduce((prev, next) => (next ? prev + next : prev), 0);
      return totalSelected >= min ? null : { required: true };
    };
    return myValidator;
  }

  hasLocalFormError(ctrlName: string) {
    return this.formLocal.get(ctrlName)?.hasError('required')
  }

  cancelLocalForm() {
    this.cancelAddLocal.next(null)
  }

}
