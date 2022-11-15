importScripts('https://www.gstatic.com/firebasejs/9.2.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.2.0/firebase-messaging-compat.js');

firebase.initializeApp({
  projectId: 'travelbuddy-366813',
  appId: '1:557646628350:web:40a71f7a53ea70f5776caf',
  storageBucket: 'travelbuddy-366813.appspot.com',
  apiKey: 'AIzaSyDBgeavkoKxlGlYSrQhaEsUq90m2x0RiLE',
  authDomain: 'travelbuddy-366813.firebaseapp.com',
  messagingSenderId: '557646628350',
})

const messaging = firebase.messaging();