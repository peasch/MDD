import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {AuthService} from "../../Services/auth.service";
import {SessionService} from "../../Services/session.service";
import {Router} from "@angular/router";
import {LoginRequest} from "../../interfaces/loginRequest.interface";
import {AuthSuccess} from "../../interfaces/authSuccess.interface";
import {User} from "../../interfaces/user.interface";
import {finalize, map, switchMap, take} from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public hide = false;
  public isLoading = false;

  /** Message d’erreur à afficher dans le template */
  public errorMessage: string | null = null;

  public form = this.fb.group({
    email: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(3)]]
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService
  ) {
  }

  ngOnInit(): void {
    const existingToken = localStorage.getItem('token');
    if (existingToken) {
      this.router.navigate(['mdd/articles']);
      return;
    }
  }

  public onSubmit(): void {
    this.errorMessage = null;

    if (this.form.invalid || this.isLoading) return;

    this.isLoading = true;
    const loginRequest = this.form.value as LoginRequest;

    this.authService.login(loginRequest).pipe(take(1), switchMap((response: AuthSuccess) => {
      localStorage.setItem('token', response.token);
      return this.authService.me().pipe(take(1), map((user: User) => ({user, token: response.token})));
    }), finalize(() => this.isLoading = false))
      .subscribe({
        next: ({user, token}) => {
          this.sessionService.logIn(user, token);
          this.router.navigate(['mdd/articles']);
        },
        error: (err) => {
          // 🔥 Message d’erreur lisible
          if (err.status === 403) {
            this.errorMessage = "Identifiants invalides. Veuillez réessayer.";
          } else if (err.status === 0) {
            this.errorMessage = "Impossible de contacter le serveur.";
          } else {
            this.errorMessage = "Une erreur est survenue. Veuillez réessayer plus tard.";
          }
          localStorage.removeItem('token');

        }
      });
  }
}
