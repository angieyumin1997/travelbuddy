import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { authInterceptorProviders } from './_helpers/auth.interceptor';
import { FrontpageComponent } from './components/frontpage/frontpage.component';
import { LoginComponent } from './components/login/login.component';
import { TripsComponent } from './components/trips/trips.component';
import { RegisterComponent } from './components/register/register.component';
import { UserComponent } from './components/user/user.component';
import { LocalsComponent } from './components/locals/locals.component';
import { LocalformComponent } from './components/localform/localform.component';
import { TripformComponent } from './components/tripform/tripform.component';
import { EdituserComponent } from './components/edituser/edituser.component';
import { ChatsComponent } from './components/chats/chats.component';
import { environment } from '../environments/environment';
import { AngularFireModule } from '@angular/fire/compat';
import { AngularFireMessagingModule } from '@angular/fire/compat/messaging';

const appRoutes: Routes = [
  {path:'',component: FrontpageComponent},
  {path:'login',component: LoginComponent},
  {path:'register',component: RegisterComponent},
  {path:'trips',component: TripsComponent},
  {path:'locals',component: LocalsComponent},
  {path:'chats',component: ChatsComponent},
  {path:'user/:username',component: UserComponent},
  {path: '**', redirectTo:'/',pathMatch:'full'}
]
export const MY_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'YYYY-MM-DD',
    monthYearLabel: 'YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'YYYY',
  },
};
@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    FrontpageComponent,
    LoginComponent,
    TripsComponent,
    UserComponent,
    LocalsComponent,
    LocalformComponent,
    TripformComponent,
    EdituserComponent,
    ChatsComponent,
  ],
  imports: [
    BrowserModule,
    NgbModule,
    RouterModule.forRoot(appRoutes, { useHash: true }),
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule, ReactiveFormsModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFireMessagingModule,
  ],
  providers: [authInterceptorProviders, 
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

