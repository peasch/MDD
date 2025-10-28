import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Article} from "../models/article.model";
import {User} from "../models/user.model";
import {environment} from "../../../environments/environment";

@Injectable()
export class UserService {
  constructor(private httpClient: HttpClient) {}

  getUserById(id: number): Observable<User>{
    return this.httpClient.get<User>(`${environment.apiUrl}/api/user/${id}`);
  }
}
