import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../../Services/auth.service";
import { RegisterRequest } from "../../interfaces/registerRequest.interface";
import { AuthSuccess } from "../../interfaces/authSuccess.interface";
import { take, map, switchMap, finalize } from "rxjs";
import { User } from "../../mdd/models/user.model";
import { Theme } from "../../mdd/models/theme.model";
import { ThemeService } from "../../shared/services/theme.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  form!: FormGroup;
  hide = true;
  onError = false;

  followedThemes: Theme[] = [];
  isUnfollowingId: number | null = null; // pour désactiver le bouton pendant l'appel

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private themeService: ThemeService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadUserIntoForm();
    this.loadFollowedThemes();
  }

  // ---------------------------
  // Init helpers
  // ---------------------------
  private buildForm(): void {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['']
    });
  }

  private loadUserIntoForm(): void {
    this.authService.me().pipe(take(1)).subscribe({
      next: (resp: any) => {
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

  // ---------------------------
  // Actions
  // ---------------------------
  onSubmit(): void {
    const registerRequest = this.form.value as RegisterRequest;
    this.authService.register(registerRequest).subscribe(
      (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
      },
      () => this.onError = true
    );
  }

  /** Appelée par le bouton "Se désabonner" (unfollow) */
  unfollow(themeId: number): void {
    if (this.isUnfollowingId !== null) return; // évite double-clic
    this.isUnfollowingId = themeId;

    this.themeService.unfollowTheme(themeId).pipe(
      take(1),
      // une fois l’API OK, on recharge la liste des thèmes suivis
      switchMap(() =>
        this.authService.getCurrentUser().pipe(
          take(1),
          map((response: any) => (response?.user?.followedThemes ?? []) as Theme[]),
          map(themes => this.dedupeThemes(themes))
        )
      ),
      finalize(() => {
        this.isUnfollowingId = null;
      })
    ).subscribe({
      next: (themes) => {
        this.followedThemes = themes; // mise à jour de l’affichage
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error(err);
        // Optionnel : message d'erreur utilisateur
      }
    });
  }

  // ---------------------------
  // Utils
  // ---------------------------
  /** Dédoublonne par id puis par slug, fallback sur nom|id */
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

  /** trackBy pour éviter les re-renders bizarres */
  trackByTheme = (_: number, t: any) => t?.id ?? t?.slug ?? `${t?.name}|${t?.id}`;
}
