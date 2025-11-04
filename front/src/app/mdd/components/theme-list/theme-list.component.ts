import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { Theme } from '../../models/theme.model';

@Component({
  selector: 'app-theme-list',
  templateUrl: './theme-list.component.html',
  styleUrls: ['./theme-list.component.scss']
})
export class ThemeListComponent implements OnInit {

  private themesSubject = new BehaviorSubject<Theme[]>([]);
  themes$: Observable<Theme[]> = this.themesSubject.asObservable();

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.data
      .pipe(map(d => (d['themes'] as { message: string; themes: Theme[] }).themes))
      .subscribe({
        next: (themes) => {
          // Dédoublonnage si besoin (sécurité)
          const deduped = this.dedupeThemes(themes);
          this.themesSubject.next(deduped);
        },
        error: (e) => console.error(e)
      });

  }

  onThemeChanged({ id, followed }: { id: number; followed: boolean }) {
    const next = this.themesSubject.value.map(t =>
      t.id === id ? { ...t, followed } : t
    );
    this.themesSubject.next(next);
  }

  /** Fonction trackBy commune avec le ProfileComponent */
  trackByTheme = (_: number, t: Theme) =>
    t?.id ??  `${t?.name}|${t?.id}`;

  /** (Optionnel) Dédoublonnage simple */
  private dedupeThemes(themes: Theme[]): Theme[] {
    const seen = new Set<string | number>();
    const out: Theme[] = [];
    for (const t of themes ?? []) {
      const key = t?.id ?? t?.name;
      if (!seen.has(key)) {
        seen.add(key);
        out.push(t);
      }
    }
    return out;
  }
}
