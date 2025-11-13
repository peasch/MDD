import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { map } from "rxjs/operators";
import { Observable, BehaviorSubject } from "rxjs";
import { Article } from "../../models/article.model";

/**
 * Composant affichant la liste des articles.
 *
 * Fonctionnalités :
 * - récupère les articles depuis les resolvers de route ;
 * - dédoublonne les articles selon une clé unique ;
 * - applique un tri (ascendant ou descendant) basé sur la date de création ;
 * - expose la liste triée via un Observable (`articles$`) utilisé dans la vue ;
 * - optimise le *ngFor* via un `trackBy` personnalisé.
 */
@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.scss']
})
export class ArticleListComponent implements OnInit {

  /** Liste complète des articles récupérés avant tri et filtrage. */
  private allArticles: Article[] = [];

  /**
   * Subject contenant les articles triés et dédoublonnés.
   * Émis sous forme d’Observable pour utilisation dans le template.
   */
  private articlesSubject = new BehaviorSubject<Article[]>([]);

  /** Observable exposant la liste d'articles affichée dans la vue. */
  articles$ = this.articlesSubject.asObservable();

  /** Sens du tri : 'asc' = du plus ancien au plus récent, 'desc' = inverse. */
  sortOrder: 'asc' | 'desc' = 'desc';

  /**
   * @param route Permet de récupérer les données résolues sur la route.
   * @param router Permet d’effectuer des navigations programmatiques.
   */
  constructor(private route: ActivatedRoute, private router: Router) {}

  /**
   * Initialise le composant :
   * - récupère les articles depuis le resolver ;
   * - dédoublonne les résultats ;
   * - applique un tri initial.
   */
  ngOnInit(): void {
    this.route.data
      .pipe(map(d => (d['articles'] as { articles: Article[] }).articles))
      .subscribe(articles => {
        // 1) Dédoublonner par id / slug / fallback titre+date
        this.allArticles = this.dedupe(
          articles,
          a => (a as any).id ?? (a as any).slug ?? `${a.title}|${a.createdAt}`
        );

        // 2) Trier et émettre la liste
        this.applySort();
      });
  }

  /**
   * Inverse l’ordre de tri et met à jour la liste affichée.
   */
  toggleSortOrder(): void {
    this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    this.applySort();
  }

  /**
   * Trie les articles par date de création puis les émet via `articlesSubject`.
   */
  private applySort(): void {
    const sorted = [...this.allArticles].sort((a, b) => {
      const dateA = a?.createdAt ? new Date(a.createdAt as any).getTime() : 0;
      const dateB = b?.createdAt ? new Date(b.createdAt as any).getTime() : 0;
      return this.sortOrder === 'asc' ? dateA - dateB : dateB - dateA;
    });
    this.articlesSubject.next(sorted);
  }

  /**
   * Supprime les doublons d’un tableau en utilisant une fonction fournissant une clé unique.
   *
   * @param arr Tableau d’éléments.
   * @param keySelector Fonction retournant une clé unique pour chaque élément.
   */
  private dedupe<T>(arr: T[], keySelector: (x: T) => string | number): T[] {
    const map = new Map<string | number, T>();
    for (const item of arr) {
      const key = keySelector(item);
      if (!map.has(key)) map.set(key, item);
    }
    return Array.from(map.values());
  }

  /**
   * Optimisation du *ngFor* : identifie un article de manière unique
   * pour éviter des rerenders inutiles.
   *
   * @param index Index de l’élément dans la boucle.
   * @param article Article concerné.
   * @returns Une clé unique pour l’article.
   */
  trackByArticle(_: number, article: any) {
    return article?.id ?? article?.slug ?? `${article?.title}|${article?.date}`;
  }
}
