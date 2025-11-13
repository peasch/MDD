import { Injectable } from "@angular/core";
import { CanActivate, Router } from "@angular/router";
import { catchError, Observable, of, switchMap } from "rxjs";
import { SessionService } from "../../Services/session.service";
import { AuthService } from "../../Services/auth.service";

/**
 * Guard permettant de protéger les routes nécessitant une authentification.
 *
 * Ce guard vérifie :
 * - si l'utilisateur est déjà authentifié en session ;
 * - s'il existe un token dans le localStorage ;
 * - sinon, il tente de récupérer les informations utilisateur via l'API.
 *
 * En cas d'échec, l'utilisateur est redirigé vers la page de login.
 *
 * @example
 * // Exemple d'utilisation dans app-routing.module.ts
 * {
 *   path: 'dashboard',
 *   canActivate: [AuthGuard],
 *   component: DashboardComponent
 * }
 */
@Injectable({
  providedIn: "root"
})
export class AuthGuard implements CanActivate {

  /**
   * @param sessionService Service gérant l'état de la session utilisateur.
   * @param router Service Angular permettant la navigation.
   * @param authService Service permettant d'appeler l'API d'authentification.
   */
  constructor(
    private sessionService: SessionService,
    private router: Router,
    private authService: AuthService
  ) {}

  /**
   * Méthode déterminant si l'accès à la route est autorisé.
   *
   * @returns `true` si l'utilisateur est authentifié, sinon une redirection vers `/login`.
   */
  public canActivate(): Observable<boolean> | boolean {

    // Si l'utilisateur est déjà connecté en session
    if (this.sessionService.isLogged) {
      return true;
    }

    // S'il n'y a pas de token en localStorage : login obligatoire
    if (!localStorage.getItem('token')) {
      this.router.navigate(['login']);
      return false;
    }

    // Vérifie le token en appelant l’API
    return this.authService.me().pipe(
      switchMap((user: any) => {
        const token = localStorage.getItem('token');
        this.sessionService.user = { ...this.sessionService.user, ...user };
        this.sessionService.logIn(user, token!);
        return of(true);
      }),
      catchError(async () => this.router.navigate(['login']))
    );
  }
}
