import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from "@angular/router";
import { Article } from "../models/article.model";
import { ArticleService } from "../../shared/services/article.service";
import { Observable } from "rxjs";

/**
 * Resolver chargé de récupérer les articles suivis par l’utilisateur
 * avant le chargement de la route.
 *
 * Ce resolver permet d’assurer que la liste des articles est disponible
 * directement dans le composant cible via `route.data`.
 *
 * @example
 * // Exemple d’utilisation dans le routing :
 * {
 *   path: 'articles',
 *   component: ArticleListComponent,
 *   resolve: { articles: ArticleResolver }
 * }
 */
@Injectable({
  providedIn: 'root'
})
export class ArticleResolver implements Resolve<Article[]> {

  /**
   * @param articleService Service permettant de récupérer les articles suivis.
   */
  constructor(private articleService: ArticleService) {}

  /**
   * Méthode exécutée automatiquement par Angular avant d'activer la route.
   *
   * @param route Snapshot de la route active.
   * @param state État du routeur (non utilisé ici mais requis).
   * @returns Observable contenant la liste des articles suivis.
   */
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Article[]> {
    return this.articleService.getFollowedArticles();
  }
}
