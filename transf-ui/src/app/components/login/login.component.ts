import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '@app/services/auth.service';
import { ErrorResponse } from '@app/shared/error-response';
import { ButtonModule } from 'primeng/button';
import { ButtonGroupModule } from 'primeng/buttongroup';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    RouterModule,
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    InputTextModule,
    ButtonGroupModule,
    ButtonModule,
    MessageModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  message = '';
  loading = false;

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  constructor(private router: Router, private authService: AuthService) {}

  onSubmit() {
    this.loading = true;
    this.message = '';
    if (this.loginForm.invalid) {
      return;
    }
    const { email, password } = this.loginForm.value;
    if (!email || !password) {
      return;
    }
    this.authService.login(email, password).subscribe({
      next: (response) => {
        if (response['token']) {
          this.authService.setToken(response['token']);
          this.authService.setRole(response['role']);
        } else {
          throw new Error('No token received');
        }
        this.router.navigate(['/dashboard']);
      },
      error: (error: ErrorResponse) => {
        switch (error.error?.errorCode) {
          case 'INVALID_CREDENTIALS_ERROR':
            this.message = 'Invalid Username/Password!';
            break;
          default:
            this.message = 'Unexpected Error!';
            break;
        }
        this.loading = false;
      },
    });
  }
}
