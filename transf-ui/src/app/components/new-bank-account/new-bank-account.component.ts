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
import { SearchUserResponse, UserService } from '@app/services/user.service';
import { BackButtonForCardComponent } from '@app/shared/components/back-button-for-card/back-button-for-card.component';
import { ErrorResponse } from '@app/shared/error-response';
import { ButtonModule } from 'primeng/button';
import { ButtonGroupModule } from 'primeng/buttongroup';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
@Component({
  selector: 'app-new-bank-account',
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
    SelectModule,
    BackButtonForCardComponent,
  ],
  templateUrl: './new-bank-account.component.html',
  styleUrl: './new-bank-account.component.scss',
})
export class NewBankAccountComponent {
  errorMessage = '';
  successMessage = '';

  isAuthenticated = false;
  searchForm = new FormGroup({
    keyword: new FormControl('', [Validators.required]),
  });
  form: any = null;

  customers: SearchUserResponse[] = [];

  constructor(
    private router: Router,
    private bankAccountService: BankAccountService,
    private authService: AuthService,
    private userService: UserService
  ) {
    this.isAuthenticated = this.authService.isAuthenticated();
  }
  onSearch() {
    this.errorMessage = '';
    if (
      this.searchForm.invalid ||
      !this.searchForm.value.keyword ||
      this.searchForm.value.keyword === '' ||
      this.searchForm.value.keyword === null
    ) {
      return;
    }
    this.userService.searchUser(this.searchForm.value.keyword).subscribe({
      next: (response) => {
        if (response.length === 0) {
          this.errorMessage = 'User not found';
          if (this.form) {
            this.form.reset();
            this.form = null;
          }
          return;
        }
        this.customers = response;
        this.form = new FormGroup({
          selectedCustomer: new FormControl(this.customers[0], [
            Validators.required,
          ]),
          depositAmount: new FormControl(0, [
            Validators.min(0),
            Validators.pattern('^[0-9]*$'),
          ]),
        });
      },
      error: (error: ErrorResponse) => {
        switch (error.error?.errorCode) {
          case 'TOKEN_EXPIRED_ERROR':
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

  onSubmit() {
    this.errorMessage = '';
    if (this.form.invalid) {
      return;
    }
    const { selectedCustomer, depositAmount } = this.form.value;
    if (!selectedCustomer || !selectedCustomer.id) {
      this.errorMessage = 'Please select customer!';
      return;
    }
    this.bankAccountService
      .createNewBankAccount(selectedCustomer.id, depositAmount)
      .subscribe({
        next: (_) => {
          this.successMessage = 'Create new bank account successfully';
          this.form.reset();
          this.form = null;
          this.searchForm.reset();
        },
        error: (error: ErrorResponse) => {
          switch (error.error?.errorCode) {
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
        },
      });
  }
}
