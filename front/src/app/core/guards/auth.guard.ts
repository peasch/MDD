import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router";
import {catchError, Observable, of, switchMap} from "rxjs";
import {SessionService} from "../../Services/session.service";
import {AuthService} from "../../Services/auth.service";

@Injectable({
  providedIn: "root"
})
export class AuthGuard implements CanActivate {
  constructor(private sessionService: SessionService,
              private router: Router,
              private authService: AuthService) {
  }


  public canActivate(): Observable<boolean> | boolean {
    if (this.sessionService.isLogged) {
      return true;
    }

    if (!localStorage.getItem('token')) {
      this.router.navigate(['login']);
      return false;
    }

    return this.authService.me().pipe(
      switchMap((user: any) => {
        const token = localStorage.getItem('token');
        this.sessionService.user = {...this.sessionService.user, ...user};
        this.sessionService.logIn(user, token!);
        return of(true);
      }),
      catchError(async () => this.router.navigate(['login']))
    );
  }
}
