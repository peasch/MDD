import { Component } from '@angular/core';
import { FormBuilder, Validators } from "@angular/forms";
import { RegisterRequest } from "../../interfaces/registerRequest.interface";
import { AuthSuccess } from "../../interfaces/authSuccess.interface";
import { AuthService } from "../../Services/auth.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
/**
 * Composant responsable de la gestion du formulaire d'inscription.
 */
export class RegisterComponent  {

  hide: boolean = false;
  onError: boolean = false;

  private passwordPattern =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;

  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    username: ['', [Validators.required, Validators.minLength(3)]],
    password: ['', [Validators.required, Validators.pattern(this.passwordPattern)]]
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) { }

  onSubmit(): void {
    // Sécurité : ne rien envoyer si le formulaire est invalide
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const registerRequest = this.form.value as RegisterRequest;

    this.authService.register(registerRequest).subscribe({
      next: (response: AuthSuccess) => {
        // Si tu veux garder le token à l'inscription
        localStorage.setItem('token', response.token);

        // Pop-up de validation
        alert('Vous êtes bien enregistré! ');

        // Redirection vers /login
        this.router.navigate(['/mdd/articles']);
      },
      error: (error) => {
        console.error('Erreur lors de l’inscription :', error);
        this.onError = true;
      }
    });
  }

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
