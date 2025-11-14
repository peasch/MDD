/**
 * Représente un article publié dans l'application.
 *
 * Ce modèle correspond aux données renvoyées par l'API
 * et utilisées dans les composants affichant des articles.
 */
export class Article {

  /** Identifiant unique de l’article. */
  id!: number;

  /** Titre de l’article. */
  title!: string;

  /** Identifiant du thème associé à l’article. */
  themeId!: number;

  /** Contenu principal de l’article. */
  content!: string;

  /** Date de création de l’article (format ISO string). */
  createdAt!: string;

  /** Date de dernière mise à jour (format ISO string). */
  updatedAt!: string;

  /** Identifiant de l’auteur de l’article. */
  authorId!: number;

  /** Nom d’utilisateur de l’auteur (utile pour l’affichage rapide). */
  authorUsername!: string;
}
