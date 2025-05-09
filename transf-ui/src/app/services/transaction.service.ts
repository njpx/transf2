import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { from, Observable } from 'rxjs';
import { API_URL } from '../api-url.token';

export interface TransactionVerifyResponse {
  valid: boolean;
  message: string;
  fromAccountName: string;
  fromAccountNumber: string;
  toAccountName: string;
  toAccountNumber: string;
  amount: number;
}

export interface StatementResponse {
  fromAccountNumber: string;
  toAccountNumber: string;
  amount: number;
  code: string;
  channel: string;
  remarks: string;
  timestamp: string; // LocalDateTime in string format
  postBalance: number;
}

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  private http = inject(HttpClient);
  private apiUrl = inject(API_URL);

  public deposit(
    accountNumber: string,
    depositAmount: number
  ): Observable<void> {
    return this.http.post<void>(this.apiUrl + '/transactions/deposit', {
      toAccountNumber: accountNumber,
      amount: depositAmount,
      channel: 'TELLER',
    });
  }

  public verify(
    fromAccountNumber: string,
    toAccountNumber: string,
    amount: number
  ): Observable<TransactionVerifyResponse> {
    return this.http.post<TransactionVerifyResponse>(
      this.apiUrl + '/transactions/verify',
      {
        fromAccountNumber,
        toAccountNumber,
        amount,
      }
    );
  }

  public transfer(
    fromAccountNumber: string,
    toAccountNumber: string,
    amount: number,
    pin: string
  ): Observable<void> {
    return this.http.post<void>(this.apiUrl + '/transactions/transfer', {
      fromAccountNumber,
      toAccountNumber,
      amount,
      pin,
      channel: 'ONLINE',
    });
  }

  public getStatement(
    bankAccountNumber: string,
    fromDate: string,
    toDate: string
  ): Observable<StatementResponse[]> {
    return this.http.get<StatementResponse[]>(
      this.apiUrl + '/transactions/statements',
      {
        params: {
          bankAccountNumber,
          fromDate,
          toDate,
        },
      }
    );
  }
}
