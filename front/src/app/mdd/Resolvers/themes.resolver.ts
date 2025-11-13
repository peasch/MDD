import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from "@angular/router";
import { Theme } from "../models/theme.model";
import { ThemeService } from "../../shared/services/theme.service";
import { Observable } from "rxjs";

/**
 * Resolver chargé de récupérer la liste complète des thèmes avant
 * l’activation de la route.
 *
 * Cela permet aux composants d’accéder directement aux thèmes via :
 * `this.route.data['themes']`
 *
 * @example
 * {
 *   path: 'themes',
 *   component: ThemeListComponent,
 *   resolve: { themes: ThemeResolver }
 * }
 */
@Injectable({
  providedIn: 'root'
})
export class ThemeResolver implements Resolve<Theme[]> {

  /**
   * @param themeService Service permettant de récupérer les thèmes.
   */
  constructor(private themeService: ThemeService) {}

  /**
   * Récupère la liste de tous les thèmes avant le chargement de la route.
   *
   * @param route Snapshot de la route active.
   * @param state État du routeur (non utilisé ici mais requis).
   * @returns Observable contenant la liste des thèmes.
   */
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Theme[]> {
    return this.themeService.getThemes();
  }
}
