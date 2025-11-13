import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import {User} from "../interfaces/user.interface";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
/**
 * Service gérant l'état de session de l'utilisateur.
 *
 * Fonctionnalités :
 * - Gestion du statut connecté/déconnecté
 * - Stockage et synchronisation du token dans le localStorage
 * - Stockage en mémoire de l'utilisateur connecté
 * - Exposition d'un Observable pour suivre l'état de connexion
 * - Restauration automatique de session à partir du token
 *
 * Ce service centralise la logique d'authentification côté client afin que
 * n'importe quel composant puisse s'abonner à l'état de connexion.
 */
export class SessionService {

  /**
   * Indique si l'utilisateur est actuellement connecté.
   * Cette valeur est synchronisée avec le BehaviorSubject interne.
   */
  public isLogged = false;

  /**
   * Référence de l'utilisateur connecté.
   * N'est définie que si la session est active.
   */
  public user: User | undefined;

  /**
   * BehaviorSubject permettant de diffuser l'état de connexion en temps réel.
   * Initialisé à `true` si un token est présent dans le localStorage.
   */
  private isLoggedSubject = new BehaviorSubject<boolean>(!!localStorage.getItem('token'));

  /**
   * @param authService Service d'authentification utilisé pour restaurer la session depuis un token.
   */
  constructor(private authService: AuthService) {}

  /**
   * Observable permettant de s'abonner à l'état de connexion de l'utilisateur.
   *
   * @returns Observable émettant `true` si l'utilisateur est connecté, `false` sinon.
   */
  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  /**
   * Active une session utilisateur.
   *
   * - Stocke l'utilisateur en mémoire
   * - Sauvegarde le token dans le localStorage
   * - Met à jour l'état de connexion
   * - Notifie les abonnés via le BehaviorSubject
   *
   * @param user L'utilisateur connecté.
   * @param token Token JWT associé à l'utilisateur.
   */
  public logIn(user: User, token: string): void {
    this.user = user;
    this.isLogged = true;
    localStorage.setItem('token', token);
    this.next();
  }

  /**
   * Déconnecte l'utilisateur.
   *
   * - Supprime le token du localStorage
   * - Nettoie l'utilisateur stocké en mémoire
   * - Met à jour l'état de connexion
   * - Notifie les abonnés
   */
  public logOut(): void {
    localStorage.removeItem('token');
    this.user = undefined;
    this.isLogged = false;
    this.next();
  }

  /**
   * Met à jour la valeur du BehaviorSubject interne
   * pour informer tous les abonnés d'un changement d'état.
   *
   * @private
   */
  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }

  /**
   * Restaure une session valide lorsque l'application démarre
   * ou lorsque seul un token est disponible.
   *
   * Le service appelle le backend pour récupérer les informations
   * de l'utilisateur associé au token.
   *
   * @param token Token JWT extrait du localStorage.
   */
  public restoreSessionFromToken(token: string): void {
    this.authService.getCurrentUser().subscribe((user: User) => {
      this.logIn(user, token);  // Restaurer la session
    });
  }
}
