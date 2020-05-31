import { Injectable } from '@angular/core';
import {
	HttpRequest,
	HttpHandler,
	HttpEvent,
	HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

	private readonly JWT_HEADER = 'Authorization';

	constructor(private authService: AuthService) { }

	intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

		const currentUser = this.authService.currentUser.getValue();

		if (currentUser) {
			request = request.clone({
				headers: request.headers.append(this.JWT_HEADER, currentUser.jwt)
			});
		}

		return next.handle(request);
	}
}
