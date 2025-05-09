import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '@app/services/auth.service';
import {
  BankAccountResponse,
  BankAccountService,
} from '@app/services/bank-account.service';
import {
  TransactionService,
  TransactionVerifyResponse,
} from '@app/services/transaction.service';
import { BackButtonForCardComponent } from '@app/shared/components/back-button-for-card/back-button-for-card.component';
import { ErrorResponse } from '@app/shared/error-response';
import { ButtonModule } from 'primeng/button';
import { ButtonGroupModule } from 'primeng/buttongroup';
import { CardModule } from 'primeng/card';
import { ChipModule } from 'primeng/chip';
import { InputTextModule } from 'primeng/inputtext';
import { ListboxModule } from 'primeng/listbox';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
@Component({
  selector: 'app-my-bank-account',
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
    ListboxModule,
    SelectModule,
    ChipModule,
    BackButtonForCardComponent,
  ],
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.scss',
})
export class TransferComponent implements OnInit {
  errorMessage = '';
  successMessage = '';

  bankAccounts: BankAccountResponse[] = [];

  transactionInfo: TransactionVerifyResponse = {
    valid: false,
    message: '',
    fromAccountName: '',
    fromAccountNumber: '',
    toAccountName: '',
    toAccountNumber: '',
    amount: 0,
  };

  isAuthenticated = false;

  verificationForm: any = null;
  confirmationForm: any = null;

  constructor(
    private router: Router,
    private bankAccountService: BankAccountService,
    private authService: AuthService,
    private transactionService: TransactionService
  ) {
    this.isAuthenticated = this.authService.isAuthenticated();
  }

  ngOnInit(): void {
    this.initVerficationForm();
  }

  initVerficationForm() {
    this.bankAccountService.getMyBankAccounts().subscribe({
      next: (response) => {
        if (response.length === 0) {
          this.errorMessage = 'No bank account found';
          return;
        }
        this.bankAccounts = response;
        this.verificationForm = new FormGroup({
          selectedBankAccount: new FormControl(this.bankAccounts[0], [
            Validators.required,
          ]),
          toAccountNumber: new FormControl('', [
            Validators.required,
            Validators.minLength(7),
            Validators.maxLength(7),
            Validators.pattern('^[0-9]*$'),
          ]),
          amount: new FormControl('', [
            Validators.required,
            Validators.min(1),
            Validators.pattern('^[0-9]*$'),
          ]),
        });
      },
      error: (error: ErrorResponse) => {
        switch (error.error.errorCode) {
          case 'TOKEN_EXPIRED_ERROR':
          case 'USER_NOT_FOUND_ERROR':
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
      },
    });
  }
  onVerify() {
    this.errorMessage = '';
    this.successMessage = '';
    if (this.verificationForm.invalid) {
      return;
    }
    const { selectedBankAccount, toAccountNumber, amount } =
      this.verificationForm.value;
    this.transactionService
      .verify(selectedBankAccount.accountNumber, toAccountNumber, amount)
      .subscribe({
        next: (response) => {
          if (response.valid) {
            this.successMessage = response.message;
            this.transactionInfo = response;
            this.confirmationForm = new FormGroup({
              pin: new FormControl('', [Validators.required]),
            });
          } else {
            this.errorMessage = response.message;
          }
        },
        error: (error: ErrorResponse) => {
          switch (error.error.errorCode) {
            case 'TOKEN_EXPIRED_ERROR':
            case 'USER_NOT_FOUND_ERROR':
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
        },
      });
  }

  onSubmit() {
    this.errorMessage = '';
    this.successMessage = '';
    if (this.confirmationForm.invalid) {
      return;
    }
    const { pin } = this.confirmationForm.value;
    const { fromAccountNumber, toAccountNumber, amount } = this.transactionInfo;

    this.transactionService
      .transfer(fromAccountNumber, toAccountNumber, amount, pin)
      .subscribe({
        next: (_) => {
          this.successMessage = 'Transfer successfully';
          this.confirmationForm.reset();
          this.confirmationForm = null;
          this.verificationForm.reset();
          this.initVerficationForm();
          this.transactionInfo = {
            valid: false,
            message: '',
            fromAccountName: '',
            fromAccountNumber: '',
            toAccountName: '',
            toAccountNumber: '',
            amount: 0,
          };
        },
        error: (error: ErrorResponse) => {
          switch (error.error.errorCode) {
            case 'TOKEN_EXPIRED_ERROR':
            case 'USER_NOT_FOUND_ERROR':
              this.authService.logout();
              this.router.navigate(['/login']);
              break;
            case 'NOT_CUSTOMER_ERROR':
              this.errorMessage = 'Only Customer can transfer funds!';
              break;
            case 'INVALID_PIN_ERROR':
              this.errorMessage = 'Invalid PIN!';
              break;
            case 'SENDER_BANK_ACCOUNT_NOT_FOUND_ERROR':
              this.errorMessage = 'Sender Bank Account not found!';
              break;
            case 'NOT_BANK_ACCOUNT_OWNER_ERROR':
              this.errorMessage = 'You are not a Bank account owner!';
              break;
            case 'RECIPIENT_BANK_ACCOUNT_NOT_FOUND_ERROR':
              this.errorMessage = 'Recipient Bank Account not found!';
              break;
            case 'INSUFFICIENT_FUNDS_ERROR':
              this.errorMessage = 'Insufficient funds!';
              break;
            default:
              this.errorMessage = 'Unexpected Error!';
              break;
          }
        },
      });
  }
}
