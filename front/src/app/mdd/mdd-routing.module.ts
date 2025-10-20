import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";

import {ThemeResolver} from "./Resolvers/themes.resolver";
import {ThemeListComponent} from "./components/theme-list/theme-list.component";
import {ArticleListComponent} from "./components/article-list/article-list.component";
import {ArticleResolver} from "./Resolvers/article.resolver";
import {AuthGuard} from "../core/guards/auth.guard";
import {ArticleDetailsComponent} from "./components/article-details/article-details.component";

const routes: Routes = [
  {path: "themes", component: ThemeListComponent,resolve:{ themes: ThemeResolver },canActivate:[AuthGuard]},
  {path:"articles",component: ArticleListComponent,resolve:{ articles:ArticleResolver },canActivate:[AuthGuard]},
  {path:"articles/:id",component: ArticleDetailsComponent,canActivate:[AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MddRoutingModule {

}
