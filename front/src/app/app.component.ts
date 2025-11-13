import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "./Services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
/**
 * Composant racine de l'application Angular.
 *
 * Il sert de conteneur principal pour toute l'application et contient
 * généralement la structure de mise en page globale, comme :
 * - la barre de navigation
 * - le router-outlet
 * - les éléments partagés entre toutes les pages
 *
 * Le composant est initialisé automatiquement au lancement de l'application.
 */
export class AppComponent {

  /**
   * Titre par défaut de l'application.
   * Peut être utilisé dans la vue si nécessaire.
   */
  title = 'MDD';

}
