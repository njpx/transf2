import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../api-url.token';

export interface CreateBankAccountResponse {
  accountNumber: string;
}

export interface BankAccountResponse {
  accountNumber: string;
  balance: number;
}
@Injectable({
  providedIn: 'root',
})
export class BankAccountService {
  private http = inject(HttpClient);
  private apiUrl = inject(API_URL);

  public createNewBankAccount(
    customerId: string,
    initialDeposit: number
  ): Observable<CreateBankAccountResponse> {
    return this.http.post<CreateBankAccountResponse>(
      this.apiUrl + '/bank-accounts/create',
      { customerId, initialDeposit }
    );
  }

  public getMyBankAccounts(): Observable<BankAccountResponse[]> {
    return this.http.get<BankAccountResponse[]>(
      this.apiUrl + '/bank-accounts/my-bank-accounts'
    );
  }
}
