import { Component, OnInit } from '@angular/core';
import { SessionService } from "../../Services/session.service";
import { map, Observable } from "rxjs";

/**
 * Composant représentant la page d’accueil de l’application.
 *
 * Il permet de déterminer si un utilisateur est connecté ou non
 * afin d’adapter l’affichage (par exemple, afficher un bouton de connexion
 * ou l’accès direct au contenu).
 *
 * @example
 * <app-home></app-home>
 */
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  /**
   * @param sessionService Service permettant de vérifier l’état d’authentification.
   */
  constructor(private sessionService: SessionService) {}

  /** Cycle de vie Angular — rien à initialiser ici. */
  ngOnInit(): void {}

  /**
   * Observable indiquant si l'utilisateur **n'est pas** connecté.
   *
   * Utile pour afficher du contenu conditionnel dans le template,
   * comme les boutons "Se connecter" ou "S'inscrire".
   *
   * @returns `true` si l’utilisateur n’est pas connecté.
   */
  public $notLogged(): Observable<boolean> {
    return this.sessionService.$isLogged().pipe(
      map(isLogged => !isLogged)
    );
  }

  /**
   * Observable indiquant si l'utilisateur est connecté.
   *
   * @returns `true` si l’utilisateur est authentifié.
   */
  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }
}
