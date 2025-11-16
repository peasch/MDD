import { Component, OnInit } from '@angular/core';
import { Article } from "../../models/article.model";
import { Comment } from "../../models/comment.model";
import { ArticleService } from "../../../shared/services/article.service";
import { ActivatedRoute, Router } from "@angular/router";
import { CommentService } from "../../../shared/services/comment.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ThemeService } from "../../../shared/services/theme.service";
import { Theme } from "../../models/theme.model";

@Component({
  selector: 'app-article-details',
  templateUrl: './article-details.component.html',
  styleUrls: ['./article-details.component.scss']
})
export class ArticleDetailsComponent implements OnInit {

  /** Article à afficher (chargé dynamiquement). */
  article: Article | null = null;
  theme: Theme | null = null;

  /** Ensemble des commentaires liés à l’article. */
  comments: Comment[] = [];

  /** Formulaire d’ajout de commentaire */
  public form = this.fb.group({
    content: ['', [Validators.required, Validators.minLength(3)]]
  });

  constructor(
    private articleService: ArticleService,
    private commentService: CommentService,
    private themeService: ThemeService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];

    // Chargement de l’article
    this.articleService.getArticleById(id).subscribe({
      next: (response: any) => {
        this.article = response.article;

        // Chargement du thème associé
        this.themeService.getThemeById(response.article.themeId).subscribe({
          next: (themeResponse: any) => {
            this.theme = themeResponse.theme;
          },
          error: (error) => {
            console.error('Erreur lors du chargement du thème :', error);
          }
        });
      },
      error: (error) => {
        console.error('Error loading article:', error);
        this.router.navigate(['/404']);
      },
    });

    // Chargement des commentaires
    this.commentService.getCommentsOfArticle(id).subscribe({
      next: (response: any) => {
        if (Array.isArray(response.comments)) {
          this.comments = response.comments.sort(
            (a: any, b: any) =>
              new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
          );
        } else {
          this.comments = [];
          console.warn('La réponse ne contient pas de commentaires valides.');
        }
      },
      error: (error) => {
        console.error('Erreur lors du chargement des commentaires :', error);
        this.router.navigate(['/404']);
      }
    });
  }

  /**
   * Ajoute un commentaire à l’article.
   */
  addComment(id: number, form: FormGroup): void {
    if (form.invalid) return;

    const content = form.get('content')?.value;

    this.commentService.addComment(id, content).subscribe({
      next: () => {
        // Recharge la page d’article sans recharger le site
        this.router.navigateByUrl('/mdd/articles', { skipLocationChange: true })
          .then(() => this.router.navigate(['/mdd/articles', id]));
      },
      error: (error) => {
        console.error('Error adding comment:', error);
        this.router.navigate(['/404']);
      }
    });
  }
}
