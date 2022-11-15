import { NgModule } from "@angular/core";
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { MatInputModule } from '@angular/material/input'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatSelectModule} from '@angular/material/select'
import { MatSidenavModule } from '@angular/material/sidenav'
import { MatMenuModule } from '@angular/material/menu'
import { MatListModule } from '@angular/material/list'
import { MatStepperModule} from '@angular/material/stepper';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatNativeDateModule, MatRippleModule } from '@angular/material/core';
import {MatRadioModule} from '@angular/material/radio';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatCardModule} from '@angular/material/card';
import {MatTabsModule} from '@angular/material/tabs';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatSnackBarModule} from '@angular/material/snack-bar';

const matModules: any[] = [
  MatToolbarModule, MatButtonModule, MatListModule,
  MatIconModule, MatInputModule, MatFormFieldModule,
  MatSelectModule, MatSidenavModule, MatMenuModule,
  MatStepperModule, MatDatepickerModule,
  BrowserAnimationsModule, MatNativeDateModule, 
  MatRippleModule, MatRadioModule,MatCheckboxModule,
  MatCardModule,MatTabsModule,MatAutocompleteModule,
  MatSnackBarModule
]

@NgModule({
  imports: matModules,
  exports: matModules
})
export class MaterialModule {}
