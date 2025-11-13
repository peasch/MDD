import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArticleComponent } from './components/article/article.component';
import { MddRoutingModule } from "./mdd-routing.module";
import { ThemeService } from "../shared/services/theme.service";
import { ThemeResolver } from "./Resolvers/themes.resolver";
import { MaterialModule } from "../shared/material.module";
import { ThemeListComponent } from './components/theme-list/theme-list.component';
import { ThemeListItemComponent } from './components/theme-list-item/theme-list-item.component';
import { ArticleListComponent } from './components/article-list/article-list.component';
import { ArticleService } from "../shared/services/article.service";
import { ArticleResolver } from "./Resolvers/article.resolver";
import { NewArticleComponent } from './components/new-article/new-article.component';
import { ArticleDetailsComponent } from './components/article-details/article-details.component';
import { CommentComponent } from './components/comment/comment.component';
import { CommentService } from "../shared/services/comment.service";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { UserService } from "../shared/services/user.service";

/**
 * Module principal du domaine MDD .
 *
 * Ce module regroupe :
 * - tous les composants liés aux articles et aux thèmes ;
 * - les resolvers nécessaires au préchargement des données ;
 * - les services spécifiques à la gestion des articles, thèmes, commentaires et utilisateurs ;
 * - les formulaires (reactives + template-driven) nécessaires à la création d’articles.
 *
 * Il est chargé via `MddRoutingModule` et exporte les composants destinés
 * à être utilisés en dehors du module (ex. intégration dans des pages).
 *
 * @example
 * // Chargement dans app.module.ts
 * @NgModule({
 *   imports: [MddModule]
 * })
 */
@NgModule({
  declarations: [
    ArticleComponent,
    ThemeListComponent,
    ThemeListItemComponent,
    ArticleListComponent,
    NewArticleComponent,
    ArticleDetailsComponent,
    CommentComponent
  ],
  imports: [
    CommonModule,
    MddRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [
    ThemeService,
    ThemeResolver,
    ArticleService,
    ArticleResolver,
    CommentService,
    UserService
  ],
  exports: [
    MaterialModule,
    ThemeListComponent,
    ThemeListItemComponent,
    ArticleListComponent,
    ArticleDetailsComponent,
    NewArticleComponent
  ]
})
export class MddModule {}
