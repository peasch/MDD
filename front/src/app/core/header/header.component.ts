import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';
import { SessionService } from '../../Services/session.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  cheminImage: string = '../assets/images/logo_p6.png';
  userImg: string = '../assets/images/user.png';

  mobileMenuOpen = false;

  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private sessionService: SessionService
  ) {}

  ngOnInit(): void {
    // Fermer le menu mobile à chaque changement de route
    this.router.events
      .pipe(
        filter((e): e is NavigationEnd => e instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        this.mobileMenuOpen = false;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate(['']);
  }

  // --- Hamburger / menu mobile ---
  public toggleMobileMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  public closeMobileMenu(): void {
    this.mobileMenuOpen = false;
  }

  // Si on repasse en desktop, on ferme le menu mobile
  @HostListener('window:resize', ['$event'])
  onResize(): void {
    if (window.innerWidth >= 600 && this.mobileMenuOpen) {
      this.mobileMenuOpen = false;
    }
  }
}
