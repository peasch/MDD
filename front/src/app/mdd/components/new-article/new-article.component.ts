import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../Services/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {SessionService} from "../../../Services/session.service";
import {User} from "../../../interfaces/user.interface";
import {ThemeService} from "../../../shared/services/theme.service";
import {Theme} from "../../models/theme.model";
import {ArticleService} from "../../../shared/services/article.service";

@Component({
  selector: 'app-new-article',
  templateUrl: './new-article.component.html',
  styleUrls: ['./new-article.component.scss']
})
export class NewArticleComponent implements OnInit {

  public onError: boolean = false;
  themes!:Theme[];
  currentUser!: User;
  constructor(private authService: AuthService,
              private fb: FormBuilder,
              private router: Router,
              private sessionService: SessionService,
              private themeService: ThemeService,
              private articleService: ArticleService,
              private route: ActivatedRoute) { }
  hideRequired = false;
  floatLabel: 'auto' | 'always' = 'auto';


  ngOnInit(): void {
    this.themeService.getThemes().subscribe({
      next:(response:any)=>{
        this.themes = response.themes;
      }
    })

    this.authService.getCurrentUser().subscribe({
      next: (response: any) => {
        this.currentUser = response.user;
      }
    })
  }

  public form = this.fb.group({
    title : ['', [Validators.required]],
    author:this.fb.group({
      id:'',
      name:'',
      email:'',
      username:''}),
    theme : this.fb.group({
      id:'',
      name:''}),
    content: ['', [Validators.required, Validators.min(3)]]
  });



  public onSubmit(form:FormGroup):void {
    const articleToSave={
      author:this.currentUser,
      title:this.form.value.title,
      content:this.form.value.content,
      theme:this.form.value.theme
    }
  console.log(articleToSave)

    this.articleService.saveArticle(articleToSave).subscribe(res =>{
        this.router.navigate(['/mdd/articles']);
      }, err => {
      console.log(err);
      alert(err);
    })

  }

}
