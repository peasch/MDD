import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { Theme } from '../../models/theme.model';

/**
 * Composant affichant la liste des thèmes.
 *
 * Fonctionnalités :
 * - récupère les thèmes depuis les données résolues par la route ;
 * - dédoublonne les thèmes pour éviter les doublons ;
 * - expose un Observable (`themes$`) afin d'être utilisé facilement dans la vue ;
 * - met à jour l'état `followed` d’un thème lorsqu'un enfant émet un événement ;
 * - optimise l'affichage via un `trackBy` performant.
 *
 * @example
 * <app-theme-list></app-theme-list>
 */
@Component({
  selector: 'app-theme-list',
  templateUrl: './theme-list.component.html',
  styleUrls: ['./theme-list.component.scss']
})
export class ThemeListComponent implements OnInit {

  /**
   * Subject interne contenant la liste des thèmes.
   * Dédoublonnés et mis à jour dynamiquement.
   */
  private themesSubject = new BehaviorSubject<Theme[]>([]);

  /** Observable exposant la liste des thèmes à afficher. */
  themes$: Observable<Theme[]> = this.themesSubject.asObservable();

  /**
   * @param route Route active, contenant les thèmes résolus.
   */
  constructor(private route: ActivatedRoute) {}

  /**
   * Initialise le composant :
   * - récupère les thèmes via les données résolues de la route ;
   * - applique un dédoublonnage sécurisant.
   */
  ngOnInit(): void {
    this.route.data
      .pipe(map(d => (d['themes'] as { message: string; themes: Theme[] }).themes))
      .subscribe({
        next: (themes) => {
          const deduped = this.dedupeThemes(themes);
          this.themesSubject.next(deduped);
        },
        error: (e) => console.error(e)
      });
  }

  /**
   * Met à jour en mémoire locale l'état `followed` d’un thème.
   *
   * @param param0 Objet contenant l’id du thème et son nouvel état suivi.
   */
  onThemeChanged({ id, followed }: { id: number; followed: boolean }) {
    const next = this.themesSubject.value.map(t =>
      t.id === id ? { ...t, followed } : t
    );
    this.themesSubject.next(next);
  }

  /**
   * Fonction trackBy optimisant l’affichage du *ngFor*.
   *
   * @param index Index de l’élément.
   * @param t Thème concerné.
   */
  trackByTheme = (_: number, t: Theme) =>
    t?.id ?? `${t?.name}|${t?.id}`;

  /**
   * Dédoublonnage simple basé sur l'id ou le nom du thème.
   *
   * @param themes Liste des thèmes éventuellement dupliqués.
   * @returns Liste unique de thèmes.
   */
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
