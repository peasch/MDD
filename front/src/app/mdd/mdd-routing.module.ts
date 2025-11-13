import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { ThemeResolver } from "./Resolvers/themes.resolver";
import { ThemeListComponent } from "./components/theme-list/theme-list.component";
import { ArticleListComponent } from "./components/article-list/article-list.component";
import { ArticleResolver } from "./Resolvers/article.resolver";
import { AuthGuard } from "../core/guards/auth.guard";
import { ArticleDetailsComponent } from "./components/article-details/article-details.component";
import { NewArticleComponent } from "./components/new-article/new-article.component";
import { HomeComponent } from "../pages/home/home.component";

/**
 * Module de routage du domaine MDD (articles & thèmes).
 *
 * Ce module :
 * - définit toutes les routes nécessaires à la gestion des articles et thèmes ;
 * - protège les routes via `AuthGuard` ;
 * - utilise des resolvers pour précharger les données (`ThemeResolver`, `ArticleResolver`) ;
 * - gère la navigation vers la page de création et de détails des articles.
 *
 * @example
 * // Chargement dans MddModule :
 * @NgModule({
 *   imports: [MddRoutingModule]
 * })
 */
const routes: Routes = [
  /** Redirection par défaut vers la liste des articles */
  { path: '', redirectTo: 'articles', pathMatch: 'full' },

  /**
   * Liste des thèmes
   * - protégée par AuthGuard
   * - précharge les thèmes via ThemeResolver
   */
  {
    path: "themes",
    component: ThemeListComponent,
    resolve: { themes: ThemeResolver },
    canActivate: [AuthGuard]
  },

  /**
   * Liste des articles
   * - protégée par AuthGuard
   * - précharge les articles suivis via ArticleResolver
   */
  {
    path: "articles",
    component: ArticleListComponent,
    resolve: { articles: ArticleResolver },
    canActivate: [AuthGuard]
  },

  /**
   * Détails d’un article selon son id
   * - protégée par AuthGuard
   */
  {
    path: "articles/:id",
    component: ArticleDetailsComponent,
    canActivate: [AuthGuard]
  },

  /**
   * Création d’un nouvel article
   * - protégée par AuthGuard
   */
  {
    path: "newArticle",
    component: NewArticleComponent,
    canActivate: [AuthGuard]
  }
];

/**
 * Module Angular déclarant les routes du domaine MDD.
 */
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MddRoutingModule {}
