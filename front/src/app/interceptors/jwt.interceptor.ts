import {HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";

/**
 * Intercepteur HTTP responsable d'ajouter automatiquement
 * le token JWT aux requêtes sortantes lorsqu'il est présent
 * dans le localStorage.
 *
 * Le token est ajouté dans l'en-tête :
 * `Authorization: Bearer <token>`
 *
 * Cela permet d'authentifier toutes les requêtes nécessitant
 * un accès protégé à l'API.
 *
 * @example
 * // Fourniture de l'intercepteur dans app.module.ts :
 * {
 *   provide: HTTP_INTERCEPTORS,
 *   useClass: JwtInterceptor,
 *   multi: true
 * }
 */
@Injectable({ providedIn: 'root' })
export class JwtInterceptor implements HttpInterceptor {

  constructor() {}

  /**
   * Intercepte chaque requête HTTP sortante.
   *
   * - Récupère le token JWT depuis le localStorage.
   * - Si le token existe, clone la requête et ajoute l'en-tête `Authorization`.
   *
   * @param request Requête HTTP en cours.
   * @param next Gestionnaire permettant de poursuivre la chaîne d'interception.
   * @returns La requête originale ou modifiée, passée au handler suivant.
   */
  public intercept(request: HttpRequest<any>, next: HttpHandler) {
    const token = localStorage.getItem('token');

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(request);
  }
}
