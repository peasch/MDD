import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../Services/auth.service";
import {finalize, map, switchMap, take} from "rxjs";
import {User} from "../../mdd/models/user.model";
import {Theme} from "../../mdd/models/theme.model";
import {ThemeService} from "../../shared/services/theme.service";
import {UserService} from "../../shared/services/user.service";
import {Router} from "@angular/router";
import { SessionService } from "../../Services/session.service";

/**
 * Composant responsable de l'affichage et la gestion du profil utilisateur.
 *
 * Fonctionnalités :
 * - Affiche et permet la modification des informations utilisateur (username, email, mot de passe).
 * - Affiche les thèmes suivis + possibilité de se désabonner.
 * - Rafraîchit la session utilisateur après modification du profil (comme lors du login).
 * - Gère les messages de succès / erreur et l’état visuel du formulaire.
 */
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  /**
   * Formulaire réactif contenant les champs du profil.
   */
  form!: FormGroup;

  /**
   * Indique si le mot de passe doit être masqué.
   */
  hide = true;

  /**
   * Indique si une erreur s'est produite lors d’une action.
   */
  onError = false;

  /**
   * Utilisateur actuellement connecté.
   */
  user!: User;

  /**
   * Message de succès affiché après une mise à jour réussie.
   */
  successMessage: string | null = null;

  /**
   * Liste des thèmes suivis par l’utilisateur.
   */
  followedThemes: Theme[] = [];

  /**
   * Identifiant du thème en cours de désabonnement (pour éviter les actions multiples).
   */
  isUnfollowingId: number | null = null;

  /**
   * Pattern de validation du mot de passe :
   * - min. 8 caractères
   * - 1 majuscule
   * - 1 minuscule
   * - 1 chiffre
   * - 1 caractère spécial
   */
  private passwordPattern =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;

  /**
   * @param fb FormBuilder pour construire le formulaire.
   * @param authService Service d’authentification (me, getCurrentUser…)
   * @param cdr Détection manuelle de changement Angular.
   * @param themeService Gestion des abonnements aux thèmes.
   * @param userService Gestion des données utilisateur.
   * @param router Router Angular.
   * @param sessionService Service gérant la session locale (mise à jour après submit).
   */
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private themeService: ThemeService,
    private userService: UserService,
    private router: Router,
    private sessionService: SessionService
  ) {}

  /**
   * Lifecycle Angular : initialise le formulaire, les données utilisateur
   * et les thèmes suivis.
   */
  ngOnInit(): void {
    this.buildForm();
    this.loadUserIntoForm();
    this.loadFollowedThemes();
  }

  /**
   * Construit le formulaire de profil et applique les règles de validation.
   */
  private buildForm(): void {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.pattern(this.passwordPattern)]]
    });
  }

  /**
   * Charge les données utilisateur via `authService.me()` et les injecte dans le formulaire.
   */
  private loadUserIntoForm(): void {
    this.authService.me().pipe(take(1)).subscribe({
      next: (resp: any) => {
        this.user = resp?.user;
        const user: User = resp?.user ?? resp;

        this.form.patchValue({
          email: user?.email ?? '',
          username: user?.username ?? '',
          password: ''
        });

        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        this.onError = true;
        this.cdr.markForCheck();
      }
    });
  }

  /**
   * Charge la liste des thèmes suivis par l’utilisateur et supprime les doublons.
   */
  private loadFollowedThemes(): void {
    this.authService.getCurrentUser().pipe(
      take(1),
      map((response: any) => (response?.user?.followedThemes ?? []) as Theme[]),
      map(themes => this.dedupeThemes(themes))
    ).subscribe({
      next: themes => {
        this.followedThemes = themes;
        this.cdr.markForCheck();
      },
      error: err => console.error(err)
    });
  }

  // -------------------------------------------------------------------
  // UPDATE PROFIL + REFRESH SESSION (comme dans le login)
  // -------------------------------------------------------------------
  /**
   * Soumet le formulaire :
   * - demande une confirmation à l'utilisateur
   * - met à jour les données utilisateur
   * - recharge l’utilisateur via `authService.me()`
   * - met à jour la session via `sessionService.logIn()`
   * - réinitialise les états visuels du formulaire
   *
   * Le comportement est similaire à celui du composant Login.
   */
  onSubmit(): void {
    if (this.form.invalid) return;

    // 🔔 POPUP DE CONFIRMATION
    const confirmed = window.confirm(
      'Êtes-vous sûr de vouloir modifier vos coordonnées ? Vous allez être déconnectés.'

    );
    if (!confirmed) {
      return; // l'utilisateur annule -> on ne fait rien
    }

    const registerRequest = { ...this.form.value } as User;
    registerRequest.id = this.user.id;

    this.userService.updateUser(registerRequest)
      .pipe(
        take(1),
        switchMap(() => {
          const token = localStorage.getItem('token');

          return this.authService.me().pipe(
            take(1),
            map((resp: any) => ({
              user: resp?.user ?? resp,
              token: token ?? null
            }))
          );
        })
      )
      .subscribe({
        next: ({ user, token }) => {
          // 🔄 mise à jour immédiate dans le composant
          this.user = user;

          // 🔄 rafraîchissement de la session locale + déconnexion
          if (token) {
            this.sessionService.logOut();
            this.router.navigate(['mdd']);
          }

          // ✓ message de succès + reset UI
          this.successMessage = 'Informations correctement sauvegardées';
          this.onError = false;

          this.form.patchValue({ password: '' });
          this.form.markAsPristine();
          this.form.markAsUntouched();
          this.cdr.markForCheck();

          setTimeout(() => {
            this.successMessage = null;
            this.cdr.markForCheck();
          }, 10000);
        },
        error: (err) => {
          console.error(err);
          this.onError = true;
          this.successMessage = null;
          this.cdr.markForCheck();
        }
      });
  }


  // -------------------------------------------------------------------
  // Unfollow
  // -------------------------------------------------------------------

  /**
   * Désabonne l’utilisateur d’un thème.
   *
   * @param themeId ID du thème à unfollow
   *
   * Process :
   * - empêche les actions simultanées
   * - envoie la requête
   * - recharge la liste des thèmes à partir du user
   */
  unfollow(themeId: number): void {
    if (this.isUnfollowingId !== null) return;
    this.isUnfollowingId = themeId;

    this.themeService.unfollowTheme(themeId).pipe(
      take(1),
      switchMap(() =>
        this.authService.getCurrentUser().pipe(
          take(1),
          map((response: any) => (response?.user?.followedThemes ?? []) as Theme[]),
          map(themes => this.dedupeThemes(themes))
        )
      ),
      finalize(() => { this.isUnfollowingId = null; })
    ).subscribe({
      next: (themes) => {
        this.followedThemes = themes;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  /**
   * Supprime les doublons dans une liste de thèmes (par id / slug / fallback).
   */
  private dedupeThemes(themes: Theme[]): Theme[] {
    const seen = new Set<string | number>();
    const keyOf = (t: Theme) =>
      (t as any).id ?? (t as any).slug ?? `${(t as any).name ?? ''}|${(t as any).id ?? ''}`;

    const out: Theme[] = [];
    for (const t of themes ?? []) {
      const k = keyOf(t);
      if (!seen.has(k)) {
        seen.add(k);
        out.push(t);
      }
    }
    return out;
  }

  /**
   * Message d’erreur affiché sous le champ mot de passe.
   */
  get passwordError(): string | null {
    const control = this.form.get('password');
    if (control?.hasError('required')) return 'Le mot de passe est requis.';
    if (control?.hasError('pattern')) {
      return 'Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.';
    }
    return null;
  }
}
