import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";

import {ThemeResolver} from "./themes.resolver";
import {ThemeListComponent} from "./components/theme-list/theme-list.component";
import {ArticleListComponent} from "./components/article-list/article-list.component";
import {ArticleResolver} from "./article.resolver";

const routes: Routes = [
  {path: "themes", component: ThemeListComponent,resolve:{ themes: ThemeResolver }},
  {path:"articles",component: ArticleListComponent,resolve:{ articles:ArticleResolver }}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MddRoutingModule {

}
