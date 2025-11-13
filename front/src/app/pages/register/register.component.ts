import {Component} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {RegisterRequest} from "../../interfaces/registerRequest.interface";
import {AuthSuccess} from "../../interfaces/authSuccess.interface";
import {AuthService} from "../../Services/auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
/**
 * Composant responsable de la gestion du formulaire d'inscription.
 *
 * Fonctionnalités :
 * - Affiche un formulaire d'inscription avec email, nom et mot de passe.
 * - Valide la complexité du mot de passe via une regex.
 * - Envoie les données au service d'authentification pour créer un compte.
 * - Stocke le token JWT en cas de succès.
 */
export class RegisterComponent  {

  /**
   * Indique si le contenu des champs sensibles (mot de passe) doit être masqué.
   */
  hide: boolean = false;

  /**
   * Indique si une erreur est survenue lors de la tentative d'inscription.
   */
  onError: boolean = false;

  /**
   * Regex vérifiant la complexité du mot de passe :
   * - au moins 8 caractères
   * - une majuscule
   * - une minuscule
   * - un chiffre
   * - un caractère spécial
   */
  private passwordPattern =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;

  /**
   * Formulaire d'inscription réactif comprenant :
   * - email : requis + format email
   * - name : requis + min 3 caractères
   * - password : requis + pattern de complexité
   */
  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    name: ['', [Validators.required, Validators.min(3)]],
    password: ['',[Validators.required, Validators.pattern(this.passwordPattern)]]
  });

  /**
   * @param authService Service d'authentification responsable des requêtes API liées à l'inscription.
   * @param fb FormBuilder pour construire le formulaire.
   */
  constructor(
    private authService: AuthService,
    private fb: FormBuilder
  ) { }

  /**
   * Soumet le formulaire d'inscription si celui-ci est valide.
   *
   * - Transforme les valeurs du formulaire en `RegisterRequest`
   * - Appelle le service d'authentification pour créer un compte
   * - En cas de succès :
   *   - stocke le token JWT dans le localStorage
   *
   * En cas d'erreur :
   * - le flag `onError` peut être activé via un message d’erreur dans l'abonnement
   */
  onSubmit() {
    const registerRequest = this.form.value as RegisterRequest;

    this.authService.register(registerRequest).subscribe(
      (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
      }
    );
  }

  /**
   * Renvoie le message d'erreur approprié pour le champ mot de passe.
   *
   * @returns Le message d'erreur à afficher ou `null` si aucune erreur.
   */
  get passwordError(): string | null {
    const control = this.form.get('password');

    if (control?.hasError('required'))
      return 'Le mot de passe est requis.';

    if (control?.hasError('pattern')) {
      return 'Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.';
    }

    return null;
  }
}
