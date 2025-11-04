import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ArticleComponent} from './components/article/article.component';
import {MddRoutingModule} from "./mdd-routing.module";
import {ThemeService} from "../shared/services/theme.service";
import {ThemeResolver} from "./Resolvers/themes.resolver";
import {MaterialModule} from "../shared/material.module";
import {ThemeListComponent} from './components/theme-list/theme-list.component';
import {ThemeListItemComponent} from './components/theme-list-item/theme-list-item.component';
import {ArticleListComponent} from './components/article-list/article-list.component';
import {ArticleService} from "../shared/services/article.service";
import {ArticleResolver} from "./Resolvers/article.resolver";
import {NewArticleComponent} from './components/new-article/new-article.component';
import {ArticleDetailsComponent} from './components/article-details/article-details.component';
import {CommentComponent} from './components/comment/comment.component';
import {CommentService} from "../shared/services/comment.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {UserService} from "../shared/services/user.service";


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
export class MddModule { }
