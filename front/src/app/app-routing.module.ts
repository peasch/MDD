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
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  {
    path: 'mdd',
    loadChildren: () => import('./mdd/mdd.module').then(m => m.MddModule),
    canActivate: [AuthGuard]
  },
  {path: '404', component: NotFoundComponent},
  {path: '**', redirectTo: 'mdd/articles'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
/**
 * Module de routage principal de l'application.
 *
 * Il définit l'ensemble des routes accessibles depuis l'application ainsi que :
 * - les composants associés à chaque chemin
 * - les guards de protection d'accès (via `canActivate`)
 * - les modules chargés paresseusement (`lazy loading`)
 *
 * Points importants :
 * - La route `'profile'` et le module `'mdd'` sont protégés par `AuthGuard`,
 *   empêchant l’accès aux utilisateurs non authentifiés.
 * - La route wildcard `**` redirige vers `'mdd/articles'` pour éviter les erreurs 404.
 * - Le lazy loading du module MDD optimise les performances
 *   en ne chargeant ses composants qu'à la demande.
 *
 * Ce module configure et exporte `RouterModule` avec les routes définies,
 * et doit être importé dans l'`AppModule`.
 */
export class AppRoutingModule {}
