/**
 * Représente un utilisateur du système.
 *
 * Cette interface correspond aux données renvoyées par l’API
 * concernant un utilisateur authentifié ou récupéré via les services.
 */
export interface User {
  /** Identifiant unique de l'utilisateur. */
  id: number;

  /** Nom complet de l’utilisateur. */
  name: string;

  /** Adresse email de l’utilisateur. */
  email: string;

  /** Nom d’utilisateur (login) associé au compte. */
  username: string;
}
