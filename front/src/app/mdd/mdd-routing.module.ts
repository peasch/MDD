import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";

import {ThemeResolver} from "./themes.resolver";
import {ThemeListComponent} from "./components/theme-list/theme-list.component";

const routes: Routes = [
  {path: "", component: ThemeListComponent,resolve:{ Themes: ThemeResolver }},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MddRoutingModule {

}
