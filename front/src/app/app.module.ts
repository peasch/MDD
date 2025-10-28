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


@NgModule({
  declarations: [AppComponent, HomeComponent, LoginComponent, RegisterComponent, NotFoundComponent, ProfileComponent],
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
export class AppModule {
}
