import { User } from "./user.model";

/**
 * Représente un commentaire associé à un article.
 *
 * Ce modèle correspond aux données renvoyées par l’API
 * pour afficher les commentaires dans l’application.
 */
export class Comment {

  /** Identifiant unique du commentaire. */
  id!: number;

  /** Contenu textuel du commentaire. */
  content!: string;

  /** Identifiant de l’auteur du commentaire. */
  authorId!: number;

  /** Date de création du commentaire (format ISO string). */
  createdAt!: string;
}
