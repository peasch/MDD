import { Theme } from "./theme.model";

/**
 * Représente un utilisateur de l’application.
 *
 * Ce modèle contient les informations essentielles d’un utilisateur
 * ainsi que la liste des thèmes qu’il suit.
 */
export class User {

  /** Identifiant unique de l’utilisateur. */
  id!: number;

  /** Adresse email de l’utilisateur. */
  email!: string;

  /** Nom d’utilisateur (login). */
  username!: string;

  /** Nom complet de l’utilisateur. */
  name!: string;

  /** Liste des thèmes suivis par l’utilisateur. */
  followedThemes!: Theme[];
}
