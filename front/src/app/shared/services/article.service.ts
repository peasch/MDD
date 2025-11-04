import {Injectable} from "@angular/core";
import {Article} from "../../mdd/models/article.model";

import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable()
export class ArticleService {
  articles!: Article[];

  constructor(private httpClient: HttpClient) {
  }

  getFollowedArticles(): Observable<Article[]> {
    return this.httpClient.get<Article[]>(`${environment.apiUrl}/api/articles/followed`);
  }

  getArticleById(id: number): Observable<Article> {
    return this.httpClient.get<Article>(`${environment.apiUrl}/api/articles/${id}`);
  }

  saveArticle(article:any): Observable<Article>  {
   return this.httpClient.post<Article>(`${environment.apiUrl}/api/articles/add`,article);
  }
}
