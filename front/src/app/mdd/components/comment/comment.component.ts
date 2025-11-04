import {Component, Input, OnInit} from '@angular/core';
import {Comment} from "../../models/comment.model";
import {User} from "../../models/user.model";
import {UserService} from "../../../shared/services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {

   @Input() comment!: Comment;
    author!: User;
  constructor(private userService: UserService,
              private router: Router) { }

  ngOnInit(): void {
    this.userService.getUserById(this.comment.authorId).subscribe({
      next :(response:any)=>{
        console.log(response.user);
        this.author=response.user;
      },
      error: (error) => {
        console.error('Error loading article:', error);
        this.router.navigate(['/404']);
      }
    });
  }

}
