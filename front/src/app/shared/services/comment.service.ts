import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable()
/**
 * Service responsable de la gestion des commentaires liés aux articles.
 *
 * Fonctionnalités :
 * - Récupérer les commentaires associés à un article
 * - Ajouter un nouveau commentaire sur un article
 *
 * Communique avec le backend via les endpoints /api/comments.
 */
export class CommentService {

  /**
   * Cache optionnel pour stocker temporairement une liste de commentaires.
   * Non utilisé directement dans les méthodes actuelles, mais disponible
   * pour optimiser des chargements futurs si nécessaire.
   */
  comments!: Comment[];

  /**
   * @param http Client HTTP utilisé pour effectuer les requêtes vers l’API.
   */
  constructor(private http: HttpClient) { }

  /**
   * Récupère la liste des commentaires d'un article donné.
   *
   * Endpoint appelé :
   * `GET /api/comments/:id`
   *
   * @param id Identifiant de l'article.
   * @returns Observable émettant la liste des commentaires associés à l’article.
   */
  getCommentsOfArticle(id: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${environment.apiUrl}/api/comments/` + id);
  }

  /**
   * Ajoute un commentaire à un article.
   *
   * Endpoint appelé :
   * `POST /api/comments/add/:id`
   *
   * @param id Identifiant de l’article.
   * @param content Contenu du commentaire à ajouter.
   * @returns Observable émettant le commentaire nouvellement créé.
   */
  addComment(id: number, content: string): Observable<Comment> {
    return this.http.post<Comment>(`${environment.apiUrl}/api/comments/add/` + id, content);
  }

}
