import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { API_URL } from '../api-url.token';
import { Observable } from 'rxjs';

export interface UserResponse {
  thaiName: string;
  englishName: string;
  role: string;
}

export interface SearchUserResponse {
  id: string;
  idCardNumber: string;
  thaiName: string;
  englishName: string;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = inject(API_URL);

  public getUserDetail(): Observable<UserResponse> {
    return this.http.get<UserResponse>(this.apiUrl + '/users/my-profile');
  }

  public searchUser(keyword: string): Observable<SearchUserResponse[]> {
    const options = keyword
      ? { params: new HttpParams().set('keyword', keyword) }
      : {};
    return this.http.get<SearchUserResponse[]>(
      this.apiUrl + '/users/search',
      options
    );
  }
}
