import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { Article } from "../../models/article.model";
import { Router } from "@angular/router";
import { User } from "../../models/user.model";
import { UserService } from "../../../shared/services/user.service";

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit, OnChanges {
  @Input() article!: Article;
  author?: User;

  constructor(private router: Router,
              private userService: UserService) {}

  ngOnInit(): void {
    // Rien ici : article peut ne pas être prêt encore
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['article'] && this.article && this.article.author) {
      this.loadAuthor();
    }
  }

  private loadAuthor(): void {
    this.userService.getUserById(this.article.author).subscribe({
      next: (response: any) => {
        this.author = response.user;
      },
      error: (err) => console.error('Erreur de chargement de l’auteur :', err)
    });
  }

  goToArticle(id: number): void {
    this.router.navigate(['/mdd/articles', id]);
  }
}
