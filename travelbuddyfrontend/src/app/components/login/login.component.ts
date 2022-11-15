import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterService } from 'src/app/services/register.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { User } from '../models';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form!: FormGroup
  hide = true;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  constructor(private tokenStorage: TokenStorageService, private fb: FormBuilder, private regSvc: RegisterService, private router: Router) { }

  ngOnInit(): void {
    this.form = this.createForm()
  }

  hasError(ctrlName: string) {
    return this.form.get(ctrlName)?.hasError('required')
  }

  private createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control<string>('', [Validators.required, Validators.minLength(3)]),
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(6)]),
    })
  }

  processForm() {
    const user: User = this.form.value as User
    console.info('>>> reg: ', user)
    this.regSvc.signIn(user.username, user.password)
      .subscribe({
        next: data => {
          this.tokenStorage.saveToken(data.accessToken);
          this.tokenStorage.saveUser(data);
          this.isLoginFailed = false;
          this.isLoggedIn = true;
          this.roles = this.tokenStorage.getUser().roles;
          this.regSvc.onAuthenticateUser.next(null)
          this.router.navigate(['/'])
        },
        error: err => {
          this.errorMessage = "Invalid username or password";
          this.isLoginFailed = true;
        }
      });
  }

}
