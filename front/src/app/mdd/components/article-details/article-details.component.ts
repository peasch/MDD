import {Component, Input, OnInit} from '@angular/core';
import {Article} from "../../models/article.model";
import {Comment} from "../../models/comment.model";
import {ArticleService} from "../../../shared/services/article.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CommentService} from "../../../shared/services/comment.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../models/user.model";
import {UserService} from "../../../shared/services/user.service";


@Component({
  selector: 'app-article-details',
  templateUrl: './article-details.component.html',
  styleUrls: ['./article-details.component.scss']
})
export class ArticleDetailsComponent implements OnInit {
  article!: Article;
  comments!: Comment[];

  constructor(private articleService: ArticleService,
              private commentService: CommentService,
              private userService: UserService,
              private fb: FormBuilder,
              private route: ActivatedRoute,
              private router: Router) { }

  public form = this.fb.group({
    content: ['', [Validators.required, Validators.min(3)]]
  })

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    this.articleService.getArticleById(id).subscribe({
      next: (response: any) => {
        this.article = response.article;
      },
      error: (error) => {
        console.error('Error loading article:', error);
        this.router.navigate(['/404']);
      },
    });
    this.commentService.getCommentsOfArticle(id).subscribe({
      next: (response: any) => {
        // Vérifie que response.comments existe et que c'est bien un tableau
        if (Array.isArray(response.comments)) {
          this.comments = response.comments.sort(
            (a:any, b:any) =>
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

  addComment(id:number,form:FormGroup){
    const content = form.get('content')?.value;
    this.commentService.addComment(id,content).subscribe({
      next:(response:any)=>{
        this.router.navigateByUrl('/mdd/articles', { skipLocationChange: true })
          .then(() => this.router.navigate(['/mdd/articles/'+id]));
      },
      error: (error) => {
        console.error('Error loading article:', error);
        this.router.navigate(['/404']);
      }
    })
  }
}
