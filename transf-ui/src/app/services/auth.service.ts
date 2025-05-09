import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../api-url.token';
import { LocalStorageService } from './local-storage.service';

export interface LoginResponse {
  token: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = inject(API_URL);
  private localStorageService = inject(LocalStorageService);

  public login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.apiUrl + '/auth/login', {
      email,
      password,
    });
  }

  logout() {
    this.localStorageService.clear();
  }

  public setToken(token: string) {
    this.localStorageService.setItem('token', token);
  }
  public getToken() {
    return this.localStorageService.getItem('token');
  }

  public setRole(role: string) {
    this.localStorageService.setItem('role', role);
  }
  public getRole() {
   return this.localStorageService.getItem('role');
  }
  public isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    return true;
  }
}
