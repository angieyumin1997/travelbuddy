import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ChatService } from 'src/app/services/chat.service';
import { RegisterService } from 'src/app/services/register.service';
import { Chat, ChatMessage } from '../models';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import * as moment from 'moment';
import { FormControl } from '@angular/forms';
import { AngularFireMessaging } from '@angular/fire/compat/messaging';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-chats',
  templateUrl: './chats.component.html',
  styleUrls: ['./chats.component.css']
})
export class ChatsComponent implements OnInit, OnDestroy {
  @ViewChild('endOfChat')
  endOfChat!: ElementRef;
  stompClient: any;
  username !: string
  chats: Chat[] = []
  chatMessages: ChatMessage[] = []
  activeContact !: string
  messageControl = new FormControl('');
  token = false

  constructor(private regSvc: RegisterService, private chatSvc: ChatService, private msg: AngularFireMessaging, private http: HttpClient) { }

  ngOnInit(): void {
    this.authenticate()
  }

  notifications() {
    this.msg.requestToken.subscribe(token => {
      console.log(token);
      if(token){
        this.chatSvc.sendToken(token).then(result =>{
          if(result){
            this.token = true
          }
        })
      } 
    }, error =>{
      console.log(error);
    });
  }

  async onContactsChange(newValue: any) {
    this.activeContact = newValue.options[0].value
    if (this.activeContact == newValue.options[0].value) {
      this.findChatMessages()
      this.chatSvc.onActiveContactChange.next(this.activeContact)
    }
  }

  connect() {
    this.stompClient = Stomp.over(() => new SockJS("https://yuminproject2.herokuapp.com/ws"));
    const _this = this;
    _this.stompClient.connect({}, function (frame: any) {
      _this.stompClient.subscribe("/user/" + _this.username + "/queue/messages", function (sdkEvent: any) {
        _this.onMessageReceived(sdkEvent);
      });
      //_this.stompClient.reconnect_delay = 2000;
    }, this.onError);
  };
  onError(err: any) {
    console.log(err);
  };

  sendMessage() {
    if (this.stompClient) {
      const msg = this.messageControl.value;
      if (msg) {
        const message = {
          sender: this.username,
          recipient: this.activeContact,
          content: msg,
          timeStamp: moment().format("MM-DD-YYYY hh:mm A"),
        };
        this.stompClient.send("/app/chat", {}, JSON.stringify(message))
        if (this.chatMessages.push(message) != 0) {
          this.scrollToBottom()
        }
        var i: number = 0
        var target: number = -1
        this.chats.forEach(chat => {
          if (chat.recipient == this.activeContact) {
            chat.lastMessage = message.content
            chat.lastMessageTimeStamp = message.timeStamp
            target = i
            if (target != -1) {
              if (this.chats.splice(target, 1))
                this.chats.unshift(chat)
            }
          }
          i++
        })
        this.chatSvc.sendNotification(message.recipient, message.sender, message.content)
        .then(result=>{
          if(result){
            console.info(">>>>>>sending firebase notifcations", result)
          }
        }).catch(e=>{
          console.info(">>>>>>error sending firebase notifcations", e)
        })
      }
    }
    this.messageControl.setValue('')
  }

  onMessageReceived(msg: any) {
    const notification = JSON.parse(msg.body);
    if (this.activeContact === notification.sender) {
      this.chatSvc.findChatMessage(notification.id).then((message) => {
        this.findChatMessages()
      });
    } else {
      console.info("Received a new message from " + notification.sender);
      this.getChats(this.username);
    }
  }

  async findChatMessages() {
    await this.chatSvc.findChatMessages(this.username, this.activeContact).then(result => {
      this.chatMessages = result
      this.getChats(this.username)
    })
    this.scrollToBottom();
  }

  getChats(sender: string) {
    this.chatSvc.getChats(sender).then(result => {
      this.chats = result
    })
  }

  async authenticate() {
    await this.regSvc.authenticate().then((result) => {
      if (result != null) {
        this.username = result['name']
      }
    })
    this.getChats(this.username)
    this.connect()
  }

  scrollToBottom() {
    setTimeout(() => {
      if (this.endOfChat) {
        this.endOfChat.nativeElement.scrollIntoView({ behavior: 'smooth' });
      }
    }, 100);
  }

  ngOnDestroy() {
    this.stompClient.disconnect();
    this.chatSvc.onActiveContactChange.next('')
  }

}
