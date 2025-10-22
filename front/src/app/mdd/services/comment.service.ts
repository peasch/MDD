import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable()
export class CommentService{
  comments!:Comment[];

  constructor(private http:HttpClient) { }

  getCommentsOfArticle(id:number):Observable<Comment[]>{
    return this.http.get<Comment[]>(`${environment.apiUrl}/api/comments/`+id);
  }
}
