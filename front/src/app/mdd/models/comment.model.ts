import {User} from "./user.model";

export class Comment{
  id!:number;
  content!: string;
  authorId!: number;
  createdAt!:string;
}
