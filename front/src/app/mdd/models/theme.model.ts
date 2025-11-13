/**
 * Représente un thème auquel un article peut appartenir
 * ou qu’un utilisateur peut suivre.
 *
 * Ces données proviennent de l’API et sont utilisées
 * dans les listes de thèmes et les composants de suivi.
 */
export class Theme {

  /** Identifiant unique du thème. */
  id!: number;

  /** Nom du thème. */
  name!: string;

  /** Description du thème. */
  description!: string;
}
