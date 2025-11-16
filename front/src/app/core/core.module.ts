import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from "../shared/shared.module";
import { RouterModule } from "@angular/router";
import { HeaderComponent } from "./header/header.component";
import { HttpClientModule } from "@angular/common/http";
import { FooterComponent } from './footer/footer.component';
import {MaterialModule} from "../shared/material.module";

/**
 * Module principal (CoreModule) regroupant les composants et services
 * essentiels au fonctionnement global de l'application.
 *
 * Ce module contient notamment le composant d'en-tête ainsi que
 * divers modules nécessaires à l'ensemble du projet.
 *
 * ⚠️ Le CoreModule doit être importé **uniquement dans AppModule**,
 * jamais dans des modules lazy-loaded.
 *
 * @example
 * // Dans app.module.ts
 * @NgModule({
 *   imports: [CoreModule]
 * })
 * export class AppModule {}
 */
@NgModule({
  declarations: [
    HeaderComponent,

  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    HttpClientModule,
    FooterComponent,
    MaterialModule
  ],
    exports: [
        HeaderComponent,
        FooterComponent
    ]
})
export class CoreModule { }
