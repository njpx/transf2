import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { API_URL } from '../api-url.token';
@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  private http = inject(HttpClient);
  private apiUrl = inject(API_URL);

  public register(
    email: string,
    password: string,
    id: string,
    thaiName: string,
    englishName: string,
    pin: string
  ) {
    return this.http.post(this.apiUrl + '/auth/register', {
      email,
      password,
      idCardNumber: id,
      thaiName,
      englishName,
      pin,
    });
  }
}
