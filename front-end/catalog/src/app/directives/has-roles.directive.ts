import { Directive, Input, ElementRef, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Directive({
	selector: '[hasRoles]'
})
export class HasRolesDirective implements OnInit {

	@Input('hasRoles') roles: string[];

	constructor(private authService: AuthService, private el: ElementRef) { }

	ngOnInit(): void {
		
		const currentUser = this.authService.currentUser.getValue();

		if (!currentUser || !this.roles.every((role) => currentUser.roles.includes(role))) {
			this.el.nativeElement.style.display = 'none';
		}
	}
}
