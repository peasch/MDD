/**
 * Données nécessaires pour créer un nouvel utilisateur.
 *
 * Cette interface représente le corps de la requête envoyée à l’API
 * lors de l’inscription d’un utilisateur.
 */
export interface RegisterRequest {
  /** Adresse email de l'utilisateur. */
  email: string;

  /** Nom complet de l'utilisateur. */
  name: string;

  /** Mot de passe choisi par l'utilisateur. */
  password: string;
}
