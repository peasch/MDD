import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Article} from "../../mdd/models/article.model";
import {User} from "../../mdd/models/user.model";
import {environment} from "../../../environments/environment";
import {Theme} from "../../mdd/models/theme.model";

@Injectable()
export class UserService {
  constructor(private httpClient: HttpClient) {}

  getUserById(id: number): Observable<User>{
    return this.httpClient.get<User>(`${environment.apiUrl}/api/user/${id}`);
  }
  getFollowedThemes():Observable<Theme[]> {
    return this.httpClient.get<Theme[]>(`${environment.apiUrl}/api/user/followed`);
  }
}
