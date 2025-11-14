import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { Article } from "../../models/article.model";
import { Router } from "@angular/router";
import { User } from "../../models/user.model";
import { UserService } from "../../../shared/services/user.service";

/**
 * Composant représentant un article sous forme de carte.
 *
 * Il reçoit un article en entrée et charge automatiquement
 * les informations de son auteur lorsque l’article change.
 *
 * @example
 * <app-article [article]="article"></app-article>
 */
@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit, OnChanges {

  /**
   * Article à afficher.
   *
   * Passé par le composant parent.
   */
  @Input() article!: Article;

  /** Informations de l’auteur de l’article (chargées dynamiquement). */
  author?: User;

  /**
   * @param router Pour naviguer vers la page détail d’un article.
   * @param userService Permet de récupérer les informations d’un utilisateur.
   */
  constructor(
    private router: Router,
    private userService: UserService
  ) {}

  /**
   * Cycle de vie OnInit.
   *
   * Rien ici, car `article` peut ne pas être encore fourni à l'initialisation.
   */
  ngOnInit(): void {}

  /**
   * Déclenché lorsque les propriétés @Input changent.
   *
   * Ici, si un nouvel article est reçu et qu’il possède un auteur,
   * on charge les informations de cet auteur.
   *
   * @param changes Objet contenant les modifications détectées.
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['article'] && this.article && this.article.authorId) {
      this.loadAuthor();
    }
  }

  /**
   * Charge les informations de l’auteur de l’article.
   *
   * Récupère l’utilisateur à partir de son ID dans l’article.
   */
  private loadAuthor(): void {
    this.userService.getUserById(this.article.authorId).subscribe({
      next: (response: any) => {
        this.author = response.user;
      },
      error: (err) => console.error('Erreur de chargement de l’auteur :', err)
    });
  }

  /**
   * Redirige vers la page de détail d’un article.
   *
   * @param id Identifiant de l’article vers lequel naviguer.
   */
  goToArticle(id: number): void {
    this.router.navigate(['/mdd/articles', id]);
  }
}
