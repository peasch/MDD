import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import {User} from "../interfaces/user.interface";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  public isLogged = false;
  public user:User | undefined;
  constructor(private authService: AuthService) {}
  private isLoggedSubject = new BehaviorSubject<boolean>(!!localStorage.getItem('token'));

  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public logIn(user: User, token: string): void {
    this.user = user;
    this.isLogged = true;
    localStorage.setItem('token', token);
    this.next();
  }

  public logOut(): void {
    localStorage.removeItem('token');
    this.user = undefined;
    this.isLogged = false;
    this.next();
  }

  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }
  public restoreSessionFromToken(token: string): void {
    this.authService.getCurrentUser().subscribe((user: User) => {
      this.logIn(user, token);  // Restaurer la session
    });
  }
}
