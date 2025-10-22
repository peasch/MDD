import {Injectable} from "@angular/core";
import {Theme} from "../models/theme.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable()
export class ThemeService {
  themes!:Theme[];

  constructor(private httpClient:HttpClient) {}

    getThemes(): Observable<Theme[]> {
      return this.httpClient.get<Theme[]>(`${environment.apiUrl}/api/theme/`);

  }

}
