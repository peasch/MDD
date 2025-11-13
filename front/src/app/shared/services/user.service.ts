import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {User} from "../../mdd/models/user.model";
import {environment} from "../../../environments/environment";
import {Theme} from "../../mdd/models/theme.model";

@Injectable()
/**
 * Service responsable de la gestion des utilisateurs.
 *
 * Fonctionnalités :
 * - Récupérer un utilisateur par son identifiant
 * - Récupérer les thèmes suivis par l'utilisateur courant
 * - Mettre à jour les informations d'un utilisateur
 *
 * Toutes les opérations s'effectuent via les endpoints du backend sous `/api/user`.
 */
export class UserService {

  /**
   * @param httpClient Client HTTP utilisé pour communiquer avec l’API backend.
   */
  constructor(private httpClient: HttpClient) {}

  /**
   * Récupère un utilisateur en fonction de son identifiant.
   *
   * Endpoint :
   * `GET /api/user/:id`
   *
   * @param id Identifiant de l'utilisateur.
   * @returns Observable émettant l'utilisateur correspondant.
   */
  getUserById(id: number): Observable<User> {
    return this.httpClient.get<User>(`${environment.apiUrl}/api/user/${id}`);
  }

  /**
   * Récupère la liste des thèmes suivis par l'utilisateur connecté.
   *
   * Endpoint :
   * `GET /api/user/followed`
   *
   * @returns Observable émettant la liste des thèmes suivis.
   */
  getFollowedThemes(): Observable<Theme[]> {
    return this.httpClient.get<Theme[]>(`${environment.apiUrl}/api/user/followed`);
  }

  /**
   * Met à jour les informations d'un utilisateur.
   *
   * Endpoint :
   * `PUT /api/user/update`
   *
   * @param user Objet utilisateur contenant les nouvelles données.
   * @returns Observable émettant l'utilisateur mis à jour.
   */
  updateUser(user: User): Observable<User> {
    return this.httpClient.put<User>(`${environment.apiUrl}/api/user/update`, user);
  }
}
