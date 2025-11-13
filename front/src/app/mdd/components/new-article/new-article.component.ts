import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../../../Services/auth.service";
import { ActivatedRoute, Router } from "@angular/router";
import { SessionService } from "../../../Services/session.service";
import { User } from "../../../interfaces/user.interface";
import { ThemeService } from "../../../shared/services/theme.service";
import { Theme } from "../../models/theme.model";
import { ArticleService } from "../../../shared/services/article.service";

/**
 * Composant permettant la création d’un nouvel article.
 *
 * Fonctionnalités :
 * - récupère la liste des thèmes disponibles ;
 * - récupère l’utilisateur actuellement connecté ;
 * - met à disposition un formulaire réactif pour la création d’article ;
 * - envoie les données de l’article à l’API ;
 * - redirige vers la liste des articles après une création réussie.
 *
 * @example
 * <app-new-article></app-new-article>
 */
@Component({
  selector: 'app-new-article',
  templateUrl: './new-article.component.html',
  styleUrls: ['./new-article.component.scss']
})
export class NewArticleComponent implements OnInit {

  /** Indique si une erreur s'est produite lors de la soumission du formulaire. */
  public onError: boolean = false;

  /** Liste des thèmes récupérés depuis l’API. */
  themes!: Theme[];

  /** Utilisateur actuellement connecté. */
  currentUser!: User;

  /** Options visuelles du formulaire (ex : Angular Material). */
  hideRequired = false;
  floatLabel: 'auto' | 'always' = 'auto';

  /**
   * Formulaire réactif permettant de créer un article.
   *
   * Champs :
   * - title   : titre de l’article (obligatoire)
   * - author  : groupe contenant les infos auteur
   * - theme   : groupe contenant le thème choisi
   * - content : contenu de l’article (min. 3 caractères)
   */
  public form = this.fb.group({
    title: ['', [Validators.required]],
    author: this.fb.group({
      id: '',
      name: '',
      email: '',
      username: ''
    }),
    theme: this.fb.group({
      id: '',
      name: ''
    }),
    content: ['', [Validators.required, Validators.min(3)]]
  });

  /**
   * @param authService Service gérant les informations utilisateur.
   * @param fb FormBuilder pour créer les formulaires réactifs.
   * @param router Service de navigation Angular.
   * @param sessionService Service de gestion de session utilisateur.
   * @param themeService Service pour récupérer les thèmes disponibles.
   * @param articleService Service chargé d’enregistrer les articles.
   * @param route Route active.
   */
  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService,
    private themeService: ThemeService,
    private articleService: ArticleService,
    private route: ActivatedRoute
  ) {}

  /**
   * Initialise le composant :
   * - récupère les thèmes disponibles ;
   * - récupère l’utilisateur actuellement connecté.
   */
  ngOnInit(): void {
    // Récupération des thèmes
    this.themeService.getThemes().subscribe({
      next: (response: any) => {
        this.themes = response.themes;
      }
    });

    // Récupération de l'utilisateur courant
    this.authService.getCurrentUser().subscribe({
      next: (response: any) => {
        this.currentUser = response.user;
      }
    });
  }

  /**
   * Soumet le formulaire et tente d’enregistrer un nouvel article.
   *
   * @param form Formulaire contenant les données saisies.
   */
  public onSubmit(form: FormGroup): void {
    const articleToSave = {
      author: this.currentUser,
      title: this.form.value.title,
      content: this.form.value.content,
      theme: this.form.value.theme
    };

    console.log(articleToSave);

    this.articleService.saveArticle(articleToSave).subscribe({
      next: () => {
        this.router.navigate(['/mdd/articles']);
      },
      error: (err) => {
        console.log(err);
        alert(err);
        this.onError = true;
      }
    });
  }
}
