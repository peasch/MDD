import {Injectable} from "@angular/core";
import {Theme} from "../../mdd/models/theme.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable()
/**
 * Service responsable de la gestion des thèmes suivis par l'utilisateur.
 *
 * Fonctionnalités :
 * - Récupérer tous les thèmes disponibles
 * - Suivre un thème
 * - Se désabonner d’un thème
 *
 * Ce service interagit avec les endpoints `/api/theme`.
 */
export class ThemeService {

  /**
   * Cache local éventuel contenant la liste des thèmes.
   * Non exploité directement dans les méthodes actuelles, mais utilisable
   * pour éviter des appels répétitifs si une optimisation est envisagée.
   */
  themes!: Theme[];

  /**
   * @param httpClient Client HTTP Angular utilisé pour effectuer les appels API.
   */
  constructor(private httpClient: HttpClient) {}

  /**
   * Récupère l’ensemble des thèmes disponibles.
   *
   * Endpoint appelé :
   * `GET /api/theme/`
   *
   * @returns Observable émettant la liste des thèmes.
   */
  getThemes(): Observable<Theme[]> {
    return this.httpClient.get<Theme[]>(`${environment.apiUrl}/api/theme/`);
  }

  /**
   * Désabonne l'utilisateur du thème spécifié.
   *
   * Endpoint appelé :
   * `GET /api/theme/unfollow/:themeId`
   *
   * ⚠️ Remarque : Une requête GET n'est normalement pas destinée
   * à modifier l’état côté serveur. Une requête DELETE ou POST serait plus appropriée.
   *
   * @param themeId Identifiant du thème à ne plus suivre.
   * @returns Observable émettant la réponse du backend.
   */
  unfollowTheme(themeId: number): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}/api/theme/unfollow/${themeId}`);
  }

  /**
   * Ajoute un thème à la liste des thèmes suivis par l'utilisateur.
   *
   * Endpoint appelé :
   * `GET /api/theme/follow/:themeId`
   *
   * ⚠️ Remarque : Idéalement, une requête POST serait plus appropriée
   * pour une action de modification.
   *
   * @param themeId Identifiant du thème à suivre.
   * @returns Observable émettant la réponse du backend.
   */
  followTheme(themeId: number): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}/api/theme/follow/${themeId}`);
  }
}
