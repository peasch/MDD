import { Component, Input, OnInit } from '@angular/core';
import { Comment } from "../../models/comment.model";
import { User } from "../../models/user.model";
import { UserService } from "../../../shared/services/user.service";
import { Router } from "@angular/router";

/**
 * Composant d’affichage d’un commentaire.
 *
 * Il reçoit un commentaire en entrée et récupère automatiquement
 * les informations de l’auteur associées à ce commentaire.
 *
 * @example
 * <app-comment [comment]="comment"></app-comment>
 */
@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {

  /**
   * Commentaire à afficher.
   *
   * Le composant parent doit fournir un objet `Comment`.
   */
  @Input() comment!: Comment;

  /** Informations de l’auteur du commentaire. */
  author!: User;

  /**
   * @param userService Service permettant de récupérer les informations d’un utilisateur.
   * @param router Service de navigation (utilisé en cas d’erreur pour redirection).
   */
  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  /**
   * Initialise le composant :
   * - récupère l’auteur du commentaire via son `authorId`.
   * - redirige vers la page 404 en cas d’erreur.
   */
  ngOnInit(): void {
    this.userService.getUserById(this.comment.authorId).subscribe({
      next: (response: any) => {
        console.log(response.user);
        this.author = response.user;
      },
      error: (error) => {
        console.error('Error loading comment author:', error);
        this.router.navigate(['/404']);
      }
    });
  }
}
