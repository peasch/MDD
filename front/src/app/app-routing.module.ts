import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {NotFoundComponent} from "./pages/not-found/not-found.component";
import {AuthGuard} from "./core/guards/auth.guard";
import {ProfileComponent} from "./pages/profile/profile.component";



// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [
  {path: '', component: HomeComponent},
  {title: 'Login', path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path:'profile',component: ProfileComponent,canActivate:[AuthGuard]},
  {path:'mdd',loadChildren:() => import('./mdd/mdd.module').then(m=>m.MddModule),canActivate:[AuthGuard]},
  {path: '404', component: NotFoundComponent},
  {path: '**', redirectTo: 'mdd/articles'}

  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
