/**
 * Données nécessaires pour effectuer une demande de connexion.
 *
 * Cette interface représente le corps envoyé à l'API
 * lors d'une tentative de login.
 */
export interface LoginRequest {
  /** Adresse email de l'utilisateur. */
  email: string;

  /** Mot de passe de l'utilisateur. */
  password: string;
}
