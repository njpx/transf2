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
import { RegisterService } from '@app/services/register.service';
import { BackButtonForCardComponent } from '@app/shared/components/back-button-for-card/back-button-for-card.component';
import { ErrorResponse } from '@app/shared/error-response';
import { ButtonModule } from 'primeng/button';
import { ButtonGroupModule } from 'primeng/buttongroup';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
@Component({
  selector: 'app-register',
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
    BackButtonForCardComponent,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  errorMessage = '';
  loading = false;

  isAuthenticated = false;

  form = new FormGroup({
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(50),
    ]),
    id: new FormControl('', [
      Validators.required,
      Validators.minLength(13),
      Validators.maxLength(13),
      Validators.pattern('^[0-9]*$'),
    ]),
    thaiName: new FormControl('', [
      Validators.required,
      Validators.pattern('^[ก-๛\\s]+$'),
    ]),
    englishName: new FormControl('', [
      Validators.required,
      Validators.pattern('^[a-zA-Z\\s]+$'),
    ]),
    pin: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      Validators.maxLength(6),
      Validators.pattern('^[0-9]*$'),
    ]),
  });

  constructor(
    private router: Router,
    private registerService: RegisterService,
    private authService: AuthService
  ) {
    this.isAuthenticated = this.authService.isAuthenticated();
  }

  onSubmit() {
    this.loading = true;
    this.errorMessage = '';
    if (this.form.invalid) {
      this.loading = false;
      return;
    }
    const { email, password, id, thaiName, englishName, pin } = this.form.value;
    if (!email || !password || !id || !thaiName || !englishName || !pin) {
      this.loading = false;
      return;
    }
    this.registerService
      .register(email, password, id, thaiName, englishName, pin)
      .subscribe({
        next: (_) => {
          this.router.navigate(['/dashboard']);
        },
        error: (error: ErrorResponse) => {
          switch (error.error.errorCode) {
            case 'TOKEN_EXPIRED_ERROR':
            case 'USER_NOT_FOUND_ERROR':
              this.authService.logout();
              this.router.navigate(['/login']);
              break;
            case 'NOT_CUSTOMER_ERROR':
              this.errorMessage = 'Only Customer can create Bank account!';
              break;
            case 'BANK_ACCOUNT_NOT_FOUND_ERROR':
              this.errorMessage = 'Bank Account not found!';
              break;
            default:
              this.errorMessage = 'Unexpected Error!';
              break;
          }
          this.loading = false;
        },
      });
  }
}
