import { Component, Input, OnInit } from '@angular/core';
import { Article } from "../../models/article.model";
import { Comment } from "../../models/comment.model";
import { ArticleService } from "../../../shared/services/article.service";
import { ActivatedRoute, Router } from "@angular/router";
import { CommentService } from "../../../shared/services/comment.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { User } from "../../models/user.model";
import { UserService } from "../../../shared/services/user.service";

/**
 * Page d’affichage des détails d’un article.
 *
 * Ce composant :
 * - charge l’article sélectionné ;
 * - récupère et trie les commentaires associés ;
 * - permet d’ajouter un commentaire via un formulaire réactif ;
 * - redirige vers la page 404 en cas d’erreur de chargement.
 *
 * @example
 * <app-article-details></app-article-details>
 */
@Component({
  selector: 'app-article-details',
  templateUrl: './article-details.component.html',
  styleUrls: ['./article-details.component.scss']
})
export class ArticleDetailsComponent implements OnInit {

  /** Article à afficher (chargé dynamiquement). */
  article!: Article;

  /** Ensemble des commentaires liés à l’article. */
  comments!: Comment[];

  /**
   * Formulaire réactif utilisé pour publier un commentaire.
   *
   * Champs :
   * - content : contenu du commentaire (min. 3 caractères, obligatoire)
   */
  public form = this.fb.group({
    content: ['', [Validators.required, Validators.min(3)]]
  });

  /**
   * @param articleService Service permettant de récupérer les détails d’un article.
   * @param commentService Service gérant les commentaires d’un article.
   * @param userService Service permettant de récupérer les informations des utilisateurs.
   * @param fb FormBuilder pour construire les formulaires réactifs.
   * @param route Route active permettant de récupérer le paramètre `id`.
   * @param router Service Angular de navigation.
   */
  constructor(
    private articleService: ArticleService,
    private commentService: CommentService,
    private userService: UserService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  /**
   * Initialise le composant :
   * - récupère l’ID depuis l’URL ;
   * - charge l’article correspondant ;
   * - charge et trie les commentaires (du plus récent au plus ancien).
   */
  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];

    // Chargement de l’article
    this.articleService.getArticleById(id).subscribe({
      next: (response: any) => {
        this.article = response.article;
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

        console.log('Commentaires triés :', this.comments);
      },
      error: (error) => {
        console.error('Erreur lors du chargement des commentaires :', error);
        this.router.navigate(['/404']);
      }
    });
  }

  /**
   * Ajoute un commentaire à l’article.
   *
   * @param id Identifiant de l’article concerné.
   * @param form Formulaire contenant le contenu du commentaire.
   */
  addComment(id: number, form: FormGroup): void {
    const content = form.get('content')?.value;

    this.commentService.addComment(id, content).subscribe({
      next: () => {
        // Recharge la page d’article sans recharger l’ensemble du site
        this.router.navigateByUrl('/mdd/articles', { skipLocationChange: true })
          .then(() => this.router.navigate(['/mdd/articles/' + id]));
      },
      error: (error) => {
        console.error('Error adding comment:', error);
        this.router.navigate(['/404']);
      }
    });
  }
}
