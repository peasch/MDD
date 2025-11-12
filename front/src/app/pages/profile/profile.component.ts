import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../Services/auth.service";
import {finalize, map, switchMap, take} from "rxjs";
import {User} from "../../mdd/models/user.model";
import {Theme} from "../../mdd/models/theme.model";
import {ThemeService} from "../../shared/services/theme.service";
import {UserService} from "../../shared/services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  form!: FormGroup;
  hide = true;
  onError = false;
  user!: User;

  /** ✅ Nouveau: message de succès */
  successMessage: string | null = null;

  followedThemes: Theme[] = [];
  isUnfollowingId: number | null = null;

  private passwordPattern =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private themeService: ThemeService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadUserIntoForm();
    this.loadFollowedThemes();
  }

  private buildForm(): void {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.pattern(this.passwordPattern)]]
    });
  }

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
    if (this.form.invalid) return;

    const registerRequest = { ...this.form.value } as User;
    registerRequest.id = this.user.id;

    this.userService.updateUser(registerRequest).pipe(take(1)).subscribe({
      next: (response: any) => {
        // ✅ Afficher le message de succès dans le template
        this.successMessage = 'Informations correctement sauvegardées';
        this.onError = false;

        // Optionnel: on nettoie le champ mot de passe et on remet le form en pristine
        this.form.patchValue({ password: '' });
        this.form.markAsPristine();
        this.form.markAsUntouched();

        this.cdr.markForCheck();

        // Optionnel: masquer automatiquement après X secondes
        setTimeout(() => {
          this.successMessage = null;
          this.cdr.markForCheck();
        }, 4000);
      },
      error: (err) => {
        console.error(err);
        this.onError = true;
        this.successMessage = null;
        this.cdr.markForCheck();
      }
    });
  }

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

  // ---------------------------
  // Utils
  // ---------------------------
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

  get passwordError(): string | null {
    const control = this.form.get('password');
    if (control?.hasError('required')) return 'Le mot de passe est requis.';
    if (control?.hasError('pattern')) {
      return 'Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.';
    }
    return null;
  }
}
