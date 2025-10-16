import { Component, OnInit } from '@angular/core';
import {SessionService} from "../../Services/session.service";
import {map, Observable} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  constructor(private sessionService: SessionService) {}

  ngOnInit(): void {}

  public $notLogged(): Observable<boolean> {
    return this.sessionService.$isLogged().pipe(
      map(isLogged => !isLogged)
    );
  }
  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged()

  }
}
