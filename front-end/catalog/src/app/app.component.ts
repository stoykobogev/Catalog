import { Component, OnInit, OnDestroy } from '@angular/core';
import { UserService } from './services/user.service';
import { Router } from '@angular/router';
import { User } from './models/user.model';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html'
})
export class AppComponent implements OnInit, OnDestroy {

	currentUser: User;
	subscription: Subscription;

	constructor(private userService: UserService, private router: Router) {}

	ngOnInit(): void {
		this.subscription = this.userService.currentUser.subscribe((user) => {
			this.currentUser = user;
		});
	}

	ngOnDestroy(): void {
		this.subscription.unsubscribe();
	}

	logout(): void {
		this.userService.logout();
		this.router.navigateByUrl('/login');
	}
}
