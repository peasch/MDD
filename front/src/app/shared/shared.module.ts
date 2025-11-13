import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MaterialModule} from "./material.module";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MaterialModule
  ],
  exports: [
    MaterialModule
  ]
})
/**
 * Module partagé de l'application.
 *
 * Ce module centralise les fonctionnalités, modules et composants
 * susceptibles d'être utilisés à travers plusieurs autres modules.
 *
 * Actuellement, il :
 * - importe `CommonModule` (offrant les directives Angular de base, comme *ngIf, *ngFor)
 * - réexporte `MaterialModule` afin de rendre disponible l'ensemble des composants Angular Material à tout module important `SharedModule`
 *
 * Le `SharedModule` a pour objectif de :
 * - éviter la duplication d'importations dans les modules fonctionnels
 * - faciliter la maintenance
 * - offrir un point central pour les éléments réutilisables
 *
 * À étendre selon les besoins futurs (pipes, directives, composants partagés…).
 */
export class SharedModule {}
