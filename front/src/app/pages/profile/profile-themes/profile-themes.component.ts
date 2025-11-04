import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Theme } from "../../../mdd/models/theme.model";
import { ThemeService } from "../../../shared/services/theme.service";

@Component({
  selector: 'app-profile-themes',
  templateUrl: './profile-themes.component.html',
  styleUrls: ['./profile-themes.component.scss']
})
export class ProfileThemesComponent implements OnInit {

  /** La liste vient du parent */
  @Input() themes: Theme[] = [];
  @Input() theme!: Theme;

  /** Pour le binding bidirectionnel [(themes)] */
  @Output() themesChange = new EventEmitter<Theme[]>();

  /** (Optionnel) si tu veux aussi savoir quel id a été unfollow côté parent */
  @Output() unfollowed = new EventEmitter<number>();

  isUnfollowingId: number | null = null;

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {}

  unfollow(id: number): void {
    if (this.isUnfollowingId !== null) return;
    this.isUnfollowingId = id;

    this.themeService.unfollowTheme(id).subscribe({
      next: () => {
        // Met à jour la liste locale de l’enfant…
        const updated = this.themes.filter(t => t.id !== id);

        // …puis notifie le parent avec la nouvelle liste
        this.themesChange.emit(updated);

        // (Optionnel) remonte l’id pour d’autres usages côté parent
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
