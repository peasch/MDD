import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Theme } from '../../models/theme.model';
import { User } from '../../models/user.model';
import { AuthService } from '../../../Services/auth.service';
import { ThemeService } from '../../../shared/services/theme.service';

/**
 * Élément individuel de la liste des thèmes.
 *
 * Ce composant :
 * - affiche un thème ;
 * - récupère l’utilisateur courant et ses thèmes suivis ;
 * - permet de suivre ou de ne plus suivre un thème ;
 * - émet un événement vers le parent lorsque l’état du thème change.
 *
 * @example
 * <app-theme-list-item
 *    [theme]="t"
 *    (changed)="onThemeChanged($event)">
 * </app-theme-list-item>
 */
@Component({
  selector: 'app-theme-list-item',
  templateUrl: './theme-list-item.component.html',
  styleUrls: ['./theme-list-item.component.scss']
})
export class ThemeListItemComponent implements OnInit {

  /**
   * Thème à afficher dans l’élément.
   */
  @Input() theme!: Theme;

  /**
   * Événement émis lorsqu'un thème est suivi ou ne l'est plus.
   *
   * Contient :
   * - `id` : identifiant du thème
   * - `followed` : nouvel état du suivi
   */
  @Output() changed = new EventEmitter<{ id: number; followed: boolean }>();

  /** Utilisateur actuellement connecté. */
  currentUser!: User;

  /** Liste des thèmes suivis par l'utilisateur. */
  followedThemes: Theme[] = [];

  /**
   * @param authService Service récupérant l’utilisateur connecté.
   * @param themeService Service permettant de suivre / ne plus suivre un thème.
   */
  constructor(
    private authService: AuthService,
    private themeService: ThemeService
  ) {}

  /**
   * Initialise le composant :
   * - récupère l’utilisateur courant avec ses thèmes suivis.
   */
  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (response: any) => {
        this.currentUser = response.user;
        this.followedThemes = response.user.followedThemes || [];
      },
      error: (err) => console.error(err)
    });
  }

  /**
   * Vérifie si l’utilisateur suit déjà un thème.
   *
   * @param themeId Identifiant du thème à vérifier.
   * @returns `true` si l’utilisateur suit ce thème, sinon `false`.
   */
  hasTheme(themeId: number): boolean {
    return this.followedThemes?.some(theme => theme.id === themeId);
  }

  /**
   * Suit ou arrête de suivre un thème en fonction de l’état actuel.
   *
   * - Met à jour la liste locale des thèmes suivis
   * - Notifie le parent du changement via l’EventEmitter
   *
   * @param themeId Identifiant du thème à modifier.
   */
  changeFollow(themeId: number): void {
    const isFollowed = this.hasTheme(themeId);
    const action$ = isFollowed
      ? this.themeService.unfollowTheme(themeId)
      : this.themeService.followTheme(themeId);

    action$.subscribe({
      next: () => {
        // Met à jour la liste locale
        if (isFollowed) {
          this.followedThemes = this.followedThemes.filter(t => t.id !== themeId);
        } else {
          this.followedThemes = [...this.followedThemes, this.theme];
        }

        // Notifie le composant parent
        this.changed.emit({ id: themeId, followed: !isFollowed });
      },
      error: (err) => {
        console.error(err);
        alert('Une erreur est survenue lors de la mise à jour du suivi.');
      }
    });
  }
}
