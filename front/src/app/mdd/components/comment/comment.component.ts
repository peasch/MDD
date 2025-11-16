import { Component, Input, OnInit } from '@angular/core';
import { Comment } from "../../models/comment.model";
import { User } from "../../models/user.model";
import { UserService } from "../../../shared/services/user.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {

  @Input() comment!: Comment;

  /** Informations de l’auteur du commentaire. */
  author: User | null = null;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Sécurité : si jamais le parent ne fournit pas correctement le comment
    if (!this.comment || !this.comment.authorId) {
      console.warn('Comment ou authorId manquant dans CommentComponent', this.comment);
      return;
    }

    this.userService.getUserById(this.comment.authorId).subscribe({
      next: (response: any) => {
        this.author = response.user;
      },
      error: (error) => {
        console.error('Error loading comment author:', error);
        this.router.navigate(['/404']);
      }
    });
  }
}
