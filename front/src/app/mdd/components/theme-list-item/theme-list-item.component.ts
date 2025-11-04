import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Theme } from '../../models/theme.model';
import { User } from '../../models/user.model';
import { AuthService } from '../../../Services/auth.service';
import { ThemeService } from '../../../shared/services/theme.service';

@Component({
  selector: 'app-theme-list-item',
  templateUrl: './theme-list-item.component.html',
  styleUrls: ['./theme-list-item.component.scss']
})
export class ThemeListItemComponent implements OnInit {

  @Input() theme!: Theme;
  @Output() changed = new EventEmitter<{ id: number; followed: boolean }>();

  currentUser!: User;
  followedThemes: Theme[] = [];

  constructor(
    private authService: AuthService,
    private themeService: ThemeService
  ) {}

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (response: any) => {
        this.currentUser = response.user;
        this.followedThemes = response.user.followedThemes || [];
      },
      error: (err) => console.error(err)
    });
  }

  hasTheme(themeId: number): boolean {
    return this.followedThemes?.some(theme => theme.id === themeId);
  }

  changeFollow(themeId: number): void {
    const isFollowed = this.hasTheme(themeId);
    const action$ = isFollowed
      ? this.themeService.unfollowTheme(themeId)
      : this.themeService.followTheme(themeId);

    action$.subscribe({
      next: (res) => {
        // Met à jour la liste locale
        if (isFollowed) {
          this.followedThemes = this.followedThemes.filter(t => t.id !== themeId);
        } else {
          this.followedThemes = [...this.followedThemes, this.theme];
        }

        // Émet l’événement pour que le parent mette à jour sa propre liste
        this.changed.emit({ id: themeId, followed: !isFollowed });
      },
      error: (err) => {
        console.error(err);
        alert('Une erreur est survenue lors de la mise à jour du suivi.');
      }
    });
  }
}
