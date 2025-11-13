import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthService } from "../../Services/auth.service";
import { SessionService } from "../../Services/session.service";
import { Router } from "@angular/router";
import { LoginRequest } from "../../interfaces/loginRequest.interface";
import { AuthSuccess } from "../../interfaces/authSuccess.interface";
import { User } from "../../interfaces/user.interface";
import { finalize, map, switchMap, take } from 'rxjs/operators';

/**
 * Composant de page de connexion.
 *
 * Fonctionnalités :
 * - Affiche un formulaire de login (email + mot de passe).
 * - Envoie les identifiants à l’API.
 * - Enregistre le token en localStorage et en session en cas de succès.
 * - Redirige l’utilisateur vers la liste des articles.
 * - Affiche des messages d’erreur lisibles en cas d’échec.
 *
 * @example
 * <app-login></app-login>
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  /** Indique si le mot de passe doit être masqué ou non dans le champ. */
  public hide = false;

  /** Indique si une requête de connexion est en cours. */
  public isLoading = false;

  /** Message d’erreur à afficher dans le template. */
  public errorMessage: string | null = null;

  /**
   * Formulaire réactif de connexion.
   *
   * Champs :
   * - email : requis
   * - password : requis, longueur minimale 3 caractères
   */
  public form = this.fb.group({
    email: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(3)]]
  });

  /**
   * @param authService Service gérant l’authentification (login, me, etc.).
   * @param fb FormBuilder pour créer le formulaire réactif.
   * @param router Service de navigation Angular.
   * @param sessionService Service de gestion de la session utilisateur.
   */
  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService
  ) {}

  /**
   * À l'initialisation :
   * - vérifie si un token existe déjà en localStorage ;
   * - si oui, redirige directement vers la page des articles.
   */
  ngOnInit(): void {
    const existingToken = localStorage.getItem('token');
    if (existingToken) {
      this.router.navigate(['mdd/articles']);
      return;
    }
  }

  /**
   * Soumet le formulaire de connexion.
   *
   * - Valide le formulaire.
   * - Appelle l’API de login.
   * - Stocke le token en localStorage.
   * - Récupère les informations de l’utilisateur (`me`).
   * - Initialise la session et redirige vers `/mdd/articles`.
   * - Gère les erreurs (403, erreur réseau, autres).
   */
  public onSubmit(): void {
    this.errorMessage = null;

    if (this.form.invalid || this.isLoading) return;

    this.isLoading = true;
    const loginRequest = this.form.value as LoginRequest;

    this.authService.login(loginRequest)
      .pipe(
        take(1),
        switchMap((response: AuthSuccess) => {
          localStorage.setItem('token', response.token);
          return this.authService.me().pipe(
            take(1),
            map((user: User) => ({ user, token: response.token }))
          );
        }),
        finalize(() => this.isLoading = false)
      )
      .subscribe({
        next: ({ user, token }) => {
          this.sessionService.logIn(user, token);
          this.router.navigate(['mdd/articles']);
        },
        error: (err) => {
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
