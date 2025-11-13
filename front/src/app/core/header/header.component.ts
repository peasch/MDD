import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';
import { SessionService } from '../../Services/session.service';

/**
 * Composant d'en-tête de l'application.
 *
 * Gère l'affichage du logo, du menu utilisateur, la déconnexion,
 * ainsi que le menu mobile (hamburger).
 * Le menu mobile se ferme automatiquement lors :
 * - d'un changement de route,
 * - d'un redimensionnement de la fenêtre passant en mode desktop.
 */
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  /** Chemin vers le logo du site. */
  cheminImage: string = '../assets/images/logo_p6.png';

  /** Image affichée pour représenter un utilisateur non connecté. */
  userImg: string = '../assets/images/user.png';

  /** Indique si le menu mobile (hamburger) est ouvert. */
  mobileMenuOpen = false;

  /**
   * Observable utilisé pour nettoyer les abonnements lors de la destruction du composant.
   * Empêche les fuites mémoire.
   */
  private destroy$ = new Subject<void>();

  /**
   * @param router Service Angular de navigation.
   * @param sessionService Service gérant l'état de session utilisateur.
   */
  constructor(
    private router: Router,
    private sessionService: SessionService
  ) {}

  /**
   * Initialise le composant.
   *
   * - Ferme automatiquement le menu mobile à chaque changement de route.
   */
  ngOnInit(): void {
    this.router.events
      .pipe(
        filter((e): e is NavigationEnd => e instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        this.mobileMenuOpen = false;
      });
  }

  /**
   * Nettoie les abonnements pour éviter les fuites mémoire.
   */
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Observable indiquant si l'utilisateur est connecté.
   *
   * @returns Observable<boolean> émis par le SessionService.
   */
  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }

  /**
   * Déconnecte l'utilisateur puis redirige vers la page d'accueil.
   */
  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate(['']);
  }

  // --- Gestion du menu mobile (hamburger) ---

  /**
   * Ouvre ou ferme le menu mobile.
   */
  public toggleMobileMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  /**
   * Ferme le menu mobile.
   */
  public closeMobileMenu(): void {
    this.mobileMenuOpen = false;
  }

  /**
   * Lors d'un redimensionnement de fenêtre :
   * - Si on repasse sur un écran ≥ 600px (desktop)
   * - Et que le menu mobile est ouvert
   * → Alors on ferme automatiquement le menu mobile.
   */
  @HostListener('window:resize', ['$event'])
  onResize(): void {
    if (window.innerWidth >= 600 && this.mobileMenuOpen) {
      this.mobileMenuOpen = false;
    }
  }
}
