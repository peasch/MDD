import {Theme} from "./theme.model";
import {User} from "../../mdd/models/user.model";

export class Article{
id!:number;
title!:string;
theme!:Theme;
content!:string;
createdAt!:string;
updatedAt!:string;
author!: User;

}
