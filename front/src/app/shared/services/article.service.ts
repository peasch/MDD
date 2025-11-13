import {Injectable} from "@angular/core";
import {Article} from "../../mdd/models/article.model";

import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable()
/**
 * Service responsable de l'accès aux données des articles.
 *
 * Fournit des méthodes pour :
 * - récupérer les articles suivis par l'utilisateur
 * - récupérer un article via son identifiant
 * - enregistrer ou créer un nouvel article
 *
 * Les appels sont effectués via HttpClient vers l’API configurée dans `environment.apiUrl`.
 */
export class ArticleService {

  /**
   * Cache local éventuel contenant une liste d'articles.
   * Peut être utilisé pour stocker des articles récupérés et éviter des requêtes répétées.
   */
  articles!: Article[];

  /**
   * @param httpClient Client HTTP Angular permettant de communiquer avec l’API backend.
   */
  constructor(private httpClient: HttpClient) {}

  /**
   * Récupère la liste des articles suivis par l'utilisateur connecté.
   *
   * Appelle l'endpoint :
   * `GET /api/articles/followed`
   *
   * @returns Observable émettant un tableau d'articles suivis.
   */
  getFollowedArticles(): Observable<Article[]> {
    return this.httpClient.get<Article[]>(`${environment.apiUrl}/api/articles/followed`);
  }

  /**
   * Récupère un article à partir de son identifiant.
   *
   * Appelle l'endpoint :
   * `GET /api/articles/:id`
   *
   * @param id Identifiant de l’article.
   * @returns Observable émettant l'article correspondant.
   */
  getArticleById(id: number): Observable<Article> {
    return this.httpClient.get<Article>(`${environment.apiUrl}/api/articles/${id}`);
  }

  /**
   * Enregistre un nouvel article (ou met à jour un article selon l'API).
   *
   * Appelle l'endpoint :
   * `POST /api/articles/add`
   *
   * @param article Données de l’article à créer ou mettre à jour.
   * @returns Observable émettant l’article enregistré.
   */
  saveArticle(article: any): Observable<Article>  {
    return this.httpClient.post<Article>(`${environment.apiUrl}/api/articles/add`, article);
  }
}
