import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Article} from "../models/article.model";
import {ArticleService} from "../../shared/services/article.service";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
  }
)
export class ArticleResolver implements Resolve<Article[]> {

  constructor(private articleService: ArticleService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Article[]> {
    return this.articleService.getFollowedArticles();
  }


}
