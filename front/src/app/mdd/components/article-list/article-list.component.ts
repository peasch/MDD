import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { map } from "rxjs/operators";
import { Observable, BehaviorSubject } from "rxjs";
import { Article } from "../../models/article.model";

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.scss']
})
export class ArticleListComponent implements OnInit {

  private allArticles: Article[] = [];
  private articlesSubject = new BehaviorSubject<Article[]>([]);
  articles$ = this.articlesSubject.asObservable();

  sortOrder: 'asc' | 'desc' = 'desc';

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.route.data
      .pipe(map(d => (d['articles'] as { articles: Article[] }).articles))
      .subscribe(articles => {
        // 1) Dédoublonner par id/slug (fallback titre+date)
        this.allArticles = this.dedupe(
          articles,
          a => (a as any).id ?? (a as any).slug ?? `${a.title}|${a.createdAt}`
        );
        // 2) Trier et émettre
        this.applySort();
      });
  }

  toggleSortOrder(): void {
    this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    this.applySort();
  }

  private applySort(): void {
    const sorted = [...this.allArticles].sort((a, b) => {
      const dateA = a?.createdAt ? new Date(a.createdAt as any).getTime() : 0;
      const dateB = b?.createdAt ? new Date(b.createdAt as any).getTime() : 0;
      return this.sortOrder === 'asc' ? dateA - dateB : dateB - dateA;
    });
    this.articlesSubject.next(sorted);
  }

  // Utilitaire: dédoublonnage par clé unique
  private dedupe<T>(arr: T[], keySelector: (x: T) => string | number): T[] {
    const map = new Map<string | number, T>();
    for (const item of arr) {
      const key = keySelector(item);
      if (!map.has(key)) map.set(key, item);
    }
    return Array.from(map.values());
  }

  // trackBy pour *ngFor — limite les rerenders et évite les "doublons visuels"
  trackByArticle(_: number, article: any) {
    return article?.id ?? article?.slug ?? `${article?.title}|${article?.date}`;
  }
}
