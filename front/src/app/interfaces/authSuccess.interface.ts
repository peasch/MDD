/**
 * Représente la réponse de l'API lors d'une authentification réussie.
 *
 * Contient le token JWT retourné par le backend.
 */
export interface AuthSuccess {
  /** Jeton JWT fourni après une connexion réussie. */
  token: string;
}
