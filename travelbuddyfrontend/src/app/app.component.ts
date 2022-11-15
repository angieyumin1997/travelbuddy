import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { RegisterService } from './services/register.service';
import { AngularFireMessaging } from '@angular/fire/compat/messaging';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ChatService } from './services/chat.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  username !: string
  sub$!: Subscription
  authenticated = false
  messages !: string
  activeContact !: string

  title = 'travelbuddy';
  constructor(private chatSvc : ChatService, private msg: AngularFireMessaging, public route: ActivatedRoute, private regSvc: RegisterService, private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.authenticate()
    this.regSvc.onAuthenticateUser.subscribe(() => {
      this.authenticate()
    })

    this.regSvc.onLogOut.subscribe(() => {
      this.authenticate()
    })

    this.sub$ =  this.chatSvc.onActiveContactChange.subscribe(result=>{
      this.activeContact = result
    })

    this.msg.onMessage((payload) => {
      // Get the data about the notification
      console.info("payload",payload)
      if(payload.notification.title != this.activeContact){
        let notification = payload.notification;
        this.messages = `${notification.title}: ${ notification.body} `
        this._snackBar.open(this.messages, "close");
        setTimeout( () => { this._snackBar.dismiss(); }, 3000 );
      }
     });
  }

  authenticate() {
    this.regSvc.authenticate().then((result) => {
      if (result != null) {
        this.username = result['name']
        this.authenticated = true
      } else {
        this.authenticated = false
      }
    })
  }
  public isMenuCollapsed = true;

}
