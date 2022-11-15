// Add your models here if you have any
export interface User {
    name: string
    username: string
    password: string
    dob: string
    image: File
    gender: string
    country: string
    state: string
    city: string
    interests: string
    introduction: string
}

export interface Trip {
    start: string
    end: string
    location: string
    type: string
    tripId: number
}

export interface Local {
    location: string
    activities: string
    description: string
}

export interface ChatMessage {
    sender: string
    recipient: string
    content: string
    timeStamp: string
}

export interface Chat {
    sender: string
    recipient: string
    lastMessage: string
    lastMessageTimeStamp: string
    newMessageCount: string
}

export interface ChatNotification {
    id: string
    sender: string
    recipient: string
}

