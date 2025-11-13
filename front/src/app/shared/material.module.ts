import {NgModule} from "@angular/core";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatListModule} from "@angular/material/list";
import {MatIconModule} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatSelectModule} from "@angular/material/select";

@NgModule({
  exports:[
    MatToolbarModule,
    MatCardModule,
    MatButtonModule,
    MatListModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    MatGridListModule,
    MatSelectModule
  ]
})
/**
 * Module Angular regroupant et réexportant les modules Angular Material utilisés dans l'application.
 *
 * Ce module a pour objectif de :
 * - centraliser les imports Angular Material
 * - simplifier les imports dans les autres modules de l'application
 * - garantir une organisation cohérente et maintenable
 *
 * Pour utiliser un composant Material, il suffit d'importer ce module
 * dans le module où le composant sera utilisé.
 */
export class MaterialModule {}
