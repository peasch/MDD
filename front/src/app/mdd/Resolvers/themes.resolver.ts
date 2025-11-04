import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Theme} from "../models/theme.model";
import {ThemeService} from "../../shared/services/theme.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
  }
)
export class ThemeResolver implements Resolve<Theme[]> {
constructor(private themeService: ThemeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Theme[]>{
  return this.themeService.getThemes();
  }

}
