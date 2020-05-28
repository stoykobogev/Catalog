import { Injectable, OnInit, OnDestroy } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, UrlTree, CanActivateChild } from '@angular/router';
import { UserService } from '../services/user.service';
import { User } from '../models/user.model';
import { Subscription } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild, OnInit, OnDestroy {

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

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
		return this.doCanActivate(route, state);
	}

	canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
		return this.doCanActivate(route, state);
	}

	private doCanActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		const expectedRoles = route.data.roles as string[];

		if (!this.currentUser) {
			return this.router.parseUrl('/login');
		} else if (!expectedRoles || this.currentUser.roles.some(role => expectedRoles.includes(role))) {
			return true;
		}

		return false;
	}
}
