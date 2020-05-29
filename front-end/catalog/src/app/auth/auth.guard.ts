import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, UrlTree, CanActivateChild } from '@angular/router';
import { UserService } from '../services/user.service';

@Injectable({
	providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild {

	constructor(private userService: UserService, private router: Router) {}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
		return this.doCanActivate(route);
	}

	canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
		return this.doCanActivate(route);
	}

	private doCanActivate(route: ActivatedRouteSnapshot) {
		const expectedRoles = route.data.roles as string[];
		const user = this.userService.currentUser.getValue();

		if (!user) {
			return this.router.parseUrl('/login');
		} else if (!expectedRoles || user.roles.some(role => expectedRoles.includes(role))) {
			return true;
		}

		return false;
	}
}
