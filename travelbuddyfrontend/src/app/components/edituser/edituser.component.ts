import { Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { City, Country, State } from 'country-state-city';
import * as moment from 'moment';
import { pairwise, Subject, Subscription } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { User } from '../models';

@Component({
  selector: 'app-edituser',
  templateUrl: './edituser.component.html',
  styleUrls: ['./edituser.component.css']
})

export class EdituserComponent implements OnInit {

  @Input()
  userDetails !:User

  @Input()
  username !:string

  @Output()
  onEditUser = new Subject()

  @Output()
  cancelEditUser = new Subject()
  
  femaleGender = 'f'
  maleGender = 'm'
  hide = true;
  countryList: Array<string> = new Array<string>;
  countryCode !: string
  stateList: Array<string> = new Array<string>;
  cityList: Array<string> = new Array<string>;
  userForm!: FormGroup
  successUserMsg !:String
  failUserMsg !: String
  maxDate!: Date;
  interestLabels = [ "hiking", "yoga", "cafe-hopping", "dancing", "running", "shopping", "swimming", "games", "cycling", "skiing", "book", "backpack", 
              "animals", "art","gardening", "cinema", "diving", "camping", "music", "photography", "writing", "sightseeing", "surfing", "culture"]
  selectedInterestLabels : Array<string> = []
  interestsArray!: FormArray
  interests !: string
  constructor(private fbUser: FormBuilder, private userSvc: UserService) { }

  ngOnInit(): void {
    this.userSvc.getUser(this.username).then(result=>{
      this.userDetails = result
      Country.getAllCountries().forEach(list => {
        this.countryList.push(list.name)
      })
      let arr: Array<string> = this.userDetails.interests.split(",");
      this.selectedInterestLabels = arr
      this.interestsArray = this.fbUser.array (this.interestLabels.map((nil,i)=> 
          this.fbUser.control<boolean>(this.selectedInterestLabels.indexOf(this.interestLabels[i])!=-1),
            [Validators.required]), 
              this.minSelectedCheckboxes(1))

      this.userForm = this.createUserForm()
      this.maxDate = new Date();
      this.onCountryChange(this.userDetails.country)
      this.onStateChange(this.userDetails.state)
    }).catch(e=>{
      console.info(e)
    })
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

  hasUserFormError(ctrlName:string){
    return this.userForm.get(ctrlName)?.hasError('required')
  }

  private createUserForm(): FormGroup {
    return this.fbUser.group({
      name: this.fbUser.control<string>(this.userDetails.name, [ Validators.required]),
      dob: this.fbUser.control(this.userDetails.dob,[ Validators.required]),
      gender: this.fbUser.control(this.userDetails.gender,[ Validators.required]),
      country: this.fbUser.control<string>(this.userDetails.country||'', [ Validators.required]),
      state: this.fbUser.control<string>(this.userDetails.state||''),
      city: this.fbUser.control<string>(this.userDetails.city||''),
      interests: this.interestsArray,
      introduction: this.fbUser.control<string>(this.userDetails.introduction,[ Validators.required])
    })
  }

  processUserForm(){
    this.interests=""
    const userDetails: User = this.userForm.value as User 
    console.info(userDetails)
    userDetails.dob = moment(userDetails.dob).local().format('MM/DD/YYYY')
    let index:number = 0
    this.userForm.value.interests.forEach((result: any)=> {
      if(result){
        this.interests = this.interests + this.interestLabels[index] + ','
      }
      index++
    })
    this.interests = this.interests.slice(0, -1)
    userDetails.interests = this.interests
    this.userSvc.updateUser(userDetails) 
    .then(result => {
      this.onEditUser.next(null)
    })
    .catch(error => {
      this.failUserMsg = error.error.message
    });
  }

  onCountryChange(newValue: any) {
    var index = this.countryList.indexOf(newValue)
    var countryDetails = Country.getAllCountries().at(index)
    this.stateList = []
    this.cityList = []
    this.countryCode = countryDetails!.isoCode
    State.getStatesOfCountry(this.countryCode).forEach(list => {
      this.stateList.push(list.name)
    })
    this.userForm.value.state = ""
    this.userForm.value.city = ""
  }

  onStateChange(newValue: any) {
    var index = this.stateList.indexOf(newValue)
    var stateDetails = State.getStatesOfCountry(this.countryCode).at(index)
    this.cityList = []
    City.getCitiesOfState(stateDetails!.countryCode, stateDetails!.isoCode).forEach(list => {
      this.cityList.push(list.name)
    })
    this.userForm.value.city = ""
  }

  cancel(){
    this.cancelEditUser.next(null)
  }

}
