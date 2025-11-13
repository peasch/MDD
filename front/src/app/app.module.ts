import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from './pages/home/home.component';
import {LoginComponent} from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {ReactiveFormsModule} from "@angular/forms";

import {NgOptimizedImage} from "@angular/common";
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {JwtInterceptor} from "./interceptors/jwt.interceptor";
import {NotFoundComponent} from './pages/not-found/not-found.component';

import {CoreModule} from "./core/core.module";
import {SharedModule} from "./shared/shared.module";
import {ProfileComponent} from './pages/profile/profile.component';
import {MaterialModule} from "./shared/material.module";
import {MddModule} from "./mdd/mdd.module";
import { ProfileThemesComponent } from './pages/profile/profile-themes/profile-themes.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    NotFoundComponent,
    ProfileComponent,
    ProfileThemesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    NgOptimizedImage,
    HttpClientModule,
    CoreModule,
    SharedModule,
    MaterialModule,
    MddModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
/**
 * Module racine de l'application Angular.
 *
 * Il centralise :
 * - la déclaration des composants globaux (Home, Login, Register, Profile, etc.)
 * - l'import des modules essentiels (Browser, Router, Animations, Forms)
 * - l'import des modules architecturaux (CoreModule, SharedModule, MddModule)
 * - la configuration des providers généraux comme les interceptors HTTP
 *
 * Ce module est chargé au démarrage de l'application et initialise le `AppComponent`
 * comme composant principal (bootstrap).
 *
 * Structure globale :
 * - **CoreModule** : Services singletons, interceptors, gestion du cœur de l'app.
 * - **SharedModule** : Composants / modules réutilisables (Material, directives, pipes).
 * - **MddModule** : Fonctionnalités métier (thèmes, articles, modèles MDD).
 * - **MaterialModule** : Modules Angular Material centralisés.
 *
 * L’`AppModule` orchestre ainsi toute la configuration technique nécessaire au fonctionnement global.
 */
export class AppModule {}
