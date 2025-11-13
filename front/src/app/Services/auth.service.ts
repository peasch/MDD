import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {AuthSuccess} from "../interfaces/authSuccess.interface";
import {LoginRequest} from "../interfaces/loginRequest.interface";
import {RegisterRequest} from "../interfaces/registerRequest.interface";
import {User} from "../interfaces/user.interface";

@Injectable({
  providedIn: 'root'
})
/**
 * Service d'authentification gérant l'inscription, la connexion
 * et la récupération des informations de l'utilisateur courant.
 *
 * Il communique avec l'API backend via HTTP pour :
 * - créer un nouveau compte utilisateur
 * - authentifier un utilisateur existant
 * - récupérer les informations du profil utilisateur connecté
 */
export class AuthService {

  /**
   * URL de base du backend.
   * Point centralisé pour toutes les routes d'authentification.
   */
  private pathService = 'http://localhost:3001';

  /**
   * @param httpClient Client HTTP Angular utilisé pour communiquer avec l’API backend.
   */
  constructor(private httpClient: HttpClient) {}

  /**
   * Enregistre un nouvel utilisateur.
   *
   * @param registerRequest Données d'inscription incluant email, nom et mot de passe.
   * @returns Observable émettant un objet {@link AuthSuccess} contenant notamment le token JWT.
   */
  public register(registerRequest: RegisterRequest): Observable<AuthSuccess> {
    return this.httpClient.post<AuthSuccess>(`${this.pathService}/api/auth/register`, registerRequest);
  }

  /**
   * Authentifie un utilisateur existant via email et mot de passe.
   *
   * @param loginRequest Données de connexion.
   * @returns Observable émettant un objet {@link AuthSuccess} contenant le token JWT.
   */
  public login(loginRequest: LoginRequest): Observable<AuthSuccess> {
    return this.httpClient.post<AuthSuccess>(`${this.pathService}/api/auth/login`, loginRequest);
  }

  /**
   * Récupère les informations de l'utilisateur actuellement connecté.
   *
   * Cette méthode nécessite que l'utilisateur soit déjà authentifié
   * et qu'un token valide soit envoyé dans les headers (géré par un interceptor ou autre).
   *
   * @returns Observable émettant un objet {@link User}.
   */
  public me(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/api/auth/me`);
  }

  /**
   * Alias de la méthode {@link me}.
   * Fournie pour des raisons de lisibilité ou de rétro-compatibilité.
   *
   * @returns Observable émettant un objet {@link User}.
   */
  public getCurrentUser(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/api/auth/me`);
  }
}
