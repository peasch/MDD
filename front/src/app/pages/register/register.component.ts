import {Component} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {RegisterRequest} from "../../interfaces/registerRequest.interface";
import {AuthSuccess} from "../../interfaces/authSuccess.interface";
import {AuthService} from "../../Services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent  {

  hide: boolean = false;
  onError: boolean = false;

  /** ✅ Regex de complexité :
   * - au moins 8 caractères
   * - au moins une majuscule
   * - au moins une minuscule
   * - au moins un chiffre
   * - au moins un caractère spécial
   */
  private passwordPattern =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;

  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    name: ['', [Validators.required, Validators.min(3)]],
    password: ['',[
      Validators.required,
      Validators.pattern(this.passwordPattern)
    ]]
  });

  constructor(private authService: AuthService,
              private fb: FormBuilder
              ) { }

  onSubmit() {
    const registerRequest = this.form.value as RegisterRequest;
    this.authService.register(registerRequest).subscribe(
      (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);

      }
    );
  }
  get passwordError(): string | null {
    const control = this.form.get('password');
    if (control?.hasError('required')) return 'Le mot de passe est requis.';
    if (control?.hasError('pattern')) {
      return 'Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.';
    }
    return null;
  }
}
