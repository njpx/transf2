import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '@app/services/auth.service';
import {
  BankAccountResponse,
  BankAccountService,
} from '@app/services/bank-account.service';
import {
  StatementResponse,
  TransactionService
} from '@app/services/transaction.service';
import { BackButtonForCardComponent } from '@app/shared/components/back-button-for-card/back-button-for-card.component';
import { ErrorResponse } from '@app/shared/error-response';
import { ButtonModule } from 'primeng/button';
import { ButtonGroupModule } from 'primeng/buttongroup';
import { CardModule } from 'primeng/card';
import { ChipModule } from 'primeng/chip';
import { DatePickerModule } from 'primeng/datepicker';
import { InputTextModule } from 'primeng/inputtext';
import { ListboxModule } from 'primeng/listbox';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
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
    DatePickerModule,
    TableModule,
    BackButtonForCardComponent,
  ],
  templateUrl: './my-bank-account.component.html',
  styleUrl: './my-bank-account.component.scss',
})
export class MyBankAccountComponent implements OnInit {
  errorMessage = '';
  successMessage = '';

  bankAccounts: BankAccountResponse[] = [];

  form: any = null;

  maxDate: Date = new Date();

  statements: StatementResponse[] = [];

  constructor(
    private router: Router,
    private bankAccountService: BankAccountService,
    private authService: AuthService,
    private fb: FormBuilder,
    private transactionService: TransactionService
  ) {}

  ngOnInit(): void {
    this.initialForm();
  }

  initialForm() {
    this.bankAccountService.getMyBankAccounts().subscribe({
      next: (response) => {
        if (response.length === 0) {
          this.errorMessage = 'No bank account found';
          return;
        }
        this.bankAccounts = response;
        this.form = this.fb.group({
          bankAccountInfo: this.fb.group({
            selectedBankAccount: [this.bankAccounts[0], [Validators.required]],
          }),
          dateRange: this.fb.group(
            {
              fromDate: ['', [Validators.required]],
              toDate: ['', [Validators.required]],
            },
            {
              validators: (group: AbstractControl) => {
                const from = group.get('fromDate')?.value;
                const to = group.get('toDate')?.value;
                return from && to && new Date(from) > new Date(to)
                  ? { dateRangeInvalid: true }
                  : null;
              },
            }
          ),
        });
      },
      error: (error: ErrorResponse) => {
        switch (error.error?.errorCode) {
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

  onShowStatement() {
    this.errorMessage = '';
    this.successMessage = '';
    if (this.form.invalid) {
      return;
    }
    const selectedBankAccount = this.form.get(
      'bankAccountInfo.selectedBankAccount'
    )?.value;
    const fromDate = this.formatDateToYYYYMMDD(
      this.form.get('dateRange.fromDate')?.value
    );
    const toDate = this.formatDateToYYYYMMDD(
      this.form.get('dateRange.toDate')?.value
    );

    if (
      !selectedBankAccount ||
      !selectedBankAccount.accountNumber ||
      !fromDate ||
      !toDate
    ) {
      this.errorMessage = 'Please select a valid date range!';
      return;
    }

    this.transactionService
      .getStatement(selectedBankAccount.accountNumber, fromDate, toDate)
      .subscribe({
        next: (response) => {
          this.statements = response;
        },
        error: (error: ErrorResponse) => {
          switch (error.error?.errorCode) {
            case 'TOKEN_EXPIRED_ERROR':
            case 'USER_NOT_FOUND_ERROR':
              this.authService.logout();
              this.router.navigate(['/login']);
              break;
            default:
              this.errorMessage = 'Unexpected Error!';
              break;
          }
        },
      });
  }

  private formatDateToYYYYMMDD(dateInput: any): string | null {
    if (!dateInput) {
      return null;
    }

    const date = new Date(dateInput);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Month is 0-based
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
}
