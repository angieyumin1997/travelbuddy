import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { empty, finalize, Subscription } from 'rxjs';
import { ChatService } from 'src/app/services/chat.service';
import { LocalService } from 'src/app/services/local.service';
import { RegisterService } from 'src/app/services/register.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { TripService } from 'src/app/services/trip.service';
import { UserService } from 'src/app/services/user.service';
import { Local, Trip, User } from '../models';

@Component({
  selector: 'app-myuser',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  displayedInterests !: Array<any>;
  displayedActivities !: Array<any> | null
  authenticated = false;
  username !: string
  userDetails !: User | null
  addTrip = false
  addLocal = false
  editUser = false
  sub$ !: Subscription
  userTrips !: Trip[] |null
  userLocal !: Local | null
  message = true
  
  constructor(
    private tokenStorage: TokenStorageService,
    private regSvc: RegisterService,
    private http: HttpClient,
    private router: Router,
    private userSvc: UserService,
    private activatedRoute: ActivatedRoute,
    private tripSvc: TripService,
    private localSvc: LocalService,
    private chatSvc: ChatService) { }

  ngOnInit(): void {
    if (this.activatedRoute.snapshot.params['username']) {
      this.sub$ = this.activatedRoute.params.subscribe(v => {
        console.info(">>>>>>>>>>>>>>>subscribe to params",this.activatedRoute.snapshot.params['username'])
        this.authenticate()
        this.getUserDetails()
        this.getUserTrips(this.activatedRoute.snapshot.params['username'])
        this.getUserLocal(this.activatedRoute.snapshot.params['username'])
      })
    }
  }

  authenticate() {
    return this.regSvc.authenticate().then((result) => {
      if (result != null) {
        this.username = result['name']
        console.info(this.username)
        if (this.activatedRoute.snapshot.params['username'] == this.username) {
          this.authenticated = true
          this.message = false
        }else{
          this.authenticated = false
          this.message = true
        }
      }
    })
  }

  getUserDetails() {
    this.userSvc.getUser(this.activatedRoute.snapshot.params['username']).then(result => {
      console.info(result)
      this.userDetails = result
      let arr: Array<string> = this.userDetails.interests.split(",");
      this.displayedInterests = arr
    }).catch(e => {
      this.userDetails = null
    })
  }

  getUserTrips(username: String) {
    this.tripSvc.getUserTrips(username).then((result) => {
      this.userTrips = result
    })
    .catch(e => {
      this.userTrips = null
    })
  }

  getUserLocal(username: String) {
    this.localSvc.getUserLocal(username).then((result) => {
      this.userLocal = result
      let arr: Array<string> = this.userLocal.activities.split(",");
      this.displayedActivities = arr
    }).catch(e => {
      this.userLocal = null
      this.displayedActivities = null
      console.info(e)
    })
  }

  logout() {
    this.http.post('/logout', {}).pipe(
      finalize(() => {
        this.authenticated = false;
        this.tokenStorage.signOut()
        this.router.navigateByUrl('/');
        this.regSvc.onLogOut.next(null)
      })).subscribe();
  }

  updateUser() {
    this.cancelEditUser()
    this.editUser = false
    this.getUserDetails()
  }

  editUserForm() {
    this.editUser = true
  }

  cancelEditUser() {
    this.editUser = false
  }

  updateTrip() {
    this.addTrip = false
    this.getUserTrips(this.activatedRoute.snapshot.params['username'])
  }

  tripForm() {
    this.addTrip = true
  }

  cancelAddTrip() {
    this.addTrip = false
  }

  updateLocalOffering() {
    this.addLocal = false
    this.getUserLocal(this.activatedRoute.snapshot.params['username'])
  }

  editLocal() {
    this.addLocal = true
  }

  localForm() {
    this.addLocal = true
  }

  cancelAddLocal() {
    this.addLocal = false
  }

  deleteTrip(id: number) {
    this.tripSvc.delete(id).then(result => {
      console.info(result)
      this.getUserTrips(this.activatedRoute.snapshot.params['username'])
    }).catch(e => {
      console.info(e)
    })
  }

  deleteLocal() {
    this.localSvc.delete(this.activatedRoute.snapshot.params['username']).then(result => {
      this.getUserLocal(this.activatedRoute.snapshot.params['username'])
    }).catch(e => {
      console.info(e)
    })
  }

  createChat(recipient: string){
    this.chatSvc.createChat(this.username,recipient).then(result=>{
      if(result){
        this.router.navigate(['/chats'])
      }
    }).catch(e => {
      console.info(e)
    })
  }
}
