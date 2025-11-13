import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from "../shared/shared.module";
import { RouterModule } from "@angular/router";
import { HeaderComponent } from "./header/header.component";
import { HttpClientModule } from "@angular/common/http";

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
    HeaderComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    HttpClientModule
  ],
  exports: [
    HeaderComponent
  ]
})
export class CoreModule { }
