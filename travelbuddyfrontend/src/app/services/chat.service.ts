import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, Subject } from "rxjs";
import { Chat, ChatMessage } from "../components/models";

@Injectable({
    providedIn: 'root'
  })
export class ChatService {

    onActiveContactChange = new Subject<string>()

    constructor(private http: HttpClient) { }

    createChat(sender : string, recipient : string) {
        return firstValueFrom(
            this.http.get(`/api/createchat/${sender}/${recipient}`)
        )
    }

    getChats(sender : string) :Promise <Chat[]>{
        return firstValueFrom(
            this.http.get<Chat[]>(`/api/chats/${sender}`)
        )
    }

    findChatMessages(sender : string, recipient : string ) :Promise <ChatMessage[]>{
        return firstValueFrom(
            this.http.get<ChatMessage[]>(`/api/messages/${sender}/${recipient}`)
        )
    }

    findChatMessage(id : string) :Promise <ChatMessage>{
        return firstValueFrom(
            this.http.get<ChatMessage>(`/api/messages/${id}`)
        )
    }

    sendToken(token : string) {
        const form = new FormData
        form.set("token",token)
        return firstValueFrom(this.http.post('/api/savetoken', form)
        )
    }

    sendNotification(recipient : string, sender : string, message : string){
        const form = new FormData
        form.set("recipient",recipient)
        form.set("sender",sender)
        form.set("message",message)
        return firstValueFrom(this.http.post('/api/sendnotification', form)
        )
    }

}