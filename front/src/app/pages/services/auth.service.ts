import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {AuthSuccess} from "../../app/interfaces/authSuccess.interface";
import {LoginRequest} from "../../app/interfaces/loginRequest.interface";
import {RegisterRequest} from "../../app/interfaces/registerRequest.interface";
import {User} from "../../app/interfaces/user.interface";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private pathService = 'http://localhost:3001';

  constructor(private httpClient: HttpClient) {
  }

  public register(registerRequest: RegisterRequest): Observable<AuthSuccess> {
    return this.httpClient.post<AuthSuccess>(`${this.pathService}/api/auth/register`, registerRequest);
  }

  public login(loginRequest: LoginRequest): Observable<AuthSuccess> {
    return this.httpClient.post<AuthSuccess>(`${this.pathService}/api/auth/login`, loginRequest);
  }
  public me(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/api/auth/me`);
  }
}
