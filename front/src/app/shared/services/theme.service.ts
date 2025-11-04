import {Injectable} from "@angular/core";
import {Theme} from "../../mdd/models/theme.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable()
export class ThemeService {
  themes!: Theme[];

  constructor(private httpClient: HttpClient) {
  }

  getThemes(): Observable<Theme[]> {
    return this.httpClient.get<Theme[]>(`${environment.apiUrl}/api/theme/`);

  }

  unfollowTheme(themeId: number): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}/api/theme/unfollow/${themeId}`);
  }

  followTheme(themeId: number): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}/api/theme/follow/${themeId}`);
  }
}
