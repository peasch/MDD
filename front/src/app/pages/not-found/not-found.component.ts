import { Component, OnInit } from '@angular/core';

/**
 * Composant affiché lorsqu’une page ou ressource demandée n’existe pas.
 *
 * Il s'agit de la page **404 – Not Found** de l'application.
 * Le composant affiche une image illustrant l’erreur et un message
 * permettant à l’utilisateur de comprendre qu’il s'est égaré.
 *
 * @example
 * <app-not-found></app-not-found>
 */
@Component({
  selector: 'app-not-found',
  templateUrl: './not-found.component.html',
  styleUrls: ['./not-found.component.scss']
})
export class NotFoundComponent implements OnInit {

  /** Chemin de l’image affichée dans la page 404. */
  cheminImage: string = "../assets/images/deadLink.jpg";

  constructor() {}

  /** Cycle de vie - aucune logique nécessaire ici. */
  ngOnInit(): void {}
}
