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
import { BankAccountService } from '@app/services/bank-account.service';
import { TransactionService } from '@app/services/transaction.service';
import { UserService } from '@app/services/user.service';
import { BackButtonForCardComponent } from '@app/shared/components/back-button-for-card/back-button-for-card.component';
import { ErrorResponse } from '@app/shared/error-response';
import { ButtonModule } from 'primeng/button';
import { ButtonGroupModule } from 'primeng/buttongroup';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
@Component({
  selector: 'app-deposit',
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
  templateUrl: './deposit.component.html',
  styleUrl: './deposit.component.scss',
})
export class DepositComponent {
  errorMessage = '';
  successMessage = '';
  loading = false;

  isAuthenticated = false;
  form = new FormGroup({
    accountNumber: new FormControl('', [
      Validators.required,
      Validators.minLength(7),
      Validators.maxLength(7),
      Validators.pattern('^[0-9]*$'),
    ]),
    depositAmount: new FormControl('', [
      Validators.required,
      Validators.min(1),
      Validators.pattern('^[0-9]*$'),
    ]),
  });

  constructor(
    private router: Router,
    private bankAccountService: BankAccountService,
    private authService: AuthService,
    private userService: UserService,
    private transactionService: TransactionService
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
    const { accountNumber, depositAmount } = this.form.value;
    if (!accountNumber || !depositAmount) {
      this.loading = false;
      return;
    }
    this.transactionService
      .deposit(accountNumber, parseFloat(depositAmount))
      .subscribe({
        next: (_) => {
          this.successMessage = 'Deposit successfully';
          this.form.reset();
        },
        error: (error: ErrorResponse) => {
          switch (error.error.errorCode) {
            case 'TOKEN_EXPIRED_ERROR':
              this.authService.logout();
              this.router.navigate(['/login']);
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
