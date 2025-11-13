import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Theme } from "../../../mdd/models/theme.model";
import { ThemeService } from "../../../shared/services/theme.service";

/**
 * Composant d'affichage et de gestion des thèmes suivis dans la page de profil.
 *
 * Fonctionnalités :
 * - Affiche la liste des thèmes suivis par l’utilisateur.
 * - Permet de se désabonner (unfollow) d’un thème.
 * - Met à jour la liste locale puis notifie le composant parent.
 * - Émet également l’identifiant du thème désabonné si nécessaire.
 *
 * @example
 * <app-profile-themes
 *    [themes]="followedThemes"
 *    (themesChange)="onThemesUpdated($event)"
 *    (unfollowed)="onThemeUnfollowed($event)">
 * </app-profile-themes>
 */
@Component({
  selector: 'app-profile-themes',
  templateUrl: './profile-themes.component.html',
  styleUrls: ['./profile-themes.component.scss']
})
export class ProfileThemesComponent implements OnInit {

  /**
   * Liste des thèmes suivis – fournie par le parent.
   * Peut être mise à jour et renvoyée au parent via `themesChange`.
   */
  @Input() themes: Theme[] = [];

  /** Thème éventuellement fourni séparément. */
  @Input() theme!: Theme;

  /**
   * Événement permettant le binding bidirectionnel :
   * le parent peut écouter `(themesChange)` pour mettre à jour son modèle.
   */
  @Output() themesChange = new EventEmitter<Theme[]>();

  /**
   * Événement optionnel pour transmettre au parent uniquement l’id du thème unfollowé.
   */
  @Output() unfollowed = new EventEmitter<number>();

  /**
   * Stocke l’id du thème en cours de désabonnement
   * pour gérer les états "loading".
   */
  isUnfollowingId: number | null = null;

  /**
   * @param themeService Service permettant le follow/unfollow des thèmes.
   */
  constructor(private themeService: ThemeService) {}

  /** Cycle de vie Angular – rien à initialiser ici. */
  ngOnInit(): void {}

  /**
   * Désabonne l’utilisateur du thème donné.
   *
   * @param id Identifiant du thème à désabonner.
   */
  unfollow(id: number): void {
    if (this.isUnfollowingId !== null) return; // Évite les clics multiples
    this.isUnfollowingId = id;

    this.themeService.unfollowTheme(id).subscribe({
      next: () => {
        // Met à jour la liste locale
        const updated = this.themes.filter(t => t.id !== id);

        // Notifie le parent avec la nouvelle liste
        this.themesChange.emit(updated);

        // Notifie également le parent de l’id désabonné (optionnel)
        this.unfollowed.emit(id);

        this.isUnfollowingId = null;
      },
      error: (err) => {
        console.error(err);
        this.isUnfollowingId = null;
        alert("Une erreur est survenue lors du désabonnement du thème.");
      }
    });
  }
}
