import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { Country, State, City } from 'country-state-city';
import * as moment from 'moment';
import { Router } from '@angular/router';
import { RegisterService } from 'src/app/services/register.service';
import { User } from '../models';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  femaleGender = 'F'
  maleGender = 'M'
  hide = true;
  countryList: Array<string> = new Array<string>;
  countryCode !: string
  stateList: Array<string> = new Array<string>;
  cityList: Array<string> = new Array<string>;
  userForm!: FormGroup
  fileName = ''
  file !: File
  successUserMsg !:String
  failUserMsg !: String
  maxDate!: Date;
  interestLabels = [ "hiking", "yoga", "cafe-hopping", "dancing", "running", "shopping", "swimming", "games", "cycling", "skiing", "book", "backpack", 
              "animals", "art","gardening", "cinema", "diving", "camping", "music", "photography", "writing", "sightseeing", "surfing", "culture"]
  interestsArray!: FormArray
  interests !: string

  constructor(private fbUser: FormBuilder, private regSvc : RegisterService, private router : Router) { }

  ngOnInit(): void {
    this.interestsArray = this.fbUser.array (this.interestLabels.map(()=> 
      this.fbUser.control<boolean>(false),
        [Validators.required]), 
          this.minSelectedCheckboxes(1))

    Country.getAllCountries().forEach(list => {
      this.countryList.push(list.name)
    })

    this.userForm = this.createUserForm()
    this.maxDate = new Date();
  }

  openInput() {
    document.getElementById("fileInput")!.click();
  }

  fileChange(input: any) {
    const file:File = input.target.files[0];
    this.file = file
    this.fileName = file.name;
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
  }

  onStateChange(newValue: any) {
    var index = this.stateList.indexOf(newValue)
    var stateDetails = State.getStatesOfCountry(this.countryCode).at(index)
    this.cityList = []
    City.getCitiesOfState(stateDetails!.countryCode, stateDetails!.isoCode).forEach(list => {
      this.cityList.push(list.name)
    })
  }

  hasUserFormError(ctrlName:string){
    return this.userForm.get(ctrlName)?.hasError('required')
  }

  private createUserForm(): FormGroup {
    return this.fbUser.group({
      name: this.fbUser.control<string>('', [ Validators.required]),
      username: this.fbUser.control<string>('', [ Validators.required, Validators.minLength(3)]),
      password: this.fbUser.control<string>('', [ Validators.required, Validators.minLength(6)]),
      dob: this.fbUser.control('',[ Validators.required]),
      gender: this.fbUser.control('',[ Validators.required]),
      image: this.fbUser.control('', [ Validators.required]),
      country: this.fbUser.control<string>('', [ Validators.required]),
      state: this.fbUser.control<string>(''),
      city: this.fbUser.control<string>(''),
      interests: this.interestsArray,
      introduction: this.fbUser.control<string>('',[ Validators.required])
    })
  }

  processUserForm(){
    this.interests=""
    const reg: User = this.userForm.value as User
    reg.dob = moment(reg.dob).local().format('MM/DD/YYYY')
    reg.image = this.file
    let index:number = 0
    this.userForm.value.interests.forEach((result: string)=> {
      if(result){       
        this.interests = this.interests + this.interestLabels[index] + ','
      }
      index++
    })
    this.interests = this.interests.slice(0, -1)
    reg.interests = this.interests
    this.regSvc.insert(reg) 
    .then(result => {
      this.failUserMsg = ""
      this.successUserMsg = "Account successfully created. Redirecting to login page ..."
      setTimeout( () => { this.router.navigate(['/login']) }, 3000 );
    })
    .catch(error => {
      this.failUserMsg = error.error.message
    });
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
}
