import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

	loginForm: FormGroup;
	usernameControl: FormControl;
	passwordControl: FormControl

	constructor(private userService: UserService, private formBuilder: FormBuilder, private router: Router) { }

	ngOnInit(): void {

		this.usernameControl = this.formBuilder.control(
			'', Validators.required
		);

		this.passwordControl = this.formBuilder.control(
			'', Validators.required
		);

		this.loginForm = this.formBuilder.group({
            username: this.usernameControl,
            password: this.passwordControl
        });
	}

	submit(): void {

		if (this.loginForm.invalid) {
            return;
        }

        this.userService.login(this.usernameControl.value, this.passwordControl.value)
            .subscribe(() => {
				this.router.navigateByUrl('/categories')
			});
	}
}
